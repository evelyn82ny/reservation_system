package nayoung.reservation_system.web.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.account.AccountService;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.account.AccountException;
import nayoung.reservation_system.exception.response.ExceptionResponse;
import nayoung.reservation_system.web.account.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Slf4j
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = accountService.signUp(request);
         return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        SignInResponse response = accountService.signIn(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/my-page")
    public ResponseEntity<?> myPage(@RequestParam Long accountId, @RequestParam(required = false) String username) {
        MyPageResponse response = accountService.getMyPage(accountId, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam Long accountId, @RequestBody AccountDetailRequest request) {
        AccountResponse response = accountService.update(accountId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<?> handleAccount(AccountException e) {
        log.warn("[UserApiController] " + e.getExceptionCode().getMessage());
        return ResponseEntity.status(e.getExceptionCode().getHttpStatus())
                .body(createExceptionResponse(e.getExceptionCode()));
    }

    private ExceptionResponse createExceptionResponse(ExceptionCode exceptionCode) {
        return ExceptionResponse.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .build();
    }
}