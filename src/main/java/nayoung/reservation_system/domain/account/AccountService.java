package nayoung.reservation_system.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.account.AccountException;
import nayoung.reservation_system.web.account.model.SignInRequest;
import nayoung.reservation_system.web.account.model.SignInResponse;
import nayoung.reservation_system.web.account.model.SignUpRequest;
import nayoung.reservation_system.web.account.model.SignUpResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountValidator validator;
    private final AccountRepository accountRepository;

    public SignUpResponse signUp(SignUpRequest request) {
        validator.duplicateUsername(request.getUsername());

        Account account = Account.fromSignUpRequest(request);
        Account savedAccount = accountRepository.save(account);
        return SignUpResponse.fromUsername(true, savedAccount.getUsername());
    }

    public SignInResponse signIn(SignInRequest request) {
        validator.existByUsername(request.getUsername());

        Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new AccountException(ExceptionCode.PASSWORD_MISMATCH));

        return SignInResponse.fromUsername(true, account.getUsername());
    }
}
