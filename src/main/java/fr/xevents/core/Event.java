package fr.xevents.core;

import com.google.common.base.Objects;

public class Event {

    private final long timestamp;

    private final String user;

    private final String message;

    private final String providerId;

    public Event(long timestamp, String owner, String message, String providerId) {
        super();
        this.timestamp = timestamp;
        this.user = owner;
        this.message = message;
        this.providerId = providerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getProviderId() {
        return providerId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timestamp, user, providerId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Event) {
            Event other = (Event) o;
            return Objects.equal(other.timestamp, timestamp) && Objects.equal(other.user, user)
                    && Objects.equal(other.providerId, providerId);
        }
        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("timestamp", timestamp).add("owner", user).add("message", message)
                .toString();
    }

}
