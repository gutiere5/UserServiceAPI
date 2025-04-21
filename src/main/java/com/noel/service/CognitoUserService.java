package com.noel.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import com.noel.config.CognitoConfiguration;
import com.noel.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CognitoUserService implements UserService{

    private final CognitoConfiguration configuration;
    private final UserBuilderHelper userBuilderHelper;
    private AWSCognitoIdentityProvider provider;

    public CognitoUserService(CognitoConfiguration configuration, UserBuilderHelper userBuilderHelper) {
        this.configuration = configuration;
        this.userBuilderHelper = userBuilderHelper;
        this.provider = constructProvider(configuration);
    }

    private AWSCognitoIdentityProvider constructProvider(CognitoConfiguration configuration) {
        return AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(configuration.getCredentialProvider())
                .withRegion(configuration.getRegion())
                .build();
    }

    @Override
    public User create(User user) {
        var createdUser = createDefaultPasswordUser(user);
        updateWithPermanentPassword(user.getLogin(), user.getPassword());

        return createdUser;
    }

    // It works fine until the User pool contains less than 60 items
    // After, that you have to do user pagination token to get all users chunk by chunk
    @Override
    public List<User> getAllUsers() {
        var request = new ListUsersRequest()
                .withUserPoolId(configuration.getPoolId());

        var userResult = new ListUsersResult();
        List<User> allUsers = new ArrayList<>();
        do{
            request.setPaginationToken(userResult.getPaginationToken());
            userResult = provider.listUsers(request);
            allUsers.addAll(userResult.getUsers()
                    .stream()
                    .map(userBuilderHelper::build)
                    .toList());
        }
        while (Objects.nonNull(userResult.getPaginationToken()));

        return allUsers;

    }

    public List<User> getAll() {
        var request = new ListUsersRequest()
                .withUserPoolId(configuration.getPoolId());

        return provider.listUsers(request)
                .getUsers()
                .stream()
                .map(userBuilderHelper::build)
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(String userId) {
        var request = new ListUsersRequest()
                .withUserPoolId(configuration.getPoolId())
                .withFilter(String.format("sub=\"%s\"",userId ));

        return provider.listUsers(request)
                .getUsers()
                .stream()
                .findFirst()
                .map(userBuilderHelper::build)
                .orElseThrow();
    }


    private void updateWithPermanentPassword(String login, String password) {
        if (login == null || password == null) {
            throw new IllegalArgumentException("Login and password must not be null.");
        }

        System.out.printf("Setting permanent password for user: %s in pool: %s%n", login, configuration.getPoolId());

        var passwordRequest = new AdminSetUserPasswordRequest()
                .withUsername(login)
                .withPassword(password)
                .withPermanent(true)
                .withUserPoolId(configuration.getPoolId());

        provider.adminSetUserPassword(passwordRequest);
    }

    private User createDefaultPasswordUser(User user) {
        try {
            return Optional.of(user)
                    .map(userBuilderHelper::adminCreateUserRequest)
                    .map(provider::adminCreateUser)
                    .map(AdminCreateUserResult::getUser)
                    .map(userBuilderHelper::build)
                    .orElseThrow();
        } catch (Exception e) {
            System.err.printf("Error creating user: %s. Exception: %s%n", user.getLogin(), e.getMessage());
            throw e; // Re-throw or handle accordingly
        }
    }
}
