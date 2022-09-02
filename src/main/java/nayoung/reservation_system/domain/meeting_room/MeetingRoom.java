package nayoung.reservation_system.domain.meeting_room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nayoung.reservation_system.domain.reservation.ReservationStatus;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numberOfPeople;

    private ReservationStatus reservationStatus;

    private MeetingRoom(Long numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
        this.reservationStatus = ReservationStatus.AVAILABLE;
    }

    public static MeetingRoom fromNumberOfPeople(Long numberOfPeople) {
        return new MeetingRoom(numberOfPeople);
    }

    public void updateReservationStatus(ReservationStatus status) {
        this.reservationStatus = status;
    }
}
