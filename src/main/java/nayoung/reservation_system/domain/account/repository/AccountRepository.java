package nayoung.reservation_system.domain.account.repository;

import nayoung.reservation_system.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUsername(String username);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Account> findById(Long id);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUsernameAndPassword(String username, String password);
}
