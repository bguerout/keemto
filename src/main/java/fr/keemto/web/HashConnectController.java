package fr.keemto.web;

import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

public class HashConnectController extends ConnectController {

    public HashConnectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        super(connectionFactoryLocator, connectionRepository);
    }

    @RequestMapping(value = "/{providerId}", method = RequestMethod.POST, params = "oauth_verifier")
    public RedirectView oauth1ManualCallback(@PathVariable String providerId, NativeWebRequest request) {
        return oauth1Callback(providerId, request);
    }

    @Override
    protected String connectView() {
        return "/#accounts";
    }

    @Override
    protected String connectView(String providerId) {
        return "/#accounts";
    }

    @Override
    protected String connectedView(String providerId) {
        return "/#accounts";
    }

    @Override
    protected RedirectView connectionStatusRedirect(String providerId) {
        return new RedirectView("/#accounts", true);
    }
}
