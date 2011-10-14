package fr.keemto.web;

import fr.keemto.core.Account;
import fr.keemto.core.AccountFactory;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@Scope(value = "request")
@RequestMapping(value = "/api/users")
public class AccountController {

    private final AccountFactory accountFactory;

    @Autowired
    public AccountController(AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }

    @RequestMapping(value = "/{userName}/accounts", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getUserConnections(@PathVariable String userName) {
        User user = getCurrentUser(userName);
        return accountFactory.getAccounts(user);
    }

    @RequestMapping(value = {"/{userName}/accounts/{providerId}-{providerUserId}"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void revokeAccount(Principal principal, @PathVariable String providerId, @PathVariable String providerUserId) {
        User user = getCurrentUser(principal.getName());
        accountFactory.revoke(new AccountKey(providerId, providerUserId, user));
    }

    private User getCurrentUser(String userName) {
        return new User(userName);
    }


}
