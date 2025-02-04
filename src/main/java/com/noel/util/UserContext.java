package com.noel.util;

import com.amazonaws.services.cognitoidp.model.UnauthorizedException;
import com.noel.model.Profile;
import com.noel.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@Data
@RequestScope
public class UserContext {
    private User user;

    public void assertAdmin() {
        if (user == null || !Profile.ADMIN.equals(user.getProfile()))
            throw new UnauthorizedException("The User has no rights to access the endpoint");
    }
}
