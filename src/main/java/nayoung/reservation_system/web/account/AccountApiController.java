package nayoung.reservation_system.web.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.account.AccountService;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.account.AccountException;
import nayoung.reservation_system.exception.response.ExceptionResponse;
import nayoung.reservation_system.web.account.model.SignInRequest;
import nayoung.reservation_system.web.account.model.SignInResponse;
import nayoung.reservation_system.web.account.model.SignUpRequest;
import nayoung.reservation_system.web.account.model.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
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