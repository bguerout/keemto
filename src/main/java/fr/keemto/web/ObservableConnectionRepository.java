package fr.keemto.web;

import fr.keemto.core.AccountInterceptor;
import fr.keemto.provider.social.SocialAccountKey;
import fr.keemto.core.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class ObservableConnectionRepository implements ConnectionRepository {

    private final String username;
    private final ConnectionRepository repository;
    private final AccountInterceptor interceptor;

    public ObservableConnectionRepository(String username, ConnectionRepository repository, AccountInterceptor interceptor) {
        this.username = username;
        this.repository = repository;
        this.interceptor = interceptor;
    }

    @Override
    public void addConnection(Connection<?> connection) {
        repository.addConnection(connection);

        SocialAccountKey socialAccountKey = toSocialAccountKey(connection.getKey());
        interceptor.accountCreated(socialAccountKey);
    }

    @Override
    public void removeConnections(String providerId) {
        List<Connection<?>> connections = repository.findConnections(providerId);
        for (Connection<?> connection : connections) {
            interceptor.accountDeleted(toSocialAccountKey(connection.getKey()));
        }
        repository.removeConnections(providerId);
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        repository.removeConnection(connectionKey);

        interceptor.accountDeleted(toSocialAccountKey(connectionKey));
    }

    public AccountInterceptor getInterceptor() {
        return interceptor;
    }

    //TODO FIXME we build a fake user
    private SocialAccountKey toSocialAccountKey(ConnectionKey connectionKey) {
        return new SocialAccountKey(connectionKey, new User(username));
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        return repository.findAllConnections();
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return repository.findConnections(providerId);
    }

    @Override
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        return repository.findConnections(apiType);
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
        return repository.findConnectionsToUsers(providerUserIds);
    }

    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        return repository.getConnection(connectionKey);
    }

    @Override
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        return getConnection(apiType, providerUserId);
    }

    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        return repository.getPrimaryConnection(apiType);
    }

    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        return repository.getPrimaryConnection(apiType);
    }

    @Override
    public void updateConnection(Connection<?> connection) {
        repository.updateConnection(connection);
    }
}
