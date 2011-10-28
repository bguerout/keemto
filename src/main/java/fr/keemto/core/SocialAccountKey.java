package fr.keemto.core;


import org.springframework.social.connect.ConnectionKey;

public class SocialAccountKey extends AccountKey {

    public SocialAccountKey(ConnectionKey key, User user) {
        super(key.getProviderId(), key.getProviderUserId(), user);
    }

}
