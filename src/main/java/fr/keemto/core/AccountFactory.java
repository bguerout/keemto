package fr.keemto.core;

import java.util.List;

public interface AccountFactory {
    List<Account> getAccounts(User user);
}
