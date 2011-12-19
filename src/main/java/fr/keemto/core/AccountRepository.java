package fr.keemto.core;

import java.util.List;

public interface AccountRepository {

    List<Account> getAccounts(User user);

    Account getAccount(AccountKey key);

    boolean supports(String providerId);
}
