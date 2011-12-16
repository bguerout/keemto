package fr.keemto.provider.exchange;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import fr.keemto.core.Account;
import fr.keemto.core.AccountKey;
import fr.keemto.core.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExchangeAccount implements Account {

    private static final Logger log = LoggerFactory.getLogger(ExchangeAccount.class);

    private final AccountKey key;
    private final List<String> allowedRecipients;
    private final MailRepository mailRepository;

    public ExchangeAccount(AccountKey key, List<String> allowedRecipients, MailRepository mailRepository) {
        this.key = key;
        this.allowedRecipients = allowedRecipients;
        this.mailRepository = mailRepository;
    }

    public ExchangeAccount(AccountKey key, MailRepository mailRepository) {
        this(key, new ArrayList<String>(), mailRepository);
    }

    @Override
    public List<Event> fetch(long newerThan) {
        String email = key.getProviderUserId();
        log.debug("Fetching emails newer than {} for user {}", newerThan, key);
        List<Email> emails = mailRepository.getMails(email, newerThan);
        Collection<Email> filteredEmails = removeEmailWithNotAllowedRecipients(emails);
        List<Event> events = convertEmailToEvent(filteredEmails, this);
        log.debug("{} emails has been fetched for user {}", events.size(), key);
        return events;
    }

    private List<Event> convertEmailToEvent(Collection<Email> filteredEmails, final Account account) {
        return Lists.transform(new ArrayList<Email>(filteredEmails), new Function<Email, Event>() {
            @Override
            public Event apply(Email email) {
                return new Event(email.getTimestamp(), email.getBodyAsHtmlFragment(), account);
            }
        });
    }

    private Collection<Email> removeEmailWithNotAllowedRecipients(List<Email> emails) {
        if (allowedRecipients.isEmpty()) return emails;

        return Collections2.filter(emails, new Predicate<Email>() {
            @Override
            public boolean apply(Email email) {
                for (String allowedRecipient : allowedRecipients) {
                    if (email.getRecipientsAsString().contains(allowedRecipient)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void revoke() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AccountKey getKey() {
        return key;
    }

    @Override
    public String getDisplayName() {
        return key.getProviderUserId();
    }

    @Override
    public String getProfileUrl() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return "http://www.gravatar.com/avatar";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeAccount)) return false;

        ExchangeAccount that = (ExchangeAccount) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MailAccount{" +
                "key=" + key +
                '}';
    }
}
