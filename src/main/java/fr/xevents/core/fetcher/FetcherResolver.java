package fr.xevents.core.fetcher;

import java.util.List;

import fr.xevents.core.User;

public interface FetcherResolver {

    List<Fetcher> resolve(User user);

    List<Fetcher> resolveAll();

}
