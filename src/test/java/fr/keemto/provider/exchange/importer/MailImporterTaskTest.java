package fr.keemto.provider.exchange.importer;

import com.google.common.collect.Lists;
import fr.keemto.provider.exchange.JdbcMailRepository;
import fr.keemto.provider.exchange.Mail;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MailImporterTaskTest {

    private MailImporterTask task;
    private MailFinder finder;
    private JdbcMailRepository mailRepository;

    @Before
    public void prepare() throws Exception {
        finder = mock(MailFinder.class);
        mailRepository = mock(JdbcMailRepository.class);
        task = new MailImporterTask(finder, mailRepository);
    }

    @Test
    public void shouldObtainMailFromFetcher() throws Exception {

        task.importMailsNewerThan(20L);

        verify(finder).findEmails(20L);
    }

    @Test
    public void shouldPersistFetchedMails() throws Exception {

        Mail mail = mock(Mail.class);
        when(finder.findEmails(20L)).thenReturn(Lists.newArrayList(mail));

        task.importMailsNewerThan(20L);

        verify(mailRepository).persist(Lists.newArrayList(mail));
    }

    @Test
    public void onRunShouldFetchAndPersistMostRecentMails() throws Exception {

        Mail mail = mock(Mail.class);
        when(finder.findEmails(999)).thenReturn(Lists.newArrayList(mail));
        when(mailRepository.getMostRecentMailTime()).thenReturn(999L);

        task.run();

        verify(finder).findEmails(999);
        verify(mailRepository).persist(Lists.newArrayList(mail));
    }
}
