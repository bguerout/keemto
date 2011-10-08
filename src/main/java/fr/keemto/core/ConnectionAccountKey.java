package fr.keemto.core;


import org.springframework.social.connect.ConnectionKey;

public class ConnectionAccountKey extends AccountKey {

    public ConnectionAccountKey(ConnectionKey key, User user) {
        super(key.getProviderId(), key.getProviderUserId(), user);
    }

}
