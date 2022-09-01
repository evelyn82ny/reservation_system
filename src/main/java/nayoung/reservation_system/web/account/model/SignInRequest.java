package nayoung.reservation_system.web.account.model;

import lombok.Getter;

@Getter
public class SignInRequest {

    private String username;
    private String password;
}