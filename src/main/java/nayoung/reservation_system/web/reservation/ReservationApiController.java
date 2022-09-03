package nayoung.reservation_system.web.reservation;

import lombok.RequiredArgsConstructor;
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

    @PostMapping("/{meetingRoomId}/{username}")
    public ResponseEntity<?> reserveMeetingRoom(@PathVariable Long meetingRoomId, @PathVariable String username) {
        ReservationResponse response = reservationService.reserveMeetingRoomWithoutLock(meetingRoomId, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
