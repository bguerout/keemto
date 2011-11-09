package fr.keemto.provider.exchange;

import microsoft.exchange.webservices.data.*;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assume.assumeThat;

public class ExchangeServiceWrapperIT {
    private ExchangeService exchangeService;
    private ExchangeServiceWrapper wrapper;

    @Before
    public void setUp() throws Exception {

        String login = System.getProperty("keemto.xebia.mail.login");
        String pwd = System.getProperty("keemto.xebia.mail.pwd");
        assumeThat(login, notNullValue());

        exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        exchangeService.setUrl(new URI("https://owa.exchange-login.net/EWS/Exchange.asmx"));
        ExchangeCredentials credentials = new WebCredentials(login, pwd);
        exchangeService.setCredentials(credentials);

        wrapper = new ExchangeServiceWrapper(exchangeService);
    }

    @Test
    public void shouldReturnMailsSinceEpoch() throws Exception {
        List<Item> items = wrapper.getItems(0L);

        assertThat(items.isEmpty(), is(false));
        assertThat(items.size(), greaterThan(100));
        System.out.println(items.size());
        Item item = items.get(0);
        assertThat(item.getBody(), notNullValue());
        assertThat(item.getSubject(), notNullValue());
        assertThat(item.getDateTimeCreated(), notNullValue());
    }

}
