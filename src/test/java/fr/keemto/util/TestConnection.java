package fr.keemto.util;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;


public class TestConnection extends NullConnection {

    public TestConnection(String providerId, String providerUserId) {
        super(new ConnectionData(providerId, providerUserId, "displayName",
                "profileUrl", "imageUrl", "accessToken", "secret", "refreshToken", (long) 999));
    }


}
