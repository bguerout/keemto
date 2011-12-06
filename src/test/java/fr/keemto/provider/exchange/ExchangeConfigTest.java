package fr.keemto.provider.exchange;

import fr.keemto.config.CoreConfig;
import fr.keemto.config.ProviderConfig;
import fr.keemto.provider.exchange.importer.MailFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, ProviderConfig.class, ExchangeConfig.class}, loader = AnnotationConfigContextLoader.class)
public class ExchangeConfigTest {

    @Test
    public void whenNoLoginSystemPropertyShouldSwitchToMockedService() throws Exception {
        ExchangeConfig exchangeConfig = new ExchangeConfig();

        MailFinder finder = exchangeConfig.mailFinder(new URI("file:/test"), null, null);

        List<Email> emails = finder.findEmails(0);
        assertThat(emails.size(), equalTo(0));
    }
}
