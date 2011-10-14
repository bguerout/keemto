package fr.keemto.provider;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;

import java.util.ArrayList;
import java.util.List;

public class MailAccount implements Account {

    private final AccountKey key;

    public MailAccount(AccountKey key) {
        this.key = key;
    }

    @Override
    public List<Event> fetch(long newerThan) {
        return new ArrayList<Event>();
    }

    @Override
    public AccountKey getKey() {
        return key;
    }

    @Override
    public String getDisplayName() {
        return key.getProviderUserId();
    }

    @Override
    public String getProfileUrl() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailAccount)) return false;

        MailAccount that = (MailAccount) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MailAccount{" +
                "key=" + key +
                '}';
    }
}
