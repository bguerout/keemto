package fr.keemto.core.fetcher.social;

import fr.keemto.core.*;
import fr.keemto.core.fetcher.Fetcher;
import org.springframework.social.connect.Connection;

import java.util.ArrayList;
import java.util.List;

public class SocialAccount extends Account {

    private final Connection<?> connection;
    private final Fetcher<Connection<?>> fetcher;

    public SocialAccount(AccountKey key, Fetcher<Connection<?>> fetcher, Connection<?> connection) {
        super(key);
        this.connection = connection;
        this.fetcher = fetcher;
    }

    @Override
    public List<Event> fetch(long newerThan) {
        List<Event> events = new ArrayList<Event>();
        List<EventData> datas = fetcher.fetch(connection, newerThan);
        AccountKey key = getKey();
        for (EventData data : datas) {
            ProviderConnection pconnection = new SocialProviderConnection(connection);
            Event event = new Event(data.getTimestamp(), data.getMessage(), key.getUser(), pconnection);
            events.add(event);
        }
        return events;
    }
}
