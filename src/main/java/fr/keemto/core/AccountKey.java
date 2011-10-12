package fr.keemto.core;


public class AccountKey {

    private String providerId;
    private String providerUserId;
    private final User user;

    public AccountKey(String providerId, String providerUserId, User user) {
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.user = user;
    }

    public String getId() {
        return getProviderId() + "-" + getProviderUserId();
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }


    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof AccountKey)) return false;

        AccountKey that = (AccountKey) o;

        if (providerId != null ? !providerId.equals(that.providerId) : that.providerId != null) return false;
        if (providerUserId != null ? !providerUserId.equals(that.providerUserId) : that.providerUserId != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = providerId != null ? providerId.hashCode() : 0;
        result = 31 * result + (providerUserId != null ? providerUserId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountKey{" +
                "providerId='" + providerId + '\'' +
                ", providerUserId='" + providerUserId + '\'' +
                ", username=" + user.getUsername() +
                '}';
    }
}
