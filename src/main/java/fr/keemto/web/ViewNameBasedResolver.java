package fr.keemto.web;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import java.util.Locale;

public class ViewNameBasedResolver implements ViewResolver, Ordered {

    private ViewResolver defaultResolver;
    private String jsonView = "MappingJacksonJsonView";

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName.equals(jsonView))
            return new MappingJacksonJsonView();

        return defaultResolver.resolveViewName(viewName, locale);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public void setDefaultResolver(ViewResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    public void setJsonView(String jsonView) {
        this.jsonView = jsonView;
    }
}
