package fr.keemto.provider.exchange;

import java.util.List;

public class MailImporterTask implements Runnable {
    private final ExchangeMailFinder exchangeMailFinder;
    private final JdbcMailRepository mailRepository;

    public MailImporterTask(ExchangeMailFinder exchangeMailFinder, JdbcMailRepository mailRepository) {
        this.exchangeMailFinder = exchangeMailFinder;
        this.mailRepository = mailRepository;
    }

    public void importMailsNewerThan(long timestamp) {
        List<Mail> mails = exchangeMailFinder.fetch(timestamp);
        mailRepository.persist(mails);
    }

    @Override
    public void run() {
        long mostRecent = mailRepository.getMostRecentMailTime();
        importMailsNewerThan(mostRecent);
    }
}
