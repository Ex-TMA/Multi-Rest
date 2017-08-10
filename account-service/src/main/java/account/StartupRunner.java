package account;

import account.model.Account;
import account.repository.AccountAccessRepository;
import account.repository.AccountRepository;
import account.repository.AccountTokenRepository;
import config.property.Utils;
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
   /* @Autowired
    private Utils utils;*/

    @Override
    public void run(String... args) throws Exception {

       /* Access access = accountAccessRepository.findOne(1L);

        Account acc3 = new Account("truong3","password", AccountState.ACTIVE,
                "truong 3 with encrypt","truongnguyen1610@gmail.com");
        acc3.addAccess(access);
        accountRepository.save( acc3 );*/

       /* for(Account acc: accountRepository.findAll()){
            System.out.println("Name: " + acc.getName() + ", match: " + utils.match("password",acc.getPass()));
        }*/
    }
}
