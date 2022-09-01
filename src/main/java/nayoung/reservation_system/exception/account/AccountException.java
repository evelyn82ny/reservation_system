package nayoung.reservation_system.exception.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.exception.ExceptionCode;

@Getter
@RequiredArgsConstructor
public class AccountException extends RuntimeException {

    private final ExceptionCode exceptionCode;
}