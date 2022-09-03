package nayoung.reservation_system.domain.reservation;

import nayoung.reservation_system.domain.meeting_room.MeetingRoom;
import nayoung.reservation_system.domain.meeting_room.MeetingRoomService;
import nayoung.reservation_system.domain.meeting_room.repository.MeetingRoomRepository;
import nayoung.reservation_system.domain.account.AccountService;
import nayoung.reservation_system.web.account.model.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
class ReservationServiceTest {

    @Autowired ReservationService reservationService;
    @Autowired MeetingRoomService meetingRoomService;
    @Autowired MeetingRoomRepository meetingRoomRepository;
    @Autowired AccountService accountService;

    List<String> usernames = List.of(new String[]{"apple", "kiwi", "banana", "kaya"});
    final String password = "1234";

    @BeforeEach
    void init() {
        for(String username : usernames) {
            SignUpRequest request = new SignUpRequest();
            request.setUsername(username);
            request.setPassword(password);
            accountService.signUp(request);
        }
        meetingRoomService.createMeetingRoom(1L);
        meetingRoomService.createMeetingRoom(2L);
    }

    @Test
    @DisplayName("데드락 발생하는 테스트")
    void reserveWithoutLock() throws InterruptedException {
        final Long meetingRoomId = 2L;
        CountDownLatch countDownLatch = new CountDownLatch(usernames.size());

        List<Thread> workers = new ArrayList<>();
        for(String username : usernames) {
            workers.add(new Thread(
                    new ReserveWithoutLockWorker(username, meetingRoomId, countDownLatch)
            ));
        }
        workers.forEach(Thread::start);
        countDownLatch.await();

        MeetingRoom meetingRoom = meetingRoomRepository.findByIdWithoutLock(meetingRoomId).get();
        System.out.println(meetingRoom.getReservationStatus());
        System.out.println(meetingRoom.getNumberOfReservations());
    }

   private class ReserveWithoutLockWorker implements Runnable {
        private String username;
        private Long meetingRoomId;
        private CountDownLatch countDownLatch;

        public ReserveWithoutLockWorker(String username, Long meetingRoomId, CountDownLatch countDownLatch) {
            this.username = username;
            this.meetingRoomId = meetingRoomId;
            this.countDownLatch = countDownLatch;
        }

       @Override
       public void run() {
           reservationService.reserveMeetingRoomWithoutLock(meetingRoomId, username);
           countDownLatch.countDown();
       }
   }
}