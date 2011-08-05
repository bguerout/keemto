package fr.keemto.core.fetcher;

import java.util.List;

import fr.keemto.core.User;

public interface FetcherResolver {

    List<Fetcher> resolve(User user);

    List<Fetcher> resolveAll();

}
