package fr.keemto.provider.exchange.importer;

import fr.keemto.provider.exchange.JdbcMailRepository;
import fr.keemto.provider.exchange.Mail;

import java.util.List;

public class MailImporterTask implements Runnable {

    private final ExchangeMailFinder exchangeMailFinder;
    private final JdbcMailRepository mailRepository;

    public MailImporterTask(ExchangeMailFinder exchangeMailFinder, JdbcMailRepository mailRepository) {
        this.exchangeMailFinder = exchangeMailFinder;
        this.mailRepository = mailRepository;
    }

    public void importMailsNewerThan(long timestamp) {
        List<Mail> mails = exchangeMailFinder.findEmails(timestamp);
        mailRepository.persist(mails);
    }

    @Override
    public void run() {
        long mostRecent = mailRepository.getMostRecentMailTime();
        importMailsNewerThan(mostRecent);
    }
}