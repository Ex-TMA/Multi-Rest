package account.model;

import config.property.Utils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties="flyway.enabled=false")
public class AccountTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private Utils utils;

	@Test
	public void createAccountPasswordShouldBeEncoded() throws Exception {
		Account account = this.entityManager.persistFlushFind(new Account( utils,"testuser","password",AccountState.ACTIVE, "test user", "truong@test.com"));
		assertThat(account.getUserName()).isEqualTo("testuser");
		assertThat(account.getName()).isEqualTo("test user");
		assertThat(account.getState()).isEqualTo(AccountState.ACTIVE);
		assertThat(account.getEmail()).isEqualTo("truong@test.com");
		assertTrue(utils.match("password",account.getPass()));
	}
}