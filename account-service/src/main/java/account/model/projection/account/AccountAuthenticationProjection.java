package account.model.projection.account;

import account.model.Account;
import org.springframework.data.rest.core.config.Projection;

/**
 * Created by nsonanh on 29/07/2017.
 */
@Projection(name = "authenticateAccount", types = Account.class)
public interface AccountAuthenticationProjection {
    String getUserName();
    String getPass();
    String getSecret();
}
