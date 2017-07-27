package account.repository;

import account.model.Access;
import account.model.Account;
import account.model.AccountState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

/**
 * Created by truongnguyen on 7/18/17.
 */
@RepositoryRestResource(collectionResourceRel = "account", path = "account")
public interface AccountRepository extends JpaRepository<Account,Long> {

    @RestResource
    Account findByUserName(@Param("userName") String userName);

    @RestResource
    Page<Account> findByState(@Param("state") AccountState state, Pageable p);

    @RestResource
    Page<Account> findByName(@Param("name") String name, Pageable p);

    @RestResource
    Account findByEmail(@Param("email") String email);

    @RestResource
    Page<Account> findByAccesses(@Param("accesses") Collection<Access> accesses, Pageable p);
}
