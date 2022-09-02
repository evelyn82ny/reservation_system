package nayoung.reservation_system.exception.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.exception.ExceptionCode;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends IllegalArgumentException {

    private final ExceptionCode exceptionCode;
}
