package nayoung.reservation_system.web.meeting_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.meeting_room.MeetingRoomService;
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

    @PostMapping("/{numberOfPeople}")
    public ResponseEntity<?> createMeetingRoom(@PathVariable Long numberOfPeople) {
        MeetingRoomResponse response = meetingRoomService.createMeetingRoom(numberOfPeople);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
