package nayoung.reservation_system.domain.account;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.web.account.model.AccountDetailRequest;
import nayoung.reservation_system.web.account.model.SignUpRequest;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private Long balance;

    private String bio;

    private Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0L;
    }

    public static Account fromSignUpRequest(SignUpRequest request) {
        return new Account(request.getUsername(), request.getPassword());
    }

    public void increaseBalance(Long money) {
        this.balance += money;
    }

    public void decreaseBalance(Long money) {
        this.balance -= money;
    }

    public void update(AccountDetailRequest request) {
        if(StringUtils.hasText(request.getUsername())) {
            this.username = request.getUsername();
        }
        if(StringUtils.hasText(request.getPassword())) {
            this.password = request.getPassword();
        }
        if(StringUtils.hasText(request.getBio())) {
            this.bio = request.getBio();
        }
    }
}