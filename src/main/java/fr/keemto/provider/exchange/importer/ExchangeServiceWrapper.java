package fr.keemto.provider.exchange.importer;

import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeService;

import java.util.Enumeration;
import java.util.List;

public class ExchangeServiceWrapper {

    private ExchangeService exchangeService;

    public ExchangeServiceWrapper(ExchangeService emailExchangeService) {
        this.exchangeService = emailExchangeService;
    }

    public Enumeration<List<EmailMessage>> getEmailsNewerThan(long newerThanSelector) {
        return new EmailExchangeService(newerThanSelector, exchangeService);

    }
}
