package nayoung.reservation_system.web.meeting_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.meeting_room.MeetingRoomService;
import nayoung.reservation_system.exception.ExceptionCode;
import nayoung.reservation_system.exception.meeting_room.NotFoundMeetingRoomException;
import nayoung.reservation_system.exception.response.ExceptionResponse;
import nayoung.reservation_system.web.meeting_room.model.MeetingRoomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting-room")
@Slf4j
public class MeetingRoomApiController {

    private final MeetingRoomService meetingRoomService;

    @GetMapping("/{numberOfPeople}")
    public ResponseEntity<?> getMeetingRoom(@PathVariable Long numberOfPeople) {
        MeetingRoomResponse response = meetingRoomService.findByNumberOfPeople(numberOfPeople);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{numberOfPeople}")
    public ResponseEntity<?> createMeetingRoom(@PathVariable Long numberOfPeople) {
        MeetingRoomResponse response = meetingRoomService.createMeetingRoom(numberOfPeople);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundMeetingRoomException.class)
    public ResponseEntity<?> handleMeetingRoom(NotFoundMeetingRoomException e) {
        log.warn("[MeetingRoomService] " + e.getExceptionCode().getMessage());
        return ResponseEntity.status(e.getExceptionCode().getHttpStatus())
                .body(createExceptionResponse(e.getExceptionCode()));
    }

    private ExceptionResponse createExceptionResponse(ExceptionCode exceptionCode) {
        return ExceptionResponse.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .build();
    }
}
