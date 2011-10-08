package fr.keemto.core;

import fr.keemto.core.fetcher.Fetcher;

import java.util.List;

public abstract class Account {

    private final AccountKey key;


    protected Account(AccountKey key) {
        this.key = key;
    }

    public String getProviderId() {
        return key.getProviderId();
    }

    public AccountKey getKey() {
        return key;
    }

    public abstract List<Event> fetch(long newerThan);


}
