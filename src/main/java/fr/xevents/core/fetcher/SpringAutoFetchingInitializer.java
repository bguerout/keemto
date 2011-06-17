package fr.xevents.core.fetcher;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import fr.xevents.core.User;
import fr.xevents.core.UserResolver;

@Component
public class SpringAutoFetchingInitializer implements InitializingBean {

    private final UserResolver userResolver;
    private final FetcherHandlerFactory handlerFactory;
    private final FetchingRegistrar registrar;

    @Inject
    public SpringAutoFetchingInitializer(UserResolver userResolver, FetcherHandlerFactory handlerFactory,
            FetchingRegistrar registrar) {
        this.userResolver = userResolver;
        this.handlerFactory = handlerFactory;
        this.registrar = registrar;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerAllHandlers();
    }

    private void registerAllHandlers() {
        for (User user : userResolver.getAllUsers()) {
            List<FetcherHandler> handlers = handlerFactory.createHandlers(user);
            registrar.registerHandlers(handlers);
        }
    }
}
