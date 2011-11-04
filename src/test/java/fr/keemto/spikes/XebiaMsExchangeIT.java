package fr.keemto.spikes;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import fr.keemto.core.fetcher.FetchingException;
import microsoft.exchange.webservices.data.*;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class XebiaMsExchangeIT {

    private static final int HUGE_PAGE_SIZE = 1000;
    private ExchangeService exchangeService;

    @Before
    public void setUp() throws Exception {
        exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        exchangeService.setUrl(new URI("https://owa.exchange-login.net/EWS/Exchange.asmx"));
        ExchangeCredentials credentials = new WebCredentials("", "");
        exchangeService.setCredentials(credentials);
    }

    @Test
    public void testName() throws Exception {

        Date since = new Date(System.currentTimeMillis() - 14400000);
        List<ItemId> ids = fetchItemIds(since);
        ServiceResponseCollection<GetItemResponse> bindedItems = exchangeService.bindToItems(ids, getPropertiesToLoadInItem());
        System.out.println("Total number of bindedItems found: " + bindedItems.getCount());
        for (GetItemResponse bindedItem : bindedItems) {
            Item item = bindedItem.getItem();
            System.out.println("id=" + item.getDateTimeCreated());
            System.out.println("sub=" + item.getSubject());
            System.out.println("body=\\n" + item.getBody());
        }
    }

    private List<ItemId> fetchItemIds(Date newerThan) throws Exception {
        SearchFilter.IsGreaterThan newerThanFilter = new SearchFilter.IsGreaterThan(ItemSchema.DateTimeCreated, newerThan);
        ItemView itemView = new ItemView(HUGE_PAGE_SIZE);
        FindItemsResults<Item> itemsResults = exchangeService.findItems(WellKnownFolderName.Inbox, newerThanFilter, itemView);
        return extractItemIds(itemsResults);
    }

    private PropertySet getPropertiesToLoadInItem() throws Exception {
        PropertySet ps = new PropertySet();
        ps.add(ItemSchema.Id);
        ps.add(ItemSchema.DateTimeCreated);
        ps.add(ItemSchema.Subject);
        ps.add(ItemSchema.Body);
        return ps;
    }

    private List<ItemId> extractItemIds(FindItemsResults<Item> itemsResults) {
        return Lists.transform(itemsResults.getItems(), new Function<Item, ItemId>() {
            @Override
            public ItemId apply(Item item) {
                try {
                    return item.getId();
                } catch (ServiceLocalException e) {
                    throw new FetchingException(e);
                }
            }
        });
    }
}
