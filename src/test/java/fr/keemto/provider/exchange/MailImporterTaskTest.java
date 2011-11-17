package fr.keemto.provider.exchange;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MailImporterTaskTest {

    private MailImporterTask task;
    private ExchangeMailFinder finder;
    private JdbcMailRepository mailRepository;

    @Before
    public void prepare() throws Exception {
        finder = mock(ExchangeMailFinder.class);
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

        Mail mail = new Mail("id", "stnevex@gmail.com", "subject", "body", System.currentTimeMillis());
        when(finder.findEmails(20L)).thenReturn(Lists.newArrayList(mail));

        task.importMailsNewerThan(20L);

        verify(mailRepository).persist(Lists.newArrayList(mail));
    }

    @Test
    public void onRunShouldFetchAndPersistMostRecentMails() throws Exception {

        Mail newMail = new Mail("id2", "stnevex@gmail.com", "new subject", "body", System.currentTimeMillis());
        when(finder.findEmails(999)).thenReturn(Lists.newArrayList(newMail));
        when(mailRepository.getMostRecentMailTime()).thenReturn(999L);

        task.run();

        verify(finder).findEmails(999);
        verify(mailRepository).persist(Lists.newArrayList(newMail));
    }
}
