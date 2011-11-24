package fr.keemto.provider.exchange.importer;

import fr.keemto.core.Task;
import fr.keemto.provider.exchange.Email;
import fr.keemto.provider.exchange.MailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MailImporterTask implements Task {

    private static final Logger log = LoggerFactory.getLogger(MailImporterTask.class);

    private final MailFinder mailFinder;
    private final MailRepository mailRepository;

    public MailImporterTask(MailFinder mailFinder, MailRepository mailRepository) {
        this.mailFinder = mailFinder;
        this.mailRepository = mailRepository;
    }

    public void importMailsNewerThan(long timestamp) {
        List<Email> emails = mailFinder.findEmails(timestamp);
        mailRepository.persist(emails);
        log.debug("{} emails has been imported", emails.size());
    }

    @Override
    public void run() {
        executeNextImportIncrement();
    }

    private void executeNextImportIncrement() {
        long mostRecent = mailRepository.getMostRecentMailCreationTime();
        log.debug("Running incremental mail import task for mail newer than {}", mostRecent);
        importMailsNewerThan(mostRecent);
    }

    @Override
    public long getDelay() {
        return 60000;
    }

    @Override
    public Object getTaskId() {
        return "exchange";
    }


}
