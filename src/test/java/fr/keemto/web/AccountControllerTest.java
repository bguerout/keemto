package fr.keemto.web;

import com.google.common.collect.Lists;
import fr.keemto.TestAccount;
import fr.keemto.core.*;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.Principal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountControllerTest extends ControllerTestCase {

    private AccountController controller;
    private UserRepository userRepository;
    private AccountFactory accountFactory;
    private Account account;
    private User user;


    @Before
    public void initBeforeTest() throws Exception {

        accountFactory = mock(AccountFactory.class);
        userRepository = mock(UserRepository.class);
        controller = new AccountController(accountFactory, userRepository);

        request.addHeader("Accept", "application/json");
        request.setMethod("GET");
        request.setRequestURI("/api/accounts/random");

        user = new User("test");
        account = new TestAccount(new AccountKey("provider", "id", user), "", "", "");
    }

    @Test
    public void testGetRandomAccountForAUser() throws Exception {

        when(userRepository.getAllUsers()).thenReturn(Lists.newArrayList(user));
        when(accountFactory.getAccounts(user)).thenReturn(Lists.newArrayList(account));
        controller.setMaxRandomAccount(1);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode jsonNode = toJsonNode(response.getContentAsString());
        assertThat(jsonNode.size(), equalTo(1));
        JsonNode connx = jsonNode.get(0);
        JsonNode keyNode = connx.get("key");
        assertThat(keyNode.get("providerId").getValueAsText(), equalTo("provider"));
    }


    @Test
    public void testGetRandomAccountsForManyUsers() throws Exception {

        when(userRepository.getAllUsers()).thenReturn(Lists.newArrayList(user, user, user));
        when(accountFactory.getAccounts(user)).thenReturn(Lists.newArrayList(account, account, account));
        controller.setMaxRandomAccount(2);

        handlerAdapter.handle(request, response, controller);

        assertThat(response.getStatus(), equalTo(200));
        JsonNode jsonNode = toJsonNode(response.getContentAsString());
        assertThat(jsonNode.size(), equalTo(2));
    }
}
