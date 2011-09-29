package fr.keemto.core;

import fr.keemto.core.fetcher.Fetcher;

import java.util.List;

public class Account {

    private final AccountKey key;
    private final User user;
    private final Fetcher fetcher;

    public Account(AccountKey key, User user, Fetcher fetcher) {
        this.key = key;
        this.user = user;
        this.fetcher = fetcher;
    }

    @Deprecated
    public Account(User user, String providerId) {
        this(new AccountKey(providerId, user.getUsername()), user, null);
    }

    public User getUser() {
        return user;
    }

    public String getProviderId() {
        return key.getProviderId();
    }

    public List<Event> fetch(long newerThan) {
        return fetcher.fetch(user, newerThan);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        if (key != null ? !key.equals(account.key) : account.key != null) {
            return false;
        }
        if (user != null ? !user.equals(account.user) : account.user != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "user=" + user +
                ", key=" + key +
                '}';
    }


}
