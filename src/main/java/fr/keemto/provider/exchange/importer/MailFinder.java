package fr.keemto.provider.exchange.importer;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import fr.keemto.provider.exchange.Email;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.ServiceLocalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MailFinder {

    private static final Logger log = LoggerFactory.getLogger(MailFinder.class);

    private ExchangeServiceWrapper exchangeServiceWrapper;

    public MailFinder(ExchangeServiceWrapper exchangeServiceWrapper) {
        this.exchangeServiceWrapper = exchangeServiceWrapper;
    }

    public List<Email> findEmails(long newerThan) {
        Enumeration<List<EmailMessage>> emails = exchangeServiceWrapper.getEmailsNewerThan(newerThan);
        List<EmailMessage> messages = new ArrayList<EmailMessage>();
        while (emails.hasMoreElements()) {
            List<EmailMessage> items = emails.nextElement();
            messages.addAll(items);
        }

        log.debug("{} email newer than {} have been found", messages.size(), newerThan);
        return transform(messages);
    }

    private List<Email> transform(List<EmailMessage> items) {
        return Lists.transform(items, new Function<EmailMessage, Email>() {
            @Override
            public Email apply(EmailMessage message) {
                return toMail(message);
            }
        });
    }

    private List<String> asRecipientsList(Iterator<EmailAddress> emailAddresses) {
        List<String> recipients = new ArrayList<String>();
        while (emailAddresses.hasNext()) {
            EmailAddress address = emailAddresses.next();
            recipients.add(address.getAddress());
        }
        return recipients;
    }

    private Email toMail(EmailMessage message) {
        try {
            String uniqueId = message.getId().getUniqueId();
            MessageBody body = message.getBody();
            String sender = message.getSender().getAddress();
            Date dateTimeCreated = message.getDateTimeCreated();
            List<String> recipients = asRecipientsList(message.getToRecipients().iterator());
            return new Email(uniqueId, sender, message.getSubject(), body.toString(), dateTimeCreated.getTime(), recipients);
        } catch (ServiceLocalException e) {
            throw new ExchangeServiceException("Unable to create mail from item:" + message, e);
        }
    }

}
