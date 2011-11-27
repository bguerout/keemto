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

public class AccountRepositoryTest {

    private AccountRepository accountRepository;

    private User user = new User("test");

    @Before
    public void setUp() throws Exception {

        AccountFactory factory = mock(AccountFactory.class);
        accountRepository = new AccountRepository(Lists.newArrayList(factory));
    }

    @Test
    public void shouldObtainAccountFromAllFactories() throws Exception {

        AccountFactory factory1 = mock(AccountFactory.class);
        AccountFactory factory2 = mock(AccountFactory.class);
        AccountRepository accountRepository = new AccountRepository(Lists.newArrayList(factory1, factory2));

        accountRepository.findAccounts(user);

        verify(factory1).getAccounts(user);
        verify(factory2).getAccounts(user);

    }

    @Test
    public void canAddFactory() throws Exception {

        AccountFactory factory3 = mock(AccountFactory.class);

        accountRepository.addFactory(factory3);

        accountRepository.findAccounts(user);
        verify(factory3).getAccounts(user);
    }

    @Test
    public void shouldObtainAnEmtpyListWhenUserHasNoAccount() throws Exception {

        AccountRepository accountRepository = new AccountRepository(new ArrayList<AccountFactory>());

        List<Account> accounts = accountRepository.findAccounts(user);

        assertThat(accounts.isEmpty(), is(true));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenKeyIsInvalid() throws Exception {

        AccountKey invalidKey = new AccountKey("provider", "userId", user);

        new AccountRepository(new ArrayList<AccountFactory>()).findAccount(invalidKey);


    }

    @Test
    public void shouldObtainAccountForKey() throws Exception {

        Account account = mock(Account.class);
        AccountKey key = new AccountKey("provider", "userId", user);
        AccountFactory factory1 = mock(AccountFactory.class);
        AccountRepository accountRepository = new AccountRepository(Lists.newArrayList(factory1));
        when(factory1.supports("provider")).thenReturn(true);
        when(factory1.getAccount(key)).thenReturn(account);

        Account result = accountRepository.findAccount(key);


        assertThat(result, equalTo(account));
        verify(factory1).getAccount(key);
    }


    @Test
    public void shouldRevokeAccountForKey() throws Exception {

        AccountKey key = new AccountKey("provider", "userId", user);
        AccountFactory factory1 = mock(AccountFactory.class);
        AccountRepository accountRepository = new AccountRepository(Lists.newArrayList(factory1));
        when(factory1.supports("provider")).thenReturn(true);

        accountRepository.revoke(key);

        verify(factory1).revoke(key);
    }

}
