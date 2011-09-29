package fr.keemto.core.fetcher.social;


import fr.keemto.core.Account;
import fr.keemto.core.AccountFactory;
import fr.keemto.core.AccountKeyAdapter;
import fr.keemto.core.User;
import fr.keemto.core.fetcher.Fetcher;
import fr.keemto.core.fetcher.FetcherLocator;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class SocialAccountFactory implements AccountFactory {

    private final UsersConnectionRepository usersConnectionRepository;
    private final FetcherLocator fetcherLocator;

    public SocialAccountFactory(UsersConnectionRepository usersConnectionRepository, FetcherLocator fetcherLocator) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.fetcherLocator = fetcherLocator;
    }

    @Override
    public List<Account> getAccounts(User user) {
        MultiValueMap<String, Connection<?>> allConnections = getConnectionRepository(user);
        if (allConnections.isEmpty()) {
            return new ArrayList<Account>();
        }
        List<Account> accounts = new ArrayList<Account>();
        for (List<Connection<?>> connectionsPerProvider : allConnections.values()) {
            for (Connection<?> connection : connectionsPerProvider) {
                Account account = createAccount(user, connection);
                accounts.add(account);
            }
        }
        return accounts;
    }

    private Account createAccount(User user, Connection<?> connection) {
        Fetcher fetcher = findFetcherForConnection(connection);
        AccountKeyAdapter accountKey = new AccountKeyAdapter(connection.getKey());
        return new Account(accountKey, user, fetcher);
    }

    private Fetcher findFetcherForConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        String providerId = key.getProviderId();
        return fetcherLocator.getFetcher(providerId);
    }

    private MultiValueMap<String, Connection<?>> getConnectionRepository(User user) {
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(user.getUsername());
        return connectionRepository.findAllConnections();
    }

}
