package nayoung.reservation_system.web.account.model;

import lombok.Getter;

@Getter
public class SignUpResponse {

    private boolean eligible;
    private String username;

    private SignUpResponse(boolean eligible, String username) {
        this.eligible = eligible;
        this.username = username;
    }

    public static SignUpResponse fromUsername(boolean eligible, String username) {
        return new SignUpResponse(eligible, username);
    }
}
