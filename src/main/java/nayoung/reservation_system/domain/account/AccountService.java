package nayoung.reservation_system.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.web.account.model.SignInRequest;
import nayoung.reservation_system.web.account.model.SignInResponse;
import nayoung.reservation_system.web.account.model.SignUpRequest;
import nayoung.reservation_system.web.account.model.SignUpResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public SignUpResponse signUp(SignUpRequest request) {
        Account savedAccount = null;
        try {
            Account account = Account.fromSignUpRequest(request);
            savedAccount = accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            return SignUpResponse.fromUsername(false, request.getUsername());
        }
        return SignUpResponse.fromUsername(true, savedAccount.getUsername());
    }

    public SignInResponse signIn(SignInRequest request) {
        Optional<Account> account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if(account.isEmpty())
            return SignInResponse.fromUsername(false, request.getUsername());

        return SignInResponse.fromUsername(true, request.getUsername());
    }
}
