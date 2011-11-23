package fr.keemto.provider.exchange;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

public class Email {

    private final String id;
    private final String subject;
    private final String from;
    private final String body;
    private final long timestamp;
    private final List<String> recipients;

    public Email(String id, String from, String subject, String body, long createdAt, List<String> recipients) {
        this.id = id;
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.timestamp = createdAt;
        this.recipients = recipients;
    }

    public Email(String id, String from, String subject, String body, long createdAt, String recipients) {
        this(id, from, subject, body, createdAt, Arrays.asList(StringUtils.split(recipients, ",")));
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
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

    public List<String> getRecipients() {
        return recipients;
    }

    public String getRecipientsAsString() {
        return StringUtils.join(recipients.toArray(), ",");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;

        Email email = (Email) o;

        if (timestamp != email.timestamp) return false;
        if (body != null ? !body.equals(email.body) : email.body != null) return false;
        if (from != null ? !from.equals(email.from) : email.from != null) return false;
        if (id != null ? !id.equals(email.id) : email.id != null) return false;
        if (recipients != null ? !recipients.equals(email.recipients) : email.recipients != null) return false;
        if (subject != null ? !subject.equals(email.subject) : email.subject != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (recipients != null ? recipients.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Mail{" +
                ", id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }


}
