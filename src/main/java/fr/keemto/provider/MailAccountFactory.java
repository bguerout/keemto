package fr.keemto.provider;

import fr.keemto.core.Account;
import fr.keemto.core.AccountFactory;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;

import java.util.ArrayList;
import java.util.List;

public class MailAccountFactory implements AccountFactory {

    @Override
    public List<Account> getAccounts(User user) {
        return new ArrayList<Account>();
    }

    @Override
    public Account getAccount(AccountKey key) {
        return new MailAccount(key);
    }

    @Override
    public boolean supports(String providerId) {
        return "mail".equals(providerId);
    }

}
