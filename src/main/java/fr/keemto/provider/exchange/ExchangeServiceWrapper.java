package fr.keemto.provider.exchange;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import fr.keemto.core.fetcher.FetchingException;
import microsoft.exchange.webservices.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExchangeServiceWrapper {

    private static final Logger log = LoggerFactory.getLogger(ExchangeServiceWrapper.class);

    private static final int PAGE_SIZE = 100;

    private final ExchangeService exchangeService;

    public ExchangeServiceWrapper(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    public List<Item> getItems(long newerThan) {
        Date since = new Date(newerThan);
        ItemView pagedView = new ItemView(PAGE_SIZE);
        return retrieveItemsByPaging(since, pagedView);
    }

    private List<Item> retrieveItemsByPaging(Date since, ItemView pagedView) {
        List<Item> items = new ArrayList<Item>();
        try {
            PartialItemList partialItemList = null;
            do {
                partialItemList = fetchItemIds(since, pagedView);
                items.addAll(partialItemList.getFullBindedItems());
                pagedView.setOffset(pagedView.getOffset() + PAGE_SIZE);
            } while (partialItemList.hasMoreItems);

        } catch (Exception e) {
            throw new ExchangeServiceException("An error has occurred when trying to retrieve items from exchange WS service " + exchangeService.getUrl(), e);
        }
        log.debug("{} items has been fetched.", items.size());
        return items;
    }

    private PartialItemList fetchItemIds(Date newerThan, ItemView itemView) throws Exception {
        log.debug("Fetching items ids newer than {} with view {}", newerThan, itemView);
        SearchFilter.IsGreaterThan newerThanFilter = new SearchFilter.IsGreaterThan(ItemSchema.DateTimeCreated, newerThan);
        FindItemsResults<Item> itemsResults = exchangeService.findItems(WellKnownFolderName.Inbox, newerThanFilter, itemView);
        int leftItems = itemsResults.getTotalCount() - itemView.getOffset();
        log.debug("{} items ids has been fetched, {} items left on remote server.", itemsResults.getItems().size(), leftItems);
        return new PartialItemList(extractItemIds(itemsResults), itemsResults.isMoreAvailable(), exchangeService);
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

    private static class PartialItemList {
        private final boolean hasMoreItems;
        private final List<ItemId> ids;
        private final ExchangeService exchangeService;


        private PartialItemList(List<ItemId> ids, boolean hasMoreItems, ExchangeService exchangeService) {
            this.ids = ids;
            this.hasMoreItems = hasMoreItems;
            this.exchangeService = exchangeService;
        }

        public List<Item> getFullBindedItems() throws Exception {
            ServiceResponseCollection<GetItemResponse> response = exchangeService.bindToItems(ids, getPropertiesToLoadInItem());
            log.debug("items data has been bounded remotely for {} items", ids.size());
            return toItemList(response);
        }

        private PropertySet getPropertiesToLoadInItem() throws Exception {
            PropertySet ps = new PropertySet();
            ps.add(ItemSchema.Id);
            ps.add(ItemSchema.DateTimeCreated);
            ps.add(ItemSchema.Subject);
            ps.add(ItemSchema.Body);
            return ps;
        }

        private List<Item> toItemList(ServiceResponseCollection<GetItemResponse> bindedItems) {
            List<Item> items = new ArrayList<Item>();
            for (GetItemResponse bindedItem : bindedItems) {
                items.add(bindedItem.getItem());
            }
            return items;
        }

    }
}
