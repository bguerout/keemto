package fr.keemto.provider.exchange.importer;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import fr.keemto.provider.exchange.Mail;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.ServiceLocalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MailFinder {

    private static final Logger log = LoggerFactory.getLogger(MailFinder.class);

    private ExchangeServiceFactory exchangeServiceFactory;

    public MailFinder(ExchangeServiceFactory exchangeServiceFactory) {
        this.exchangeServiceFactory = exchangeServiceFactory;
    }

    public List<Mail> findEmails(long newerThan) {
        EmailExchangeService emailExchange = exchangeServiceFactory.createServiceWithTimeSelector(newerThan);
        List<EmailMessage> messages = new ArrayList<EmailMessage>();
        do {
            List<EmailMessage> items = emailExchange.nextElement();
            messages.addAll(items);
        } while (emailExchange.hasMoreElements());

        log.debug("{} messages has been retrieved.", messages.size());
        return transform(messages);
    }

    private List<Mail> transform(List<EmailMessage> items) {
        return Lists.transform(items, new Function<EmailMessage, Mail>() {
            @Override
            public Mail apply(EmailMessage message) {
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

    private Mail toMail(EmailMessage message) {
        try {
            String uniqueId = message.getId().getUniqueId();
            MessageBody body = message.getBody();
            String sender = message.getSender().getAddress();
            Date dateTimeCreated = message.getDateTimeCreated();
            List<String> recipients = asRecipientsList(message.getToRecipients().iterator());
            return new Mail(uniqueId, sender, message.getSubject(), body.toString(), dateTimeCreated.getTime(), recipients);
        } catch (ServiceLocalException e) {
            throw new ExchangeServiceException("Unable to create mail from item:" + message, e);
        }
    }

}
