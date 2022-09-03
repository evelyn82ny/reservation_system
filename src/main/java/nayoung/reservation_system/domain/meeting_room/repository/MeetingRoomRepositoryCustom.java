package nayoung.reservation_system.domain.meeting_room.repository;

import nayoung.reservation_system.domain.meeting_room.MeetingRoom;

import java.util.Optional;

public interface MeetingRoomRepositoryCustom {

    Optional<MeetingRoom> findByIdWithoutLock(Long meetingRoomId);
}
