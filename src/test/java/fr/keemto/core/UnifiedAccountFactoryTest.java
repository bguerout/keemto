package fr.keemto.core;

import com.google.common.collect.Lists;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class UnifiedAccountFactoryTest {

    private UnifiedAccountFactory unifiedFactory;

    @Before
    public void setUp() throws Exception {

        AccountFactory factory = mock(AccountFactory.class);
        unifiedFactory = new UnifiedAccountFactory(Lists.newArrayList(factory));

    }

    @Test
    public void shouldObtainAccountFromAllFactories() throws Exception {

        User user = new User("test");
        AccountFactory factory1 = mock(AccountFactory.class);
        AccountFactory factory2 = mock(AccountFactory.class);
        UnifiedAccountFactory unifiedFactory = new UnifiedAccountFactory(Lists.newArrayList(factory1, factory2));

        List<Account> accounts = unifiedFactory.getAccounts(user);

        verify(factory1).getAccounts(user);
        verify(factory2).getAccounts(user);

    }

    @Test
    public void shouldObtainAnEmtpyListWhenUserHasNoAccount() throws Exception {

        User user = new User("test");

        UnifiedAccountFactory unifiedFactory = new UnifiedAccountFactory(new ArrayList<AccountFactory>());

        List<Account> accounts = unifiedFactory.getAccounts(user);

        assertThat(accounts.isEmpty(), is(true));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenKeyIsInvalid() throws Exception {

        AccountKey invalidKey = new AccountKey("provider", "userId", new User("test"));

        new UnifiedAccountFactory(new ArrayList<AccountFactory>()).getAccount(invalidKey);


    }

    @Test
    public void shouldObtainAccountForKey() throws Exception {

        User user = new User("test");
        Account account = mock(Account.class);
        AccountKey key = new AccountKey("provider", "userId", new User("test"));
        AccountFactory factory1 = mock(AccountFactory.class);
        UnifiedAccountFactory unifiedFactory = new UnifiedAccountFactory(Lists.newArrayList(factory1));
        when(factory1.supports("provider")).thenReturn(true);
        when(factory1.getAccount(key)).thenReturn(account);

        Account result = unifiedFactory.getAccount(key);


        assertThat(result, equalTo(account));
        verify(factory1).getAccount(key);
    }

}
