package fr.keemto.core;

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
