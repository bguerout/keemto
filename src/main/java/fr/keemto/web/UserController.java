package fr.keemto.web;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.AccountRegistry;
import fr.keemto.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/api/users")
public class UserController {

    private final AccountRegistry accountRegistry;

    @Autowired
    public UserController(AccountRegistry accountRegistry) {
        this.accountRegistry = accountRegistry;
    }

    @RequestMapping(value = "/{userName}/accounts", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getUserConnections(@PathVariable String userName) {
        User user = getCurrentUser(userName);
        return accountRegistry.findAccounts(user);
    }

    @RequestMapping(value = {"/{userName}/accounts/{providerId}-{providerUserId}"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void revokeAccount(Principal principal, @PathVariable String providerId, @PathVariable String providerUserId) {
        User user = getCurrentUser(principal.getName());
        AccountKey key = new AccountKey(providerId, providerUserId, user);
        accountRegistry.revoke(key);
    }

    private User getCurrentUser(String userName) {
        return new User(userName);
    }


}
