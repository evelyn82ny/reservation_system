package nayoung.reservation_system.domain.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.account.AccountRepository;
import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import nayoung.reservation_system.domain.account.Account;
import nayoung.reservation_system.domain.meeting_room.repository.MeetingRoomRepository;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.global.NotFoundException;
import nayoung.reservation_system.web.reservation.model.ReservationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final ReservationValidator validator;

    protected ReservationResponse reserveMeetingRoom(MeetingRoom meetingRoom, Account account) {
        ReservationResponse response = validator.isAvailableMeetingRoom(meetingRoom);
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


    @Transactional
    public ReservationResponse reserveMeetingRoomWithoutLock(Long meetingRoomId, String username) {
        MeetingRoom meetingRoom = meetingRoomRepository.findByIdWithoutLock(meetingRoomId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEETING_ROOM));

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_ACCOUNT));

        return reserveMeetingRoom(meetingRoom, account);
    }
}
