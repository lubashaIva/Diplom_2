package org.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginResponse {

    private UserLoginObject user;
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private String message;

    public UserLoginResponse(UserLoginObject user, boolean success, String accessToken, String refreshToken, String message) {
        this.user = user;
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }

    public UserLoginResponse() {}
}
