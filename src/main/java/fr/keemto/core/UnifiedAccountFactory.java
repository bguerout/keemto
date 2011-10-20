package fr.keemto.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UnifiedAccountFactory implements AccountFactory {

    private static final Logger log = LoggerFactory.getLogger(UnifiedAccountFactory.class);

    private final List<AccountFactory> factories;

    public UnifiedAccountFactory(List<AccountFactory> factories) {
        this.factories = factories;
    }

    @Override
    public List<Account> getAccounts(User user) {
        List<Account> accounts = new ArrayList<Account>();
        for (AccountFactory factory : factories) {
            accounts.addAll(factory.getAccounts(user));
        }
        return accounts;
    }

    @Override
    public Account getAccount(AccountKey key) {
        String providerId = key.getProviderId();
        return findFactory(providerId).getAccount(key);

    }

    @Override
    public void revoke(AccountKey key) {
        String providerId = key.getProviderId();
        findFactory(providerId).revoke(key);
    }

    @Override
    public boolean supports(String providerId) {
        return true;
    }

    private AccountFactory findFactory(String providerId) {
        for (AccountFactory factory : factories) {
            if (factory.supports(providerId)) {
                return factory;
            }
        }
        throw new IllegalArgumentException("Unable to find an account factory for " + providerId
                + ". Please check if provider " + providerId + " is a valid provider.");
    }
}
