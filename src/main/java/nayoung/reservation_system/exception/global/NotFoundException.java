package nayoung.reservation_system.exception.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.exception.ExceptionCode;
import javax.persistence.EntityNotFoundException;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends EntityNotFoundException {

    private final ExceptionCode exceptionCode;
}
