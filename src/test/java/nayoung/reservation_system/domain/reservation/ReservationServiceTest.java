package nayoung.reservation_system.domain.reservation;

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

    final int TOTAL_USERS = 100;
    final String username = "apple";
    final String password = "password";

    /**
     * 모든 계정의 username 필드의 값이 같다는 점을 감안합니다.
     * AccountValidator.duplicateUsername 주석처리 후 테스트합니다.
     */
    @BeforeEach
    void init() {
        for(int i = 0; i < TOTAL_USERS; i++) {
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
        CountDownLatch countDownLatch = new CountDownLatch(TOTAL_USERS);

        List<ReserveWithoutLockWorker> workers = new ArrayList<>();
        for(long accountId = 1L; accountId <= TOTAL_USERS; accountId++) {
            workers.add(new ReserveWithoutLockWorker(accountId, meetingRoomId, countDownLatch));
        }

        workers.forEach(worker -> new Thread(worker).start());
        countDownLatch.await();
    }

   private class ReserveWithoutLockWorker implements Runnable {
        private Long accountId;
        private Long meetingRoomId;
        private CountDownLatch countDownLatch;

        public ReserveWithoutLockWorker(Long accountId, Long meetingRoomId, CountDownLatch countDownLatch) {
            this.accountId = accountId;
            this.meetingRoomId = meetingRoomId;
            this.countDownLatch = countDownLatch;
        }

       @Override
       public void run() {
           reservationService.reserveMeetingRoomWithoutLock(meetingRoomId, accountId);
           countDownLatch.countDown();
       }
   }
}