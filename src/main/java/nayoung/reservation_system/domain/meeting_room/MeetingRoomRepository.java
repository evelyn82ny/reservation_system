package nayoung.reservation_system.domain.meeting_room;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

    Optional<MeetingRoom> findByNumberOfPeople(Long numberOfPeople);

    boolean existsByNumberOfPeople(Long numberOfPeople);
}
