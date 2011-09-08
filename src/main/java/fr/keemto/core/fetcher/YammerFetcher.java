package fr.keemto.core.fetcher;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import fr.keemto.core.Event;
import fr.keemto.core.User;
import org.springframework.social.yammer.api.MessageOperations;
import org.springframework.social.yammer.api.impl.MessageInfo;
import org.springframework.social.yammer.api.impl.YammerMessage;
import org.springframework.social.yammer.api.impl.YammerTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class YammerFetcher extends SocialFetcher<YammerTemplate> {

    public YammerFetcher(ApiResolver<YammerTemplate> apiResolver, long delay) {
        super(apiResolver, delay);
    }

    public YammerFetcher(ApiResolver<YammerTemplate> apiResolver) {
        this(apiResolver, 60000);
    }

    @Override
    protected List<Event> fetchApiEvents(YammerTemplate api, long lastFetchedEventTime, User user) {

        MessageOperations messageOperations = api.messageOperations();
        //TODO we fetch all messages because yammer REST API cannot filter messages by date, this API allows filtering by id (eg. get messages older than an id)
        MessageInfo messageInfo = messageOperations.getMessagesSent(0, 0, null, 0);

        List<Event> events = new ArrayList<Event>();
        List<YammerMessage> messages = messageInfo.getMessages();
        Collection<YammerMessage> messagesFilteredByDate = filterMessagesByDate(messages, lastFetchedEventTime);
        for (YammerMessage message : messagesFilteredByDate) {
            Event event = convertMessageToEvent(message, user);
            events.add(event);
        }
        return events;
    }

    private Collection<YammerMessage> filterMessagesByDate(List<YammerMessage> messages, final long lastFetchedEventTime) {
        return Collections2.filter(messages, new Predicate<YammerMessage>() {

            @Override
            public boolean apply(YammerMessage message) {
                Date messageDate = message.getCreatedAt();
                return messageDate.after(new Date(lastFetchedEventTime));
            }

        });
    }

    private Event convertMessageToEvent(YammerMessage message, User user) {
        YammerMessage.Body body = message.getBody();
        String messageContent = body.getPlain();
        long messageCreationTime = message.getCreatedAt().getTime();
        return new Event(messageCreationTime, user.getUsername(), messageContent, getProviderId());
    }


    @Override
    public String getProviderId() {
        return "yammer";
    }
}
