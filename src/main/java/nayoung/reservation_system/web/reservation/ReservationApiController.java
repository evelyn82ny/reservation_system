package nayoung.reservation_system.web.reservation;

import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.domain.account.AccountValidator;
import nayoung.reservation_system.domain.reservation.ReservationService;
import nayoung.reservation_system.web.reservation.model.ReservationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationApiController {

    private final ReservationService reservationService;
    private final AccountValidator accountValidator;

    @PostMapping("/{meetingRoomId}/{username}")
    public ResponseEntity<?> reserveMeetingRoom(@PathVariable Long meetingRoomId, @PathVariable String username) {
        Long accountId = accountValidator.existByUsername(username);

        ReservationResponse response = reservationService.reserveMeetingRoomWithoutLock(meetingRoomId, accountId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
