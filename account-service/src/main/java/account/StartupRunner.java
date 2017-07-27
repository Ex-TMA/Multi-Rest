package account;

import account.model.Access;
import account.model.Account;
import account.model.AccountState;
import account.repository.AccountAccessRepository;
import account.repository.AccountRepository;
import account.repository.AccountTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * Created by truongnguyen on 7/18/17.
 */
public class StartupRunner implements CommandLineRunner {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountAccessRepository accountAccessRepository;
    @Autowired
    private AccountTokenRepository accountToken;

    @Override
    public void run(String... args) throws Exception {

       /* Access access = accountAccessRepository.findOne(1L);

        Account acc3 = new Account("truong3","password", AccountState.ACTIVE,
                "truong 3 with encrypt","truongnguyen1610@gmail.com");
        acc3.addAccess(access);
        accountRepository.save( acc3 );*/

        for(Account acc: accountRepository.findAll()){
            System.out.println("Name: " + acc.getName() + ", match: " + Utils.match("password",acc.getPass()));
        }
    }
}
