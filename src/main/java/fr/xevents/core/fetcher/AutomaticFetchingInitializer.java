package fr.xevents.core.fetcher;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

@Component
public class AutomaticFetchingInitializer implements InitializingBean {

    private UserResolver userResolver;
    private TaskFactory taskFactory;
    private FetchingRegistrar registrar;
    private FetcherResolver fetcherResolver;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Fetcher> fetchers = fetcherResolver.resolveAll();
        registerAllTasks(fetchers);
    }

    protected void registerAllTasks(List<Fetcher> fetchers) {
        for (User user : userResolver.getAllUsers()) {
            List<EventTask> tasks = taskFactory.createTasks(user);
            registrar.registerTasks(tasks);
        }
    }

    @Autowired
    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    @Autowired
    public void setEventTaskFactory(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    @Autowired
    public void setRegistrar(FetchingRegistrar registrar) {
        this.registrar = registrar;
    }

    @Autowired
    public void setFetcherResolver(FetcherResolver fetcherResolver) {
        this.fetcherResolver = fetcherResolver;
    }

}
