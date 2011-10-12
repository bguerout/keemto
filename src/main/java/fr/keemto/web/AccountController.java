package fr.keemto.web;

import fr.keemto.core.Account;
import fr.keemto.core.AccountFactory;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Scope(value = "request")
@RequestMapping(value = "/api/accounts")
public class AccountController {

    private final AccountFactory accountFactory;

    public AccountController(AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getUserConnections() {
        return accountFactory.getAccounts(getCurrentUser());
    }

    @RequestMapping(value = "/{providerId}-{providerUserId}", method = RequestMethod.GET)
    @ResponseBody
    public Account getUserConnections(@PathVariable String providerId, @PathVariable String providerUserId) {
        //TODO we should use a ConnectionKeyBuilder to convert id to provider*Id
        return getAccountForCurrentUser(providerId, providerUserId);
    }

    private Account getAccountForCurrentUser(String providerId, String providerUserId) {
        User user = getCurrentUser();
        return accountFactory.getAccount(new AccountKey(providerId, providerUserId, user));
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @RequestMapping(value = {"/{providerId}-{providerUserId}"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeConnection(@PathVariable String providerId, @PathVariable String providerUserId) {
        Account revokableAccount = getAccountForCurrentUser(providerId, providerUserId);
        revokableAccount.revoke();
    }


}
