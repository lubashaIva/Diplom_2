package org.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateRequest {

    private String email;
    private String password;
    private String name;

    public UserCreateRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}