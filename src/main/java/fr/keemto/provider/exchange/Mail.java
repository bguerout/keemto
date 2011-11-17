package fr.keemto.provider.exchange;

public class Mail {

    private final String id;
    private final String subject;
    private final String sender;
    private final String body;
    private final long timestamp;

    public Mail(String id, String sender, String subject, String body, long timestamp) {
        this.id = id;
        this.sender = sender;
        this.subject = subject;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mail)) return false;

        Mail mail = (Mail) o;

        if (timestamp != mail.timestamp) return false;
        if (body != null ? !body.equals(mail.body) : mail.body != null) return false;
        if (id != null ? !id.equals(mail.id) : mail.id != null) return false;
        if (sender != null ? !sender.equals(mail.sender) : mail.sender != null) return false;
        if (subject != null ? !subject.equals(mail.subject) : mail.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Mail{" +
                ", id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", sender='" + sender + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
