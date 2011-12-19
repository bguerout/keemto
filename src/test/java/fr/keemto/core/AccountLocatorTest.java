package fr.keemto.core;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class AccountLocatorTest {

    private AccountLocator accountLocator;

    private User user = new User("test");

    @Before
    public void setUp() throws Exception {

        AccountRepository repository = mock(AccountRepository.class);
        accountLocator = new AccountLocator(Lists.newArrayList(repository));
    }

    @Test
    public void shouldObtainAccountFromAllFactories() throws Exception {

        AccountRepository repository1 = mock(AccountRepository.class);
        AccountRepository repository2 = mock(AccountRepository.class);
        AccountLocator accountLocator = new AccountLocator(Lists.newArrayList(repository1, repository2));

        accountLocator.findAccounts(user);

        verify(repository1).getAccounts(user);
        verify(repository2).getAccounts(user);

    }

    @Test
    public void canAddFactory() throws Exception {

        AccountRepository repository3 = mock(AccountRepository.class);

        accountLocator.register(repository3);

        accountLocator.findAccounts(user);
        verify(repository3).getAccounts(user);
    }

    @Test
    public void shouldObtainAnEmtpyListWhenUserHasNoAccount() throws Exception {

        AccountLocator accountLocator = new AccountLocator(new ArrayList<AccountRepository>());

        List<Account> accounts = accountLocator.findAccounts(user);

        assertThat(accounts.isEmpty(), is(true));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenKeyIsInvalid() throws Exception {

        AccountKey invalidKey = new AccountKey("provider", "userId", user);

        new AccountLocator(new ArrayList<AccountRepository>()).findAccount(invalidKey);


    }

    @Test
    public void shouldObtainAccountForKey() throws Exception {

        Account account = mock(Account.class);
        AccountKey key = new AccountKey("provider", "userId", user);
        AccountRepository repository1 = mock(AccountRepository.class);
        AccountLocator accountLocator = new AccountLocator(Lists.newArrayList(repository1));
        when(repository1.supports("provider")).thenReturn(true);
        when(repository1.getAccount(key)).thenReturn(account);

        Account result = accountLocator.findAccount(key);


        assertThat(result, equalTo(account));
        verify(repository1).getAccount(key);
    }

}
