package fr.keemto.provider.exchange;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import microsoft.exchange.webservices.data.*;

import java.util.Date;
import java.util.List;

public class ExchangeMailFinder {
    private ExchangeServiceWrapper exchangeServiceWrapper;

    public ExchangeMailFinder(ExchangeServiceWrapper exchangeServiceWrapper) {
        this.exchangeServiceWrapper = exchangeServiceWrapper;
    }

    public List<Mail> fetch(long newerThan) {
        List<EmailMessage> items = exchangeServiceWrapper.getItems(newerThan);
        return transform(items);
    }

    private List<Mail> transform(List<EmailMessage> items) {
        return Lists.transform(items, new Function<EmailMessage, Mail>() {
            @Override
            public Mail apply(EmailMessage message) {
                return toMail(message);
            }
        });
    }

    private Mail toMail(EmailMessage message) {
        try {
            String uniqueId = message.getId().getUniqueId();
            MessageBody body = message.getBody();
            String sender = message.getSender().getAddress();
            Date dateTimeCreated = message.getDateTimeCreated();
            return new Mail(uniqueId, sender, message.getSubject(), body.toString(), dateTimeCreated.getTime());
        } catch (ServiceLocalException e) {
            throw new ExchangeServiceException("Unable to create mail from item:" + message, e);
        }
    }

}
