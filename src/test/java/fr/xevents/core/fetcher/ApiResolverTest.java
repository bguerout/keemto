package fr.xevents.core.fetcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Twitter;

import com.google.common.collect.Lists;

import fr.xevents.core.User;

public class ApiResolverTest {

    private ApiResolver<Twitter> resolver;

    @Mock
    private UsersConnectionRepository usersConnectionRepository;

    @Mock
    private Twitter api;

    @Mock
    private ConnectionRepository connRepository;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        resolver = new ApiResolver<Twitter>(Twitter.class, usersConnectionRepository);

        when(usersConnectionRepository.createConnectionRepository("bguerout")).thenReturn(connRepository);

    }

    @Test
    public void whenNoApiExistsThenShouldReturnAnEmptyList() throws Exception {

        List<Twitter> apis = resolver.getApis(new User("bguerout"));

        assertThat(apis, notNullValue());
    }

    @Test
    public void shouldUseSocialRepositoryToResolveApis() {

        Connection<Twitter> twitterConnection = mock(Connection.class);
        when(connRepository.findConnections(Twitter.class)).thenReturn(Lists.newArrayList(twitterConnection));
        when(twitterConnection.getApi()).thenReturn(api);

        List<Twitter> apis = resolver.getApis(new User("bguerout"));

        assertThat(apis, hasItem(api));

    }

}
