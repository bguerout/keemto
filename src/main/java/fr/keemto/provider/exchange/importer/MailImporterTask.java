package fr.keemto.provider.exchange.importer;

import fr.keemto.core.Task;
import fr.keemto.provider.exchange.Mail;
import fr.keemto.provider.exchange.MailRepository;

import java.util.List;

public class MailImporterTask implements Task {

    private final ExchangeMailFinder exchangeMailFinder;
    private final MailRepository mailRepository;

    public MailImporterTask(ExchangeMailFinder exchangeMailFinder, MailRepository mailRepository) {
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

    @Override
    public long getDelay() {
        return 60;
    }

    @Override
    public Object getTaskId() {
        return "exchange";
    }


}
