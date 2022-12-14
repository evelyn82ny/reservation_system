package nayoung.reservation_system.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    NOT_FOUND_ACCOUNT(HttpStatus.BAD_REQUEST, "해당 계정은 존재하지 않습니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이미 사용중인 username 입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_SUFFICIENT_BALANCE_EXCEPTION(HttpStatus.BAD_REQUEST, "잔고가 부족합니다."),

    NOT_FOUND_MEETING_ROOM(HttpStatus.BAD_REQUEST, "해당 Meeting Room은 존재하지 않습니다."),

    VERSION_CONFLICT(HttpStatus.CONFLICT, "다시 시도해주세요");

    private final HttpStatus httpStatus;
    private final String message;
}
