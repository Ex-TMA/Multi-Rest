package security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import security.model.AccountToken;

import java.util.Date;

/**
 * Created by truongnguyen on 7/18/17.
 */
@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long>{
    AccountToken findByTokenAndIpAndExpiresAtAfter(@Param("token") String token, @Param("ip") String ip, @Param("now") Date now);
}
