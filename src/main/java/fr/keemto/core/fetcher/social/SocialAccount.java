package fr.keemto.core.fetcher.social;

import fr.keemto.core.*;
import fr.keemto.core.fetcher.Fetcher;
import org.springframework.social.connect.Connection;

import java.util.ArrayList;
import java.util.List;

public class SocialAccount implements Account {

    private final AccountKey key;
    private final Fetcher<Connection<?>> fetcher;
    private final Connection<?> connection;
    private final MinimalConnectionRepository repository;

    public SocialAccount(AccountKey key, Fetcher<Connection<?>> fetcher, Connection<?> connection, MinimalConnectionRepository repository) {
        this.key = key;
        this.connection = connection;
        this.fetcher = fetcher;
        this.repository = repository;
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
        repository.revoke(connection.getKey());
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
}
