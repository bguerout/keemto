package fr.keemto.provider.exchange;

import microsoft.exchange.webservices.data.ExchangeService;

public class ExchangeServiceFactory {

    private ExchangeService exchangeService;

    public ExchangeServiceFactory(ExchangeService emailExchangeService) {
        this.exchangeService = emailExchangeService;
    }

    public EmailExchangeService createEmailExchange(long newerThanSelector) {
        return new EmailExchangeService(newerThanSelector, exchangeService);

    }
}
