package fr.xevents.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

public class ApiResolver<T> {

    private final UsersConnectionRepository usersConnectionRepository;

    private final Class<T> apiClass;

    public ApiResolver(Class<T> clazz, UsersConnectionRepository usersConnectionRepository) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.apiClass = clazz;
    }

    public List<T> getApis(User user) {
        List<T> apis = new ArrayList<T>();
        for (Connection<T> connection : getConnectionsFor(user)) {
            apis.add(connection.getApi());
        }
        return apis;
    }

    private List<Connection<T>> getConnectionsFor(User user) {
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(user
                .getUsername());
        return connectionRepository.findConnectionsToApi(apiClass);
    }

    Class<T> getApiClass() {
        return apiClass;
    }

}
