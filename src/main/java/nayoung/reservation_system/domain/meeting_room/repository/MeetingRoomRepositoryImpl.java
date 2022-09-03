package nayoung.reservation_system.domain.meeting_room.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nayoung.reservation_system.domain.meeting_room.MeetingRoom;

import javax.persistence.LockModeType;
import java.util.Optional;

import static nayoung.reservation_system.domain.reservation.QReservation.reservation;
import static nayoung.reservation_system.domain.account.QAccount.account;
import static nayoung.reservation_system.domain.meeting_room.QMeetingRoom.meetingRoom;

@RequiredArgsConstructor
@Slf4j
public class MeetingRoomRepositoryImpl implements MeetingRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MeetingRoom> findByIdWithoutLock(Long meetingRoomId) {
        return Optional.ofNullable(queryFactory.selectFrom(meetingRoom)
                .where(meetingRoom.id.eq(meetingRoomId))
                .fetchOne());
    }
}
