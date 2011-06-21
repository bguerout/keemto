package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.xevents.core.User;

@Service
public class DefaultFetcherResolver implements FetcherResolver {

    private static final Logger log = LoggerFactory.getLogger(DefaultFetcherResolver.class);

    private final List<Fetcher> fetchers;

    @Autowired
    public DefaultFetcherResolver(List<Fetcher> fetchers) {
        this.fetchers = fetchers;
        handleEmptyFetchersList();
    }

    private void handleEmptyFetchersList() {
        if (fetchers != null && fetchers.isEmpty()) {
            log.warn("Fetchers List is empty. This resolver will not be able to resolver fetchers.");
        }
    }

    @Override
    public List<Fetcher> resolve(User user) {
        List<Fetcher> validFetchers = new ArrayList<Fetcher>();
        for (Fetcher fetcher : fetchers) {
            if (fetcher.canFetch(user)) {
                validFetchers.add(fetcher);
            } else {
                log.debug("Fetcher " + fetcher.getProviderId()
                        + " is rejected because it cannot fetch events for user: " + user + ".");
            }
        }
        return validFetchers;
    }

    @Override
    public List<Fetcher> resolveAll() {
        return Collections.unmodifiableList(fetchers);
    }

}
