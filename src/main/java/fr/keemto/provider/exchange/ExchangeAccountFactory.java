package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import fr.keemto.core.Account;
import fr.keemto.core.AccountFactory;
import fr.keemto.core.AccountKey;
import fr.keemto.core.User;

import java.util.List;

public class ExchangeAccountFactory implements AccountFactory {

    private static final String PROVIDER_ID = "exchange";

    private final MailRepository mailRepository;
    private final List<String> allowedRecipients;

    public ExchangeAccountFactory(MailRepository mailRepository, List<String> allowedRecipients) {
        this.mailRepository = mailRepository;
        this.allowedRecipients = allowedRecipients;
    }

    @Override
    public List<Account> getAccounts(User user) {
        String email = user.getEmail();
        AccountKey key = new AccountKey(PROVIDER_ID, email, user);
        Account account = new ExchangeAccount(key, allowedRecipients, mailRepository);
        return Lists.newArrayList(account);
    }

    @Override
    public Account getAccount(AccountKey key) {
        return new ExchangeAccount(key, mailRepository);
    }

    @Override
    public boolean supports(String providerId) {
        if (providerId != null) {
            return providerId.startsWith(PROVIDER_ID);
        }
        return false;
    }
}
