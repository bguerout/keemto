package fr.keemto.util;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;

import java.util.ArrayList;
import java.util.List;

public class TestAccount implements Account {

    private final AccountKey key;
    private final String displayName;
    private final String profileUrl;
    private final String imageUrl;
    private boolean hasBeenRevoked;

    public TestAccount(AccountKey key, String displayName, String profileUrl, String imageUrl) {
        this.key = key;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
    }

    @Override
    public AccountKey getKey() {
        return key;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public List<Event> fetch(long newerThan) {
        return new ArrayList<Event>();
    }

    @Override
    public void revoke() {
        hasBeenRevoked = true;
    }

    public boolean hasBeenRevoked() {
        return hasBeenRevoked;
    }
}
