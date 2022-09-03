package nayoung.reservation_system.domain.meeting_room.repository;

import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long>, MeetingRoomRepositoryCustom {

    boolean existsByMaximumNumberOfPeople(Long maximumNumberOfPeople);
}
