package nayoung.reservation_system.web.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nayoung.reservation_system.domain.account.Account;

@Getter
@NoArgsConstructor
public class MyPageResponse {

    private String username;

    private String bio;

    protected MyPageResponse(String username, String bio) {
        this.username = username;
        this.bio = bio;
    }

    public static MyPageResponse of(Account account) {
        return new MyPageResponse(account.getUsername(), account.getBio());
    }
}
