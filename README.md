# 예약 서비스

- Lock과 Transaction Isolation level을 파악하기 위한 프로젝트로 여러 상황에 대해 Optimistic(낙관적) 또는 Pessimistic(비관적) Lock을 적용
- 미팅룸은 제한된 인원만 예약 가능하며 **제한된 인원보다 많은 요청이 발생하면** 제한된 인원 수만큼의 요청만 처리하고 나머지 요청은 실패 처리

### Optimistic Lock (낙관적 락)

- 충돌이 잘 일어나지 않을 것이라 예상되는 상황에 사용
- Transaction 없는 상황에서 사용
- Rollback, 재시도 처리 그리고 재시도 처리에 실패하는 경우를 Application에 직접 구현해야함
- 적용 가능한 상황
    - 계정 정보 수정: 해당 작업은 권한이 있는 사용자만 가능하고, 해당 사용자가 앱과 웹에서 동시에 수정하는 가능성은 작으므로 충돌 감지만 해도 된다고 생각

### Pessimistic Lock (비관적 락)

- 충돌이 많을 것이라 예상되는 상황에 사용
- Transaction에서 사용
- Rollback이 transaction 단위로 알아서 처리됨
- S-Lock(Shared Lock)과 X-Lock(Exclusive Lock)을 사용해 구현
- 적용 가능한 상황
    - 인원 제한이 있는 미팅룸을 여러 사용자가 예약 시도하는 경우
    - 예약 성공 후 사용자 잔고 차감이나 콘서트 자리 예매 후 해당 자리의 상태를 변경하는 것처럼 **여러 데이터가 연관된 경우** 트랜잭션으로 묶고 쓰기 트랜잭션 간 갱신 손실을 막기위해 비관적 락 사용
    - X-Lock(Exclusive Lock) 사용 시 모든 트랜잭션이 직렬화되므로 응답 속도가 매우 늦어지는데 이에 대한 해결책이 필요한 상황

<br>

# Perssimistic Lock 적용

WAS로 사용 중인 Tomcat은 요청마다 스레드가 할당되는 멀티스레드 환경이다. 그러므로 기존 API를 멀티 스레드 환경에서 테스트했다.

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
    
    // Transaction을 사용하지 않는다면 save method 직접 호출
    meetingRoomRepository.save(meetingRoom);
	return response;
}
```

- 예약 시도 중인 Meeting Room에 대해 예약이 가능하다면 Reservation(예약) 생성
- 예약 생성 후 해당 Meeting Room의 numberOfReservations(현재 예약 개수)를 증가시킴
- Meeting Room의 numberOfReservations(현재 예약 개수) 값과 최대 수용가능한 예약의 값이 일치하다면 더이상 예약을 생성할 수 없으므로 **FULL** 상태로 변경

<br>

### Attempt1: Lock X, Transaction X

- Lock과 Transaction을 사용하지 않고 멀티 스레드 환경에서 테스트
- 2명만 예약할 수 있는 Meeting Room에 100개의 예약 요청
- 100개의 예약 요청이 **모두 성공으로 처리**되며 Meeting Room의 상태는 여전히 **AVAILABLE** 상태

<br>

### Attempt2: Lock X, Transaction O

- ```Service.reserveMeetingRoom()```에 **@Transactional** 추가
- 트랜잭션 사용한 결과 DeadLock 발생
<br>

- Lock을 걸지 않았음에도 DeadLock 발생한 이유는 **FK를 가지고 있는 테이블에 대한 작업 수행 시 MySQL InnoDB 엔진이 자동으로 FK로 참조 중인 레코드에 레코드 레벨의 S-Lock 설정**
- 또한 UPDATE query에 사용되는 모든 레코드에 X-Lock(Exclusive lock)이 설정됨
<br>

- Reservation 객체는 Account(사용자 계정)과 MeetingRoom 객체를 FK로 참조하는 구조
- Reservation을 INSERT 하는 과정에서 제약 조건을 확인하기 위해 FK로 참조 중인 Meeting Room 레코드에 S-Lock(Shard Lock) 설정되고, 그 후에 Meeting Room의 numberOfReservation(현재 예약 개수)를 갱신하기 위해 X-Lock(Exclusive Lock)을 획득하기 위해 대기
- 하지만 모든 트랜잭션이 같은 상황이므로 X-Lock(Exclusive Lock)을 획득할 수 없는 Deadlock 상태가 됨
<br>

데드락을 해결할 수 있는 방법은 다음과 같다.
- SaveAndFlush 사용(갱신 손실 발생)
- Lock 사용 (Attempt 3)

<br>

### 갱신 손실 발생

Attempt2에서 Deadlock이 발생한 이유는 S-lock 획득 후 X-Lock을 획득하는 과정이 트랜잭션으로 묶여있었기 때문이다. 그러므로 ```SaveAndFlush()``` 를 사용해 먼저 미팅룸의 현재 예약 개수를 변경한 후 예약을 생성하면 데드락을 막을 수 있다. 하지만 갱신 손실 문제가 발생한다.
<br>

HikariCP에서 Connection 개수를 10개로 설정했다. (10개가 Default) 커넥션을 얻은 첫 10개의 스레드는 같은 버전의 레코들르 본다. 만약 예약이 가능한 상태라면 예약을 생성하고 미팅룸의 현재 예약 개수를 증가시킬 것이다. 즉, 예약 가능한 개수가 1개임에도 10개의 스레드가 10개의 예약을 생성하고, 10개의 스레드가 미팅룸의 현재 예약 개수를 1 증가시키는 쿼리를 10개 발생시켜도 같은 버전의 상태를 같은 조건으로 갱신하는 갱신 손실이 발생한다.<br>

갱신 손실을 막기 위해선 낙관적 락을 사용해 충돌을 감지하거나, 비관적 락을 사용해 아예 직렬화하거나 아니면 더 좋은 방법이 필요하다. 아래 Attempt3는 비관적 락을 사용한 결과이다.

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

- MeetingRoom을 가져오는 메소드에 **PESSIMISTIC_WRITE** 설정하면 **FOR UDPATE**로 접근
- 레코드에 X-Lock(Exclusive Lock)을 획득하면 다른 세션에서 S-Lock(Shared lock) 이나 X-Lock(Exclusive Lock) 을 획득할 수 없어 차례대로 처리됨 (X-lock을 획득한 세션이 끝나야 다른 세션에서 접근 가능)
- Reservation 생성 전 MeetingRoom 레코드에 대한 X-Lock(Exclusive Lock)을 획득하므로 다른 세션은 해당 세션이 끝날 때까지 대기하므로 제한된 인원만큼만 예약이 생성됨
- 하지만 모든 트랜잭션이 직렬화되므로 응답 속도가 느려지는데 이를 해결하는 단계에 있음