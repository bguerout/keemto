package fr.keemto.core;


import org.springframework.social.connect.ConnectionKey;

public class AccountKeyAdapter extends AccountKey {

    public AccountKeyAdapter(ConnectionKey key) {
        super(key.getProviderId(), key.getProviderUserId());
    }

}
