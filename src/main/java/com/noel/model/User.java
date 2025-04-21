package com.noel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;
    private String login;
    private String password;
    private String phone;
    private String email;

    private Profile profile;
    private Date created;
    private Date modified;

    public void setProfile (String profile) {
        try {
            this.profile = Profile.valueOf(profile);
        }
        catch (Exception e) {
            this.profile = Profile.USER;
        }
    }

}
