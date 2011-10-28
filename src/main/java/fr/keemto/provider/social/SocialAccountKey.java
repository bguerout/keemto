package fr.keemto.provider.social;


import fr.keemto.core.AccountKey;
import fr.keemto.core.User;
import org.springframework.social.connect.ConnectionKey;

public class SocialAccountKey extends AccountKey {

    public SocialAccountKey(ConnectionKey key, User user) {
        super(key.getProviderId(), key.getProviderUserId(), user);
    }

}
