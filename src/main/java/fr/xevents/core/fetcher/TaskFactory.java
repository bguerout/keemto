package fr.xevents.core.fetcher;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

@Component
public class TaskFactory {

    private static final Logger log = LoggerFactory.getLogger(TaskFactory.class);

    private final EventRepository eventRepository;

    private final FetcherResolver fetcherResolver;

    @Autowired
    public TaskFactory(EventRepository eventRepository, FetcherResolver fetcherResolver) {
        this.eventRepository = eventRepository;
        this.fetcherResolver = fetcherResolver;

    }

    private EventUpdateTask createTask(Fetcher fetcher, User user) {
        return new EventUpdateTask(fetcher, user, eventRepository);
    }

    public List<EventUpdateTask> createTasks(User user) {

        List<EventUpdateTask> tasks = new ArrayList<EventUpdateTask>();
        for (Fetcher fetcher : fetcherResolver.resolve(user)) {
            EventUpdateTask task = createTask(fetcher, user);
            tasks.add(task);
        }
        return tasks;
    }

}