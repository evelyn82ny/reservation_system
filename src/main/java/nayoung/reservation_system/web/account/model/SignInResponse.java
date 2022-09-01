package nayoung.reservation_system.web.account.model;

import lombok.Getter;

@Getter
public class SignInResponse {

    private boolean eligible;
    private String username;

    private SignInResponse(boolean eligible, String username) {
        this.eligible = eligible;
        this.username = username;
    }

    public static SignInResponse fromUsername(boolean eligible, String username) {
        return new SignInResponse(eligible, username);
    }
}
