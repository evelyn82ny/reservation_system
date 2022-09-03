package nayoung.reservation_system.domain.reservation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import nayoung.reservation_system.domain.account.Account;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "meeting_room_id")
    private MeetingRoom meetingRoom;

    private Reservation(Account account, MeetingRoom meetingRoom) {
        this.account = account;
        this.meetingRoom = meetingRoom;
    }

    public static Reservation fromAccountAndMeetingRoom(Account account, MeetingRoom meetingRoom) {
        return new Reservation(account, meetingRoom);
    }
}
