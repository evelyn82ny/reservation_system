package nayoung.reservation_system.domain.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.account.repository.AccountRepository;
import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import nayoung.reservation_system.domain.account.Account;
import nayoung.reservation_system.domain.meeting_room.repository.MeetingRoomRepository;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.account.NotFoundAccountException;
import nayoung.reservation_system.exception.meeting_room.NotFoundMeetingRoomException;
import nayoung.reservation_system.web.reservation.model.ReservationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    protected ReservationResponse reserveMeetingRoom(MeetingRoom meetingRoom, Account account) {

        ReservationResponse response;
        if(Objects.equals(meetingRoom.getReservationStatus(), ReservationStatus.AVAILABLE)) {
            response = ReservationResponse.ofEligible(meetingRoom.getId());
        }
        else {
            response = ReservationResponse.ofIneligible(meetingRoom.getId());
        }

        response.setAccountId(account.getId());
        if(!response.isEligible())
            return response;

        Reservation reservation = Reservation.fromAccountAndMeetingRoom(account, meetingRoom);
        reservationRepository.save(reservation);
        response.setReservationId(reservation.getId());

        meetingRoom.addNumberOfReservations();
        if(meetingRoom.isFull()) {
            meetingRoom.updateReservationStatus(ReservationStatus.FULL);
        }
        return response;
    }

    public ReservationResponse reserveMeetingRoomWithoutLock(Long meetingRoomId, Long accountId) {
        MeetingRoom meetingRoom = meetingRoomRepository.findByIdWithoutLock(meetingRoomId)
                .orElseThrow(() -> new NotFoundMeetingRoomException(ExceptionCode.NOT_FOUND_MEETING_ROOM));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT));

        return reserveMeetingRoom(meetingRoom, account);
    }

    public ReservationResponse reserveMeetingRoomWithLock(Long meetingRoomId, Long accountId) {
        MeetingRoom meetingRoom = meetingRoomRepository.findByIdWithLock(meetingRoomId)
                .orElseThrow(() -> new NotFoundMeetingRoomException(ExceptionCode.NOT_FOUND_MEETING_ROOM));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ExceptionCode.NOT_FOUND_ACCOUNT));

        return reserveMeetingRoom(meetingRoom, account);
    }
}
