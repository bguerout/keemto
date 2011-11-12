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

    private static final int PAGE_OFFSET = 100;

    private final ExchangeService exchangeService;

    public ExchangeServiceWrapper(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    public List<EmailMessage> getEmailMessages(long newerThan) {
        
        ItemView offsetView = new ItemView(PAGE_OFFSET);
        List<EmailMessage> messages = new ArrayList<EmailMessage>();
        
        try {
            PartialEmailList partialEmails = null;
            do {
                partialEmails = findEmailMessages(new Date(newerThan), offsetView);
                messages.addAll(partialEmails.getFullBindedMessages());
                offsetView.setOffset(offsetView.getOffset() + PAGE_OFFSET);
            } while (partialEmails.hasMoreItems);

        } catch (Exception e) {
            throw new ExchangeServiceException("An error has occurred when trying to retrieve messages from exchange WS service " + exchangeService.getUrl(), e);
        }
        log.debug("{} messages has been retrieved.", messages.size());
        return messages;
    }

    private PartialEmailList findEmailMessages(Date newerThan, ItemView itemView) throws Exception {
        log.debug("Retrieving email ids newer than {} with view {}", newerThan, itemView);
        SearchFilter.IsGreaterThan filter = new SearchFilter.IsGreaterThan(EmailMessageSchema.DateTimeCreated, newerThan);

        FindItemsResults<Item> itemsResults = exchangeService.findItems(WellKnownFolderName.Inbox, filter, itemView);

        int leftItems = itemsResults.getTotalCount() - itemView.getOffset();
        log.debug("{} items ids has been retrieved, {} items left on remote server.", itemsResults.getItems().size(), leftItems);

        List<ItemId> ids = extractItemIdsFrom(itemsResults);
        return new PartialEmailList(ids, itemsResults.isMoreAvailable(), exchangeService);
    }

    private List<ItemId> extractItemIdsFrom(FindItemsResults<Item> itemsResults) {
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

    private static class PartialEmailList {
        private final boolean hasMoreItems;
        private final List<ItemId> ids;
        private final ExchangeService exchangeService;


        private PartialEmailList(List<ItemId> ids, boolean hasMoreItems, ExchangeService exchangeService) {
            this.ids = ids;
            this.hasMoreItems = hasMoreItems;
            this.exchangeService = exchangeService;
        }

        public List<EmailMessage> getFullBindedMessages() throws Exception {
            ServiceResponseCollection<GetItemResponse> response = exchangeService.bindToItems(ids, getPropertiesToLoadForEmailMessage());
            log.debug("email message data has been bounded remotely for {} items", ids.size());
            return convertResponseToEmailList(response);
        }

        private PropertySet getPropertiesToLoadForEmailMessage() throws Exception {
            PropertySet ps = new PropertySet();
            ps.add(EmailMessageSchema.Id);
            ps.add(EmailMessageSchema.DateTimeCreated);
            ps.add(EmailMessageSchema.Subject);
            ps.add(EmailMessageSchema.Body);
            ps.add(EmailMessageSchema.Sender);
            ps.add(EmailMessageSchema.ToRecipients);
            return ps;
        }

        private List<EmailMessage> convertResponseToEmailList(ServiceResponseCollection<GetItemResponse> bindedItems) {
            List<EmailMessage> messages = new ArrayList<EmailMessage>();
            for (GetItemResponse bindedItem : bindedItems) {
                messages.add((EmailMessage) bindedItem.getItem());
            }
            return messages;
        }

    }
}
