package fr.keemto.web;

import fr.keemto.TestAccount;
import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.AccountRepository;
import fr.keemto.core.User;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTest extends ControllerTestCase {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private UserController controller;

    @Mock
    private AccountRepository accountRepository;
    private User user;
    private TestAccount account;
    private AccountKey key;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        user = new User("stnevex");
        controller = new UserController(accountRepository);

        request.addHeader("Accept", "application/json");
        request.setUserPrincipal(new Principal() {
            @Override
            public String getName() {
                return user.getUsername();
            }
        });


        String providerUserId = "1111";
        String providerId = "twitter";
        String profileUrl = "http://twitter.com/stnevex";
        String imageUrl = "http://twitter.com/stnevex.jpg";
        String displayName = "stnevex";
        key = new AccountKey(providerId, providerUserId, user);
        account = new TestAccount(key, displayName, profileUrl, imageUrl);
    }

    @Test
    public void showReturnAllAccounts() throws Exception {

        request.setMethod("GET");
        request.setRequestURI("/api/users/stnevex/accounts");
        List<Account> accounts = new ArrayList<Account>();
        accounts.add(account);
        when(accountRepository.findAccounts(user)).thenReturn(accounts);

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
        request.setRequestURI("/api/users/stnevex/accounts");
        when(accountRepository.findAccounts(user)).thenReturn(new ArrayList<Account>());

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode node = toJsonNode(response.getContentAsString());
        assertThat(node.size(), equalTo(0));
    }


    @Test
    public void shouldDeleteConnection() throws Exception {

        request.setMethod("DELETE");
        request.setRequestURI("/api/users/stnevex/accounts/twitter-1111");
        when(accountRepository.findAccount(key)).thenReturn(account);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(204));
        verify(accountRepository).revoke(key);
    }


    @Test
    public void shouldDeleteConnectionBySplittingKeyWithLastIndexOfMinus() throws Exception {

        request.setMethod("DELETE");
        request.setRequestURI("/api/users/stnevex/accounts/linked-in-9999");
        when(accountRepository.findAccount(new AccountKey("linked-in", "9999", user))).thenReturn(account);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(204));
    }
}
