package fr.keemto.core.fetcher.social;

import fr.keemto.core.Account;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.Fetcher;
import fr.keemto.core.fetcher.FetcherLocator;
import fr.keemto.core.fetcher.social.SocialAccountFactory;
import fr.keemto.util.DummyConnection;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
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
    public void shouldObtainAnAccountWithOneTwitterConnection() throws Exception {

        User user = new User("user");
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", new DummyConnection("twitter", "mylogin"));
        setConnectionsForUserIntoRepository("user", connections);

        List<Account> accounts = accountFactory.getAccounts(user);

        assertThat(accounts.size(), equalTo(1));
        Account account = accounts.get(0);
        assertThat(account, notNullValue());
        assertThat(account.getProviderId(), equalTo("twitter"));
        assertThat(account.getKey().getUser(), equalTo(user));//TODO demeter ...
    }

    @Test
    public void shouldObtainAnAccountWithManyConnections() throws Exception {

        User user = new User("user");
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", new DummyConnection("twitter", "mylogin"));
        connections.add("yammer", new DummyConnection("yammer", "myyammerlogin"));
        setConnectionsForUserIntoRepository("user", connections);

        List<Account> accounts = accountFactory.getAccounts(user);

        assertThat(accounts.size(), equalTo(2));
    }


    private void setConnectionsForUserIntoRepository(String username, MultiValueMap<String, Connection<?>> connections) {
        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        when(connectionRepository.findAllConnections()).thenReturn(connections);
        when(usersConnectionRepository.createConnectionRepository(username)).thenReturn(connectionRepository);
    }
}
