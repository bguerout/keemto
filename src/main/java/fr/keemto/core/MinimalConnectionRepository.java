package fr.keemto.core;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;

public class MinimalConnectionRepository {

    private final ConnectionRepository connectionRepository;

    public MinimalConnectionRepository(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }


    public void revoke(ConnectionKey key) {
        connectionRepository.removeConnection(key);
    }
}
