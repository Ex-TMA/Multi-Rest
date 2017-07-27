package account.repository;

import account.model.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by truongnguyen on 7/18/17.
 */
@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {
}
