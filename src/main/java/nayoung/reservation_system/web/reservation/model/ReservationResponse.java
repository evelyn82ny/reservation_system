package nayoung.reservation_system.web.reservation.model;

import lombok.Getter;

@Getter
public class ReservationResponse {

    private boolean eligible;
    private Long meetingRoomId;
    private Long accountId;
    private Long reservationId;

    private ReservationResponse(boolean eligible, Long meetingRoomId, Long accountId) {
        this.eligible = eligible;
        this.meetingRoomId = meetingRoomId;
        this.accountId = accountId;
    }

    public static ReservationResponse ofEligible(Long meetingRoomId) {
        return new ReservationResponse(true, meetingRoomId, null);
    }

    public static ReservationResponse ofIneligible(Long meetingRoomId) {
        return new ReservationResponse(false, meetingRoomId, null);
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
