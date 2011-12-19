package fr.keemto.web;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.AccountLocator;
import fr.keemto.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/api/users")
public class UserAccountController {

    private final AccountLocator accountLocator;

    @Autowired
    public UserAccountController(AccountLocator accountLocator) {
        this.accountLocator = accountLocator;
    }

    @RequestMapping(value = "/{userName}/accounts", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getUserConnections(@PathVariable String userName) {
        User user = getCurrentUser(userName);
        return accountLocator.findAccounts(user);
    }

    @RequestMapping(value = {"/{userName}/accounts/{providerId}-{providerUserId}-{username}"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void revokeAccount(Principal principal, @PathVariable String providerId, @PathVariable String providerUserId) {
        User user = getCurrentUser(principal.getName());
        AccountKey key = new AccountKey(providerId, providerUserId, user);
        Account account = accountLocator.findAccount(key);
        account.revoke();
    }

    private User getCurrentUser(String userName) {
        return new User(userName);
    }


}
