package fr.keemto.provider.social.fetcher;

import fr.keemto.core.EventData;
import fr.keemto.core.fetcher.Fetcher;
import org.springframework.social.connect.Connection;

import java.util.ArrayList;
import java.util.List;

public abstract class ConnectionFetcher<A, D> implements Fetcher<Connection<A>> {

    private long delay;

    public ConnectionFetcher(long delay) {
        super();
        this.delay = delay;
    }

    @Override
    public List<EventData> fetch(Connection<A> connection, long lastFetchedEventTime) {
        List<EventData> events = new ArrayList<EventData>();
        A api = connection.getApi();
        List<D> fetchedDatas = fetchApi(api, lastFetchedEventTime);
        for (D data : fetchedDatas) {
            EventData eventData = convertDataToEvent(data);
            events.add(eventData);
        }
        return events;
    }

    protected abstract List<D> fetchApi(A api, long lastFetchedEventTime);

    protected abstract EventData convertDataToEvent(D data);

}
