package fr.keemto.provider.yammer;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import fr.keemto.provider.social.EventData;
import fr.keemto.provider.social.ConnectionFetcher;
import org.springframework.social.yammer.api.MessageInfo;
import org.springframework.social.yammer.api.Yammer;
import org.springframework.social.yammer.api.YammerMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class YammerFetcher extends ConnectionFetcher<Yammer, YammerMessage> {

    public YammerFetcher(long delay) {
        super(delay);
    }

    public YammerFetcher() {
        this(60000);
    }

    /*
    * TODO we fetch all messages because yammer REST API cannot filter messages by date, this API allows filtering by id (eg. get messages older than an id)
     */
    @Override
    protected List<YammerMessage> fetchApi(Yammer api, long lastFetchedEventTime) {

        MessageInfo messageInfo = api.messageOperations().getMessagesSent(0, 0, null, 0);
        List<YammerMessage> messages = messageInfo.getMessages();
        Collection<YammerMessage> filteredYammerMessages = removeAlreadyFetchedMessages(messages, lastFetchedEventTime);
        return new ArrayList<YammerMessage>(filteredYammerMessages);
    }

    private Collection<YammerMessage> removeAlreadyFetchedMessages(List<YammerMessage> messages, final long lastFetchedEventTime) {
        return Collections2.filter(messages, new Predicate<YammerMessage>() {

            @Override
            public boolean apply(YammerMessage message) {
                Date messageDate = message.getCreatedAt();
                return messageDate.after(new Date(lastFetchedEventTime));
            }

        });
    }

    @Override
    protected EventData convertDataToEvent(YammerMessage message) {
        YammerMessage.Body body = message.getBody();
        String messageContent = body.getPlain();
        long messageCreationTime = message.getCreatedAt().getTime();
        return new EventData(messageCreationTime, messageContent, getProviderId());
    }


    @Override
    public String getProviderId() {
        return "yammer";
    }
}
