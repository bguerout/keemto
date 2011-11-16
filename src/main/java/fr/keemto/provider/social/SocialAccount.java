package fr.keemto.provider.social;

import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;
import fr.keemto.core.fetching.Fetcher;
import org.springframework.social.connect.Connection;

import java.util.ArrayList;
import java.util.List;

public class SocialAccount implements Account {

    private final AccountKey key;
    private final Fetcher<Connection<?>> fetcher;
    private final Connection<?> connection;

    public SocialAccount(AccountKey key, Fetcher<Connection<?>> fetcher, Connection<?> connection) {
        this.key = key;
        this.connection = connection;
        this.fetcher = fetcher;
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
