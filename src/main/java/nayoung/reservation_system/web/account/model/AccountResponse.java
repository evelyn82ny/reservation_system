package nayoung.reservation_system.web.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nayoung.reservation_system.domain.account.Account;

@Getter
@NoArgsConstructor
public class AccountResponse extends MyPageResponse{

    private Long balance;

    protected AccountResponse(String username, String bio, Long balance) {
        super(username, bio);
        this.balance = balance;
    }

    public static AccountResponse of(Account account) {
        return new AccountResponse(account.getUsername(), account.getBio(), account.getBalance());
    }
}
