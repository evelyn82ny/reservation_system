package nayoung.reservation_system.exception.account;

import lombok.Getter;
import nayoung.reservation_system.exception.ExceptionCode;

@Getter
public class DuplicateUsernameException extends AccountException {

    public DuplicateUsernameException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
