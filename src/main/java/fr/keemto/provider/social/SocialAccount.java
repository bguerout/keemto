package fr.keemto.provider.social;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;
import fr.keemto.core.RevocationHanlder;
import fr.keemto.core.fetching.Fetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;

import java.util.ArrayList;
import java.util.List;

public class SocialAccount implements Account {

    private static final Logger log = LoggerFactory.getLogger(SocialAccount.class);

    private final AccountKey key;
    private final Fetcher<Connection<?>> fetcher;
    private final RevocationHanlder revocationHanlder;
    private final Connection<?> connection;

    public SocialAccount(AccountKey key, Fetcher<Connection<?>> fetcher, Connection<?> connection, RevocationHanlder revocationHanlder) {
        this.key = key;
        this.connection = connection;
        this.fetcher = fetcher;
        this.revocationHanlder = revocationHanlder;
    }

    @Override
    public List<Event> fetch(long newerThan) {
        List<Event> events = new ArrayList<Event>();
        List<EventData> datas = fetcher.fetch(connection, newerThan);
        for (EventData data : datas) {
            Event event = new Event(data.getTimestamp(), data.getMessage(), this);
            events.add(event);
        }
        return events;
    }

    @Override
    public void revoke() {
        revocationHanlder.revoke(key);
        log.info("Social Account {} has been revoked", key);
    }

    @Override
    public String getDisplayName() {
        return connection.getDisplayName();
    }

    @Override
    public String getProfileUrl() {
        return connection.getProfileUrl();
    }

    @Override
    public String getImageUrl() {
        return connection.getImageUrl();
    }

    @Override
    public AccountKey getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SocialAccount)) return false;

        SocialAccount that = (SocialAccount) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
