package nayoung.reservation_system.domain.reservation;

import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import nayoung.reservation_system.web.reservation.model.ReservationResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReservationValidator {

    protected ReservationResponse isAvailableMeetingRoom(MeetingRoom meetingRoom) {
        if(meetingRoom.getReservationStatus().equals(ReservationStatus.AVAILABLE))
            return ReservationResponse.ofEligible(meetingRoom.getId());

        return ReservationResponse.ofIneligible(meetingRoom.getId());
    }
}
