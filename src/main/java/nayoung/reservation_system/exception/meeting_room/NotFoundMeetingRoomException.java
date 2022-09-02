package nayoung.reservation_system.exception.meeting_room;

import lombok.Getter;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.global.NotFoundException;

@Getter
public class NotFoundMeetingRoomException extends NotFoundException {

    public NotFoundMeetingRoomException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}