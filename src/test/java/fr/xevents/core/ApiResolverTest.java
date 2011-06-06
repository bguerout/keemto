package fr.xevents.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
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
import org.springframework.social.twitter.api.TwitterApi;

import com.google.common.collect.Lists;

import fr.xevents.core.ApiResolver;
import fr.xevents.core.User;

public class ApiResolverTest {

    private ApiResolver<TwitterApi> resolver;

    @Mock
    private UsersConnectionRepository usersConnectionRepository;

    @Mock
    private TwitterApi api;

    @Mock
    private ConnectionRepository connRepository;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        resolver = new ApiResolver<TwitterApi>(TwitterApi.class, usersConnectionRepository);

        when(usersConnectionRepository.createConnectionRepository("bguerout")).thenReturn(connRepository);

    }

    @Test
    public void whenNoApiExistsThenShouldReturnAnEmptyList() throws Exception {

        List<TwitterApi> apis = resolver.getApis(new User("bguerout"));

        assertThat(apis, notNullValue());
    }

    @Test
    public void shouldUseSocialRepositoryToResolveApis() {

        Connection<TwitterApi> twitterConnection = mock(Connection.class);
        when(connRepository.findConnectionsToApi(TwitterApi.class)).thenReturn(Lists.newArrayList(twitterConnection));
        when(twitterConnection.getApi()).thenReturn(api);

        List<TwitterApi> apis = resolver.getApis(new User("bguerout"));

        assertThat(apis, hasItem(api));

    }

}
