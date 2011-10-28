package fr.keemto.provider.social;

public class EventData {
    private final long timestamp;
    private final String message;
    private final String providerId;


    public EventData(long timestamp, String message, String providerId) {
        this.timestamp = timestamp;
        this.message = message;
        this.providerId = providerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getProviderId() {
        return providerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventData eventData = (EventData) o;

        if (timestamp != eventData.timestamp) return false;
        if (message != null ? !message.equals(eventData.message) : eventData.message != null) return false;
        if (providerId != null ? !providerId.equals(eventData.providerId) : eventData.providerId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
