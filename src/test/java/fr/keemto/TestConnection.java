package fr.keemto;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.AbstractConnection;


public class TestConnection extends AbstractConnection {

    public TestConnection(String providerId, String providerUserId) {
        super(new ConnectionData(providerId, providerUserId, "displayName",
                "profileUrl", "imageUrl", "accessToken", "secret", "refreshToken", (long) 999), new NullApiAdapter());
    }


    @Override
    public ConnectionData createData() {
        return null;
    }

    @Override
    public Object getApi() {
        return null;
    }

    public static class NullApiAdapter implements ApiAdapter<Object> {


        public NullApiAdapter() {
        }

        public boolean test(Object api) {
            return true;
        }

        public void setConnectionValues(Object api, ConnectionValues values) {

        }

        public UserProfile fetchUserProfile(Object api) {
            return UserProfile.EMPTY;
        }

        public void updateStatus(Object api, String message) {
        }

    }
}
