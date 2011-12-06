package fr.keemto.core.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

public class TypedCollectionFactoryBean<T> extends AbstractFactoryBean<Collection<T>> implements ApplicationContextAware{

    private Class<T> beanType;

    private ApplicationContext applicationContext;

    @Required
    public void setBeanType(Class<T> beanType) {
        this.beanType = beanType;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected Collection<T> createInstance() throws Exception {
        return applicationContext.getBeansOfType(beanType).values();
    }

    @Override
    public Class<?> getObjectType() {
        return Collection.class;
    }
}
