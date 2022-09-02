package nayoung.reservation_system.exception;

import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.exception.global.NotFoundException;
import nayoung.reservation_system.exception.meeting_room.NotFoundMeetingRoomException;
import nayoung.reservation_system.exception.response.ExceptionResponse;
import nayoung.reservation_system.exception.account.NotFoundAccountException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundAccountException.class, NotFoundMeetingRoomException.class})
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return handleExceptionInternal(e.getExceptionCode());
    }

    private ResponseEntity<Object> handleExceptionInternal(ExceptionCode exceptionCode) {
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(createExceptionResponse(exceptionCode));
    }

    private ExceptionResponse createExceptionResponse(ExceptionCode exceptionCode) {
        return ExceptionResponse.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .build();
    }
}
