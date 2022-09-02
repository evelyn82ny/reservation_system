package nayoung.reservation_system.domain.meeting_room;

import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.web.meeting_room.model.MeetingRoomResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;

    public MeetingRoomResponse createMeetingRoom(Long numberOfPeople) {
        MeetingRoom meetingRoom = MeetingRoom.fromNumberOfPeople(numberOfPeople);
        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);
        return MeetingRoomResponse.fromMeetingRoom(savedMeetingRoom);
    }
}
