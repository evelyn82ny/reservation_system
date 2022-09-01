package nayoung.reservation_system.domain.account;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.web.account.model.SignUpRequest;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Account fromSignUpRequest(SignUpRequest request) {
        return new Account(request.getUsername(), request.getPassword());
    }
}