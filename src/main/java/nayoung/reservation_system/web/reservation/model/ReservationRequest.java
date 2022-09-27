package nayoung.reservation_system.web.reservation.model;

import lombok.Getter;

@Getter
public class ReservationRequest {

    private Long accountId;

    private Long meetingRoomId;
}
