package nayoung.reservation_system.domain.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.account.repository.AccountRepository;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.account.AccountException;
import nayoung.reservation_system.exception.account.DuplicateUsernameException;
import nayoung.reservation_system.exception.account.NotFoundAccountException;
import nayoung.reservation_system.web.account.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public SignUpResponse signUp(SignUpRequest request) {
        if(accountRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException(ExceptionCode.DUPLICATE_USERNAME);
        }

        Account account = Account.fromSignUpRequest(request);
        Account savedAccount = accountRepository.save(account);
        return SignUpResponse.fromUsername(true, savedAccount.getUsername());
    }

    public SignInResponse signIn(SignInRequest request) {
        if(!accountRepository.existsByUsername(request.getUsername())) {
            throw new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT);
        }

        Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new AccountException(ExceptionCode.PASSWORD_MISMATCH));

        return SignInResponse.fromUsername(true, account.getUsername());
    }

    private AccountResponse getAccountInfo(Long accountId) {
        Account account = accountRepository.findById(accountId)
                        .orElseThrow(() -> new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT));

        return AccountResponse.of(account);
    }

    private MyPageResponse getMyPageInfo(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT));

        return MyPageResponse.of(account);
    }

    public MyPageResponse getMyPage(Long accountId, String username) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT));

        if(StringUtils.hasText(username) && !Objects.equals(account.getUsername(), username)) {
            return getMyPageInfo(username);
        }
        return AccountResponse.of(account);
    }

    public AccountResponse update(Long accountId, AccountDetailRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT));

        if(!Objects.equals(account.getUsername(), request.getUsername())) {
            if(accountRepository.existsByUsername(request.getUsername())) {
                throw new DuplicateUsernameException(ExceptionCode.DUPLICATE_USERNAME);
            }
        }

        account.update(request);
        accountRepository.save(account);

        return AccountResponse.of(account);
    }
}
