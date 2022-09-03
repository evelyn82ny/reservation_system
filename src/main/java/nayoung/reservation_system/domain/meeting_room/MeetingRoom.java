package nayoung.reservation_system.domain.meeting_room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nayoung.reservation_system.domain.reservation.ReservationStatus;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long maximumNumberOfPeople;

    private ReservationStatus reservationStatus;

    private Long numberOfReservations;

    private MeetingRoom(Long maximumNumberOfPeople) {
        this.maximumNumberOfPeople = maximumNumberOfPeople;
        this.reservationStatus = ReservationStatus.AVAILABLE;
    }

    public static MeetingRoom fromNumberOfPeople(Long numberOfPeople) {
        return new MeetingRoom(numberOfPeople);
    }

    public void updateReservationStatus(ReservationStatus status) {
        this.reservationStatus = status;
    }

    public boolean isFull() {
        return Objects.equals(numberOfReservations, maximumNumberOfPeople);
    }

    public void addNumberOfReservations() {
        this.numberOfReservations += 1;
    }
}
