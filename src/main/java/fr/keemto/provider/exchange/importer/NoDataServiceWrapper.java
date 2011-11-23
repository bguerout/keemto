package fr.keemto.provider.exchange.importer;

import microsoft.exchange.webservices.data.EmailMessage;

import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

public class NoDataServiceWrapper extends ExchangeServiceWrapper {

    public NoDataServiceWrapper() {
        super(null);
    }

    @Override
    public Enumeration<List<EmailMessage>> getEmailsNewerThan(long newerThanSelector) {
        return new EmptyEnumerator();
    }

    private static class EmptyEnumerator implements Enumeration<List<EmailMessage>> {


        public boolean hasMoreElements() {
            return false;
        }

        public List<EmailMessage> nextElement() {
            throw new NoSuchElementException();
        }
    }
}
