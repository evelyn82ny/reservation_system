package nayoung.reservation_system.domain.meeting_room.repository;

import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface MeetingRoomRepositoryCustom {

    Optional<MeetingRoom> findByIdWithoutLock(Long meetingRoomId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MeetingRoom> findByIdWithLock(Long meetingRoomId);
}
