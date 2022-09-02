package nayoung.reservation_system.web.meeting_room;

import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.domain.meeting_room.MeetingRoomService;
import nayoung.reservation_system.web.meeting_room.model.MeetingRoomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting-room")
public class MeetingRoomApiController {

    private final MeetingRoomService meetingRoomService;

    @GetMapping("/{numberOfPeople}")
    public ResponseEntity<?> getMeetingRoom(@PathVariable Long numberOfPeople) {
        MeetingRoomResponse response = meetingRoomService.findByNumberOfPeople(numberOfPeople);
        if(!response.isEligible())
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{numberOfPeople}")
    public ResponseEntity<?> createMeetingRoom(@PathVariable Long numberOfPeople) {
        MeetingRoomResponse response = meetingRoomService.createMeetingRoom(numberOfPeople);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
