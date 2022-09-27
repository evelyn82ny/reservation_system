package nayoung.reservation_system.web.reservation;

import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.domain.reservation.ReservationService;
import nayoung.reservation_system.web.reservation.model.ReservationRequest;
import nayoung.reservation_system.web.reservation.model.ReservationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationApiController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> reserveMeetingRoom(@RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.reserveMeetingRoomWithoutLock(request.getMeetingRoomId(), request.getAccountId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
