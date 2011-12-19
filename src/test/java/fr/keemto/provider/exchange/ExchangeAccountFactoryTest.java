package fr.keemto.provider.exchange;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class ExchangeAccountFactoryTest {

    private ExchangeAccountRepository factory;

    @Before
    public void setUp() throws Exception {
        MailRepository mailRepository = mock(MailRepository.class);
        factory = new ExchangeAccountRepository(mailRepository, mock(List.class));
    }

    @Test
    public void shouldCreateAnAccountUsingUserEmail() throws Exception {

        User user = new User("stnevex", "Ben", "G", "stnevex@gmail.com");

        List<Account> accounts = factory.getAccounts(user);

        assertThat(accounts.size(), is(1));
        Account account = accounts.get(0);
        AccountKey key = account.getKey();
        assertThat(key.getProviderUserId(), equalTo("stnevex@gmail.com"));
        assertThat(key.getUser(), equalTo(user));
    }


    @Test
    public void shouldCreateAccountFromAccountKey() throws Exception {

        User user = new User("stnevex", "Ben", "G", "stnevex@gmail.com");
        AccountKey key = new AccountKey("exchange", user.getEmail(), user);

        Account account = factory.getAccount(key);

        assertThat(account, notNullValue());
        assertThat(account.getKey(), equalTo(key));
    }


    @Test
    public void shouldSupportExchangeProvider() throws Exception {
        assertThat(factory.supports("test"), is(false));
        assertThat(factory.supports("exchange"), is(true));
        assertThat(factory.supports("exchange-corporate"), is(true));
    }


}
