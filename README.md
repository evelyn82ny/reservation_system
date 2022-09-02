# 예약 시스템

Lock과 Transaction Isolation level을 파악하기 위한 프로젝트로 여러 상황에 대해 Optimistic(낙관적) 또는 Pessimistic(비관적) Lock을 적용한다.

미팅 룸은 인원 제한이 있으며 **인원 제한보다 많은 요청이 발생하면** 제한된 인원만 처리하고 나머지 요청은 실패 처리한다.

<br>

# Optimistic Lock (낙관적 락)

- 충돌이 잘 일어나지 않을 것이라 예상되는 상황에 사용
- Transaction 없는 상황에서 사용
- Rollback을 개발자가 처리

## 적용 가능한 상황

- 계정 정보 수정: 사용자 계정은 사용자만 수정하기 때문에 동시 요청 가능성이 현저히 낮다고 예상

<br>

# Pessimistic Lock (비관적 락)

- 충돌이 많을 것이라 예상되는 상황에 사용
- Transaction에서 사용
- Rollback이 transaction 단위로 알아서 처리됨

## 적용 가능한 상황

- 인원 제한이 있는 미팅 룸을 여러 사용자가 예약 시도하는 경우
- 예약 성공 후 사용자 잔고 차감: transaction이 필요