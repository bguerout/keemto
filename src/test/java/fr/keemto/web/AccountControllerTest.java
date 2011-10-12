package fr.keemto.web;

import com.google.common.collect.Lists;
import fr.keemto.core.Account;
import fr.keemto.core.AccountFactory;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import fr.keemto.util.NullConnection;
import fr.keemto.util.TestAccount;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountControllerTest extends ControllerTestCase {

    private static final Logger log = LoggerFactory.getLogger(ConnectionControllerTest.class);

    private AccountController controller;

    @Mock
    private AccountFactory accountFactory;
    private User user;
    private TestAccount account;
    private AccountKey key;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        controller = new AccountController(accountFactory);

        request.addHeader("Accept", "application/json");

        user = new User("bguerout");
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null));

        String providerUserId = "1111";
        String providerId = "twitter";
        String profileUrl = "http://twitter.com/stnevex";
        String imageUrl = "http://twitter.com/stnevex.jpg";
        String displayName = "stnevex";
        key = new AccountKey(providerId, providerUserId, user);
        account = new TestAccount(key, displayName, profileUrl, imageUrl);
    }

    @Test
    public void showReturnAllConnections() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/accounts");
        List<Account> accounts = new ArrayList<Account>();
        accounts.add(account);
        when(accountFactory.getAccounts(user)).thenReturn(accounts);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode jsonNode = toJsonNode(response.getContentAsString());
        assertThat(jsonNode.isArray(), is(true));
        assertThat(jsonNode.has(0), is(true));
        JsonNode connx = jsonNode.get(0);
        assertThat(connx.get("displayName").getValueAsText(), equalTo("stnevex"));
        assertThat(connx.get("profileUrl").getValueAsText(), equalTo("http://twitter.com/stnevex"));
        assertThat(connx.get("imageUrl").getValueAsText(), equalTo("http://twitter.com/stnevex.jpg"));

        JsonNode keyNode = connx.get("key");
        assertThat(keyNode.get("id").getValueAsText(), equalTo("twitter-1111"));
        assertThat(keyNode.get("providerId").getValueAsText(), equalTo("twitter"));

    }

    @Test
    public void whenUserHasNoConnectionShouldReturnEmptyJson() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/accounts");
        when(accountFactory.getAccounts(user)).thenReturn(new ArrayList<Account>());

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode node = toJsonNode(response.getContentAsString());
        assertThat(node.size(), equalTo(0));
    }

    @Test
    public void showReturnConnectionById() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/accounts/twitter-1111");
        when(accountFactory.getAccount(key)).thenReturn(account);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode connx = toJsonNode(response.getContentAsString());

        assertThat(connx.get("displayName").getTextValue(), equalTo("stnevex"));
        assertThat(connx.get("profileUrl").getTextValue(), equalTo("http://twitter.com/stnevex"));
        assertThat(connx.get("imageUrl").getTextValue(), equalTo("http://twitter.com/stnevex.jpg"));

        JsonNode keyNode = connx.get("key");
        assertThat(keyNode.get("id").getValueAsText(), equalTo("twitter-1111"));
        assertThat(keyNode.get("providerId").getValueAsText(), equalTo("twitter"));

    }

    @Test
    public void shouldDeleteConnection() throws Exception {

        request.setMethod("DELETE");
        request.setRequestURI("/api/accounts/twitter-1111");
        when(accountFactory.getAccount(key)).thenReturn(account);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(204));
        assertThat(account.hasBeenRevoked(), is(true));
    }


    @Test
    public void shouldDeleteConnectionBySplittingKeyWithLastIndexOfMinus() throws Exception {

        request.setMethod("DELETE");
        request.setRequestURI("/api/accounts/linked-in-9999");
        when(accountFactory.getAccount(new AccountKey("linked-in", "9999", user))).thenReturn(account);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(204));
    }
}
