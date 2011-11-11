package fr.keemto.provider.exchange;

import microsoft.exchange.webservices.data.*;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assume.assumeThat;

public class ExchangeServiceWrapperIT {
    private ExchangeMailFinder finder;

    @Before
    public void setUp() throws Exception {

        String login = System.getProperty("keemto.xebia.mail.login");
        String pwd = System.getProperty("keemto.xebia.mail.pwd");
        assumeThat(login, notNullValue());

        ExchangeService exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        exchangeService.setUrl(new URI("https://owa.exchange-login.net/EWS/Exchange.asmx"));
        ExchangeCredentials credentials = new WebCredentials(login, pwd);
        exchangeService.setCredentials(credentials);

        ExchangeServiceWrapper wrapper = new ExchangeServiceWrapper(exchangeService);
        finder = new ExchangeMailFinder(wrapper);
    }

    @Test
    public void shouldReturnMailsSinceEpoch() throws Exception {
        List<Mail> mails = finder.fetch(0L);

        assertThat(mails.isEmpty(), is(false));
        assertThat(mails.size(), greaterThan(100));
        System.out.println(mails.size());
        Mail mail = mails.get(0);
        assertThat(mail.getBody(), notNullValue());
        assertThat(mail.getId(), notNullValue());
        assertThat(mail.getSender(), notNullValue());
        assertThat(mail.getSubject(), notNullValue());
        assertThat(mail.getTimestamp(), not(equalTo(0L)));
    }

}
