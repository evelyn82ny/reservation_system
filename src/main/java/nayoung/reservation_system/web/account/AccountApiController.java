package nayoung.reservation_system.web.account;

import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.domain.account.AccountService;
import nayoung.reservation_system.web.account.model.SignInRequest;
import nayoung.reservation_system.web.account.model.SignInResponse;
import nayoung.reservation_system.web.account.model.SignUpRequest;
import nayoung.reservation_system.web.account.model.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse response = accountService.signUp(request);
        if(response.isEligible())
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        SignInResponse response = accountService.signIn(request);
        if(response.isEligible())
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}