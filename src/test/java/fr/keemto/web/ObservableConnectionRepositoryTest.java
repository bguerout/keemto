package fr.keemto.web;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.core.User;
import fr.keemto.provider.social.SocialAccountKey;
import org.junit.Before;
import org.junit.Test;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ObservableConnectionRepositoryTest {

    private ObservableConnectionRepository connectionRepository;
    private ConnectionRepository realConnectionRepository;
    private AccountInterceptor interceptor;
    private String username = "aUser";

    @Before
    public void setUp() throws Exception {
        realConnectionRepository = mock(ConnectionRepository.class);
        interceptor = mock(AccountInterceptor.class);
        connectionRepository = new ObservableConnectionRepository(username, realConnectionRepository, interceptor);
    }

    @Test
    public void whenConnectionIsRemovedShouldTriggerInterceptor() throws Exception {

        ConnectionKey key = new ConnectionKey("provider", "userId");

        connectionRepository.removeConnection(key);


        SocialAccountKey socialAccountKey = new SocialAccountKey(key, new User(username));
        verify(interceptor).accountDeleted(socialAccountKey);
    }

    @Test
    public void whenAllProviderConnectionsAreRemovedShouldTriggerInterceptor() throws Exception {

        Connection connection = mock(Connection.class);
        ConnectionKey key = new ConnectionKey("provider", "userId");
        List<Connection<?>> connections = new ArrayList<Connection<?>>();
        connections.add(connection);
        connections.add(connection);
        when(realConnectionRepository.findConnections("provider")).thenReturn(connections);
        when(connection.getKey()).thenReturn(key);

        connectionRepository.removeConnections("provider");


        verify(interceptor, times(2)).accountDeleted(any(SocialAccountKey.class));
    }

    @Test
    public void whenConnectionIsAddedShouldTriggerInterceptor() throws Exception {

        ConnectionKey key = new ConnectionKey("provider", "userId");
        Connection connection = mock(Connection.class);
        when(connection.getKey()).thenReturn(key);

        connectionRepository.addConnection(connection);


        SocialAccountKey socialAccountKey = new SocialAccountKey(key, new User(username));
        verify(interceptor).accountCreated(socialAccountKey);
    }

}

