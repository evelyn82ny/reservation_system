package nayoung.reservation_system.domain.meeting_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.meeting_room.repository.MeetingRoomRepository;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.meeting_room.NotFoundMeetingRoomException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeetingRoomValidator {

    private final MeetingRoomRepository meetingRoomRepository;

    public void existByNumberOfPeople(Long maximumNumberOfPeople) {
        if(!meetingRoomRepository.existsByMaximumNumberOfPeople(maximumNumberOfPeople)) {
            log.warn("[MeetingRoomValidator] " + maximumNumberOfPeople + "인원인 Meeting Room은 존재하지 않음");
            throw new NotFoundMeetingRoomException(ExceptionCode.NOT_FOUND_MEETING_ROOM);
        }
    }
}
