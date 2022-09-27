package nayoung.reservation_system.exception.meeting_room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.exception.ExceptionCode;

@Getter
@RequiredArgsConstructor
public class NotFoundMeetingRoomException extends RuntimeException {

    private final ExceptionCode exceptionCode;
}