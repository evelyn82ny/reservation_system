package nayoung.reservation_system.exception.account;

import lombok.Getter;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.global.NotFoundException;

@Getter
public class NotFoundAccountException extends NotFoundException {

    public NotFoundAccountException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
