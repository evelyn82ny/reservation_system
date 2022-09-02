package nayoung.reservation_system.domain.meeting_room;

import lombok.RequiredArgsConstructor;
import nayoung.reservation_system.web.meeting_room.model.MeetingRoomResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;
    private final MeetingRoomValidator validator;

    public MeetingRoomResponse createMeetingRoom(Long numberOfPeople) {
        MeetingRoom meetingRoom = MeetingRoom.fromNumberOfPeople(numberOfPeople);
        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);
        return MeetingRoomResponse.fromMeetingRoom(savedMeetingRoom);
    }

    public MeetingRoomResponse findByNumberOfPeople(Long numberOfPeople) {
        validator.existByNumberOfPeople(numberOfPeople);

        MeetingRoom meetingRoom = meetingRoomRepository.findByNumberOfPeople(numberOfPeople).get();
        return MeetingRoomResponse.fromMeetingRoom(meetingRoom);
    }
}
