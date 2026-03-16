package org.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {

    private String email;
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
