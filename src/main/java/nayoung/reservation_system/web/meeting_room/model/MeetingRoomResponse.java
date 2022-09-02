package nayoung.reservation_system.web.meeting_room.model;

import lombok.Getter;
import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import nayoung.reservation_system.domain.reservation.ReservationStatus;

@Getter
public class MeetingRoomResponse {

    private boolean eligible;
    private Long id;
    private Long numberOfPeople;
    private ReservationStatus reservationStatus;

    private MeetingRoomResponse(boolean eligible, Long id, Long numberOfPeople, ReservationStatus reservationStatus) {
        this.eligible = eligible;
        this.id = id;
        this.numberOfPeople = numberOfPeople;
        this.reservationStatus = reservationStatus;
    }

    public static MeetingRoomResponse fromMeetingRoom(MeetingRoom meetingRoom) {
        return new MeetingRoomResponse(true, meetingRoom.getId(), meetingRoom.getNumberOfPeople(), meetingRoom.getReservationStatus());
    }

    public static MeetingRoomResponse fromIneligible() {
        return new MeetingRoomResponse(false, null, null, ReservationStatus.UNAVAILABLE);
    }
}
