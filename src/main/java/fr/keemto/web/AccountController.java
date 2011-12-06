package fr.keemto.web;

import fr.keemto.core.Account;
import fr.keemto.core.User;
import fr.keemto.core.UserRepository;
import fr.keemto.provider.social.SocialAccountFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = "/api/accounts")
public class AccountController {

    public static final int MAX_RANDOM_ACCOUNTS = 5;
    private final SocialAccountFactory accountFactory;
    private final UserRepository userRepository;
    private int maxRandomAccount = MAX_RANDOM_ACCOUNTS;

    @Autowired
    public AccountController(SocialAccountFactory accountFactory, UserRepository userRepository) {
        this.accountFactory = accountFactory;
        this.userRepository = userRepository;
    }


    @RequestMapping(value = "/random", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getRandomAccounts() {
        List<Account> accounts = new ArrayList<Account>();
        List<User> users = userRepository.getAllUsers();
        for (User user : users) {
            accounts.addAll(accountFactory.getAccounts(user));
        }
        return randomize(accounts, Account.class);
    }

    private <T> List<T> randomize(List<T> accounts, Class<T> clazz) {
        Collections.shuffle(accounts);
        return accounts.subList(0, accounts.size() < maxRandomAccount ? accounts.size() : maxRandomAccount);
    }

    public void setMaxRandomAccount(int maxRandomAccount) {
        this.maxRandomAccount = maxRandomAccount;
    }
}
