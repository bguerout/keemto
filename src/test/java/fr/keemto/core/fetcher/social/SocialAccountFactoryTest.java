package fr.keemto.core.fetcher.social;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.FetcherLocator;
import fr.keemto.util.TestConnection;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.connect.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


public class SocialAccountFactoryTest {

    private SocialAccountFactory accountFactory;
    private FetcherLocator fetcherLocator;
    private UsersConnectionRepository usersConnectionRepository;

    @Before
    public void prepare() throws Exception {
        usersConnectionRepository = mock(UsersConnectionRepository.class);
        fetcherLocator = mock(FetcherLocator.class);
        accountFactory = new SocialAccountFactory(usersConnectionRepository, fetcherLocator);
    }

    @Test
    public void whenNoConnectionExistsShouldReturnAnEmptyList() {

        User user = new User("bguerout");
        setConnectionsForUserIntoRepository("bguerout", new LinkedMultiValueMap<String, Connection<?>>());

        List<Account> accounts = accountFactory.getAccounts(user);

        assertThat(accounts.isEmpty(), is(true));
    }

    @Test
    public void canCheckIfFactorySupportsAProvider() throws Exception {
        when(fetcherLocator.hasFetcherFor("twitter")).thenReturn(true);
        assertThat(accountFactory.supports("twitter"),is(true));

        when(fetcherLocator.hasFetcherFor("invalid")).thenReturn(false);
        assertThat(accountFactory.supports("invalid"),is(false));

    }

    @Test
    public void shouldObtainAnAccountWithOneTwitterConnection() throws Exception {

        User user = new User("user");
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", new TestConnection("twitter", "mylogin"));
        setConnectionsForUserIntoRepository("user", connections);

        List<Account> accounts = accountFactory.getAccounts(user);

        assertThat(accounts.size(), equalTo(1));
        Account account = accounts.get(0);
        assertThat(account, notNullValue());
        AccountKey key = account.getKey();
        assertThat(key.getProviderId(), equalTo("twitter"));
        assertThat(key.getUser(), equalTo(user));
    }

    @Test
    public void shouldObtainAnAccountWithManyConnections() throws Exception {

        User user = new User("user");
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", new TestConnection("twitter", "mylogin"));
        connections.add("yammer", new TestConnection("yammer", "myyammerlogin"));
        setConnectionsForUserIntoRepository("user", connections);

        List<Account> accounts = accountFactory.getAccounts(user);

        assertThat(accounts.size(), equalTo(2));
    }

    @Test
    public void shouldObtainAnAccountByKey() throws Exception {

        User user = new User("user");
        AccountKey key = new AccountKey("twitter", "@stnevex", user);
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        ConnectionKey connxKey = new ConnectionKey("twitter", "@stnevex");
        when(usersConnectionRepository.createConnectionRepository("user")).thenReturn(connectionRepository);
        when(connectionRepository.getConnection(connxKey)).thenReturn(new TestConnection("twitter", "@stnevex"));

        Account account = accountFactory.getAccount(key);

        assertThat(account, notNullValue());
        assertThat(key, equalTo(account.getKey()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenKeyIsInvalid() throws Exception {

        User user = new User("user");
        AccountKey key = new AccountKey("twitter", "@stnevex", user);
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        ConnectionKey connxKey = new ConnectionKey("twitter", "@stnevex");
        when(usersConnectionRepository.createConnectionRepository("user")).thenReturn(connectionRepository);
        when(connectionRepository.getConnection(connxKey)).thenThrow(new NoSuchConnectionException(connxKey));

        Account account = accountFactory.getAccount(key);

        assertThat(account, notNullValue());
        assertThat(key, equalTo(account.getKey()));
    }


    private void setConnectionsForUserIntoRepository(String username, MultiValueMap<String, Connection<?>> connections) {
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        when(connectionRepository.findAllConnections()).thenReturn(connections);
        when(usersConnectionRepository.createConnectionRepository(username)).thenReturn(connectionRepository);
    }
}
