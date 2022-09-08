package nayoung.reservation_system.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.account.AccountException;
import nayoung.reservation_system.exception.account.NotFoundAccountException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountValidator {

    private final AccountRepository accountRepository;

    public void duplicateUsername(String username) {
        if(accountRepository.existsByUsername(username)) {
            log.warn("[UserValidator] DUPLICATE USERNAME EXCEPTION");
            throw new AccountException(ExceptionCode.DUPLICATE_USERNAME);
        }
    }

    public Long existByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT));

        return account.getId();
    }
}
