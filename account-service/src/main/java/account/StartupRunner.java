package account;

import account.model.Account;
import account.repository.AccountRepository;
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
    private Utils utils;

    @Override
    public void run(String... args) throws Exception {

        for(Account acc: accountRepository.findAll()){
            System.out.println("Name: " + acc.getName() + ", match: " + utils.match("password",acc.getPass()));
        }
    }
}
