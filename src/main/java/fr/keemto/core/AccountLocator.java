package fr.keemto.core;

import java.util.ArrayList;
import java.util.List;

public class AccountLocator {

    private final List<AccountRepository> registeredRepositories;

    public AccountLocator(List<AccountRepository> preRegisteredRepositories) {
        this.registeredRepositories = new ArrayList<AccountRepository>(preRegisteredRepositories);
    }

    public List<Account> findAccounts(User user) {
        List<Account> accounts = new ArrayList<Account>();
        for (AccountRepository repository : registeredRepositories) {
            List<Account> userAccounts = repository.getAccounts(user);
            accounts.addAll(userAccounts);
        }
        return accounts;
    }

    public Account findAccount(AccountKey key) {
        String providerId = key.getProviderId();
        AccountRepository accountRepository = selectFactoryByProvider(providerId);
        return accountRepository.getAccount(key);
    }

    private AccountRepository selectFactoryByProvider(String providerId) {
        for (AccountRepository repository : registeredRepositories) {
            if (repository.supports(providerId)) {
                return repository;
            }
        }
        throw new IllegalArgumentException("Unable to find an account factory for " + providerId
                + ". Please check if provider " + providerId + " is a valid provider.");
    }

    public void register(AccountRepository repository) {
        registeredRepositories.add(repository);
    }
}
