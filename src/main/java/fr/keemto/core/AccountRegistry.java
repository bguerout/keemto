package fr.keemto.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AccountRegistry {

    private static final Logger log = LoggerFactory.getLogger(AccountRegistry.class);

    private final List<AccountFactory> factories;

    public AccountRegistry(List<AccountFactory> factories) {
        this.factories = factories;
    }

    public List<Account> findAccounts(User user) {
        List<Account> accounts = new ArrayList<Account>();
        for (AccountFactory factory : factories) {
            List<Account> userAccounts = factory.getAccounts(user);
            accounts.addAll(userAccounts);
        }
        return accounts;
    }

    public Account findAccount(AccountKey key) {
        String providerId = key.getProviderId();
        AccountFactory accountFactory = selectFactoryByProvider(providerId);
        return accountFactory.getAccount(key);
    }

    public void revoke(AccountKey key) {
        String providerId = key.getProviderId();
        AccountFactory accountFactory = selectFactoryByProvider(providerId);
        accountFactory.revoke(key);
    }

    private AccountFactory selectFactoryByProvider(String providerId) {
        for (AccountFactory factory : factories) {
            if (factory.supports(providerId)) {
                return factory;
            }
        }
        throw new IllegalArgumentException("Unable to find an account factory for " + providerId
                + ". Please check if provider " + providerId + " is a valid provider.");
    }

    public void addFactory(AccountFactory factory) {
        factories.add(factory);
    }
}
