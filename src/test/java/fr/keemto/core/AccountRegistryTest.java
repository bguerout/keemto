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

public class AccountRegistryTest {

    private AccountRegistry accountRegistry;

    private User user = new User("test");

    @Before
    public void setUp() throws Exception {

        AccountFactory factory = mock(AccountFactory.class);
        accountRegistry = new AccountRegistry(Lists.newArrayList(factory));
    }

    @Test
    public void shouldObtainAccountFromAllFactories() throws Exception {

        AccountFactory factory1 = mock(AccountFactory.class);
        AccountFactory factory2 = mock(AccountFactory.class);
        AccountRegistry accountRegistry = new AccountRegistry(Lists.newArrayList(factory1, factory2));

        accountRegistry.findAccounts(user);

        verify(factory1).getAccounts(user);
        verify(factory2).getAccounts(user);

    }

    @Test
    public void canAddFactory() throws Exception {

        AccountFactory factory3 = mock(AccountFactory.class);

        accountRegistry.addFactory(factory3);

        accountRegistry.findAccounts(user);
        verify(factory3).getAccounts(user);
    }

    @Test
    public void shouldObtainAnEmtpyListWhenUserHasNoAccount() throws Exception {

        AccountRegistry accountRegistry = new AccountRegistry(new ArrayList<AccountFactory>());

        List<Account> accounts = accountRegistry.findAccounts(user);

        assertThat(accounts.isEmpty(), is(true));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenKeyIsInvalid() throws Exception {

        AccountKey invalidKey = new AccountKey("provider", "userId", user);

        new AccountRegistry(new ArrayList<AccountFactory>()).findAccount(invalidKey);


    }

    @Test
    public void shouldObtainAccountForKey() throws Exception {

        Account account = mock(Account.class);
        AccountKey key = new AccountKey("provider", "userId", user);
        AccountFactory factory1 = mock(AccountFactory.class);
        AccountRegistry accountRegistry = new AccountRegistry(Lists.newArrayList(factory1));
        when(factory1.supports("provider")).thenReturn(true);
        when(factory1.getAccount(key)).thenReturn(account);

        Account result = accountRegistry.findAccount(key);


        assertThat(result, equalTo(account));
        verify(factory1).getAccount(key);
    }

}
