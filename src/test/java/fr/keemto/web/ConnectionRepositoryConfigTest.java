package fr.keemto.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

public class ConnectionRepositoryConfigTest {

    private ConnectionRepositoryConfig connectionRepositoryConfig;

    @Mock
    private UsersConnectionRepository usersConnectionRepository;

    @Before
    public void prepare() throws Exception {
        initMocks(this);
        connectionRepositoryConfig = new ConnectionRepositoryConfig();
        connectionRepositoryConfig.usersConnectionRepository = usersConnectionRepository;
    }

    @Test
    public void shouldCreateAConnectionRepositoryWithPrincipal() {

        ConnectionRepository connectionRepository = mock(ConnectionRepository.class);
        when(usersConnectionRepository.createConnectionRepository("user")).thenReturn(connectionRepository);
        TestingAuthenticationToken principal = new TestingAuthenticationToken("user", null);

        ConnectionRepository repo = connectionRepositoryConfig.connectionRepository(principal);

        assertThat(repo, equalTo(connectionRepository));
        verify(usersConnectionRepository).createConnectionRepository("user");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenPrincipalCannotBeRetrieved() {
        connectionRepositoryConfig.connectionRepository(null);
    }

}
