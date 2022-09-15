package nayoung.reservation_system.web.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AccountDetailRequest {

    private String username;

    private String password;

    private String bio;
}
