package fr.keemto.core;


public class AccountKey {

    private String providerId;
    private String providerUserId;

    public AccountKey(String providerId, String providerUserId) {
        this.providerId = providerId;
        this.providerUserId = providerUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountKey that = (AccountKey) o;

        if (providerId != null ? !providerId.equals(that.providerId) : that.providerId != null) {
            return false;
        }
        if (providerUserId != null ? !providerUserId.equals(that.providerUserId) : that.providerUserId != null) {
            return false;
        }

        return true;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    @Override
    public int hashCode() {
        int result = providerId != null ? providerId.hashCode() : 0;
        result = 31 * result + (providerUserId != null ? providerUserId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountKey{" +
                "providerId='" + providerId + '\'' +
                ", providerUserId='" + providerUserId + '\'' +
                '}';
    }
}
