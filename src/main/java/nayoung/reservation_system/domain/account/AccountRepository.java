package nayoung.reservation_system.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUsername(String username);

    Optional<Account> findByUsernameAndPassword(String username, String password);
}
