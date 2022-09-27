# 예약 시스템

- Lock과 Transaction Isolation level을 파악하기 위한 프로젝트로 여러 상황에 대해 Optimistic(낙관적) 또는 Pessimistic(비관적) Lock을 적용
- 미팅 룸은 인원 제한이 있으며 **제한된 인원보다 많은 요청이 발생하면** 제한된 인원 수만큼의 요청만 처리하고 나머지 요청은 실패 처리

### Optimistic Lock (낙관적 락)

- 충돌이 잘 일어나지 않을 것이라 예상되는 상황에 사용
- Transaction 없는 상황에서 사용
- Rollback을 개발자가 처리
- 적용 가능한 상황
    - 계정 정보 수정: 사용자 계정은 사용자만 수정하기 때문에 동시 요청 가능성이 현저히 낮다고 예상

### Pessimistic Lock (비관적 락)

- 충돌이 많을 것이라 예상되는 상황에 사용
- Transaction에서 사용
- Rollback이 transaction 단위로 알아서 처리됨
- 적용 가능한 상황
    - 인원 제한이 있는 미팅 룸을 여러 사용자가 예약 시도하는 경우
    - 예약 성공 후 사용자 잔고 차감: transaction이 필요

<br>

# Perssimistic Lock 적용

- 멀티 스레드 환경에서 테스트함

## 예약 생성 기능

- 더 자세한 내용은 https://velog.io/@evelyn82ny/when-to-use-a-pessimistic-lock-feat-2PL 에서 확인 가능

```java
protected ReservationResponse reserveMeetingRoom(MeetingRoom meetingRoom, Account account) {

    // 예약 가능한 미팅룸인지 확인
	ReservationResponse response = validator.isAvailableMeetingRoom(meetingRoom);
	response.setAccountId(account.getId());
    
    // 예약할 수 있는 자격이 아니라면 return
	if(!response.isEligible()) {
		return response;
	}
    
    // 예약할 수 있으므로 Account, MeetingRoom을 참조하는 Reservation 생성
	Reservation reservation = Reservation.fromAccountAndMeetingRoom(account, meetingRoom);
	reservationRepository.save(reservation);
	response.setReservationId(reservation.getId());

	// 예약 수를 증가시킴 (numberOfReservations: Long type)
	meetingRoom.addNumberOfReservations();
    // 미팅 룸의 예약이 가득찼다면 상태를 변경
	if(meetingRoom.isFull()) {
		meetingRoom.updateReservationStatus(ReservationStatus.FULL);
	}
    
    // Transaction을 사용하지 않기 때문에 save method 호출
    meetingRoomRepository.save(meetingRoom);
	return response;
}
```

- 예약 시도 중인 Meeting Room에 대해 예약이 가능하다면 Reservation(예약) 생성
- 예약 생성 후 해당 Meeting Room의 예약이 가득찼다면(인원 마감) **FULL** 상태로 변경

### Attempt1: Lock X, Transaction X

- Lock과 Transaction을 사용하지 않고 멀티 스레드 환경에서 테스트
- 2명만 예약할 수 있는 Meeting Room에 100개의 예약 요청
- 100개의 예약 요청이 **모두 성공으로 처리**되며 Meeting Room의 상태는 여전히 AVAILABLE 상태

<br>

### Attempt2: Lock X, Transaction O

- ```Service.reserveMeetingRoom()```에 **@Transactional** 추가
- 추가한 이유: 여러 상황에 대해 테스트헤보기 위해
- 결과: DeadLock 발생
<br>

- Lock을 걸지 않았음에도 불구하고 DeadLock 발생한 이유는 **FK를 가지고 있는 테이블에 대한 작업 수행 시 데이터베이스가 자동으로 Lock 설정**
- Reservation 객체는 MeetingRoom 객체를 참조 (MeetingRoom의 FK를 Reservation이 가지고 있음)
- FK가 있는 테이블에서 Insert, Update, Delete 하기 위해 FK 대상에 S-Lock(Shard Lock) 설정
- Update query에 사용되는 모든 레코드에 X-Lock(Exclusive Lock) 설정
<br>

- 즉, 100개의 작업이 Reservation 생성 작업에서 MeetingRoom 레코드에 S-Lock 설정
- 그 후 MeetingRoom의 상태를 변경하려고 하지만 **다른 트랜잭션에서 같은 MeetingRoom 레코드에 S-Lock을 걸어둔 상태**이므로 X-Lock을 걸지못하고 무한 대기하는 DeadLock 발생

<br>

### Attempt3: Lock O, Transaction O

```java
public Optional<MeetingRoom> findByIdWithLock(Long meetingRoomId) {
	return Optional.ofNullable(queryFactory.selectFrom(meetingRoom)
                .where(meetingRoom.id.eq(meetingRoomId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
}
```

- MeetingRoom을 가져오는 메소드에 **PESSIMISTIC_WRITE** 설정 
- MeetingRoom 레코드를 가져오는 쿼리 맨 뒤에 **for update** 추가됨
- 즉, 다른 Transactino의 작업이 끝나야만 객체에 접근 가능
- Reservation 생성 전 MeetingRoom 레코드를 먼저 가져오며
- 하나의 요청에 대한 Transaction이 끝나면 새로운 요청이 MeetingRoom 레코드에 접근하기 때문에 제한된 인원만큼의 요청만 처리할 수 있게됨