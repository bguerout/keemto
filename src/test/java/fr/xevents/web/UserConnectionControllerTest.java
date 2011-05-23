package fr.xevents.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

public class UserConnectionControllerTest extends ControllerTestCase {

    @Mock
    private ConnectionRepository repository;
    private UserConnectionController controller;

    @Before
    public void initBeforeTest() throws Exception {
        initMocks(this);

        controller = new UserConnectionController(repository);

        request.setMethod("GET");
        request.setRequestURI("/connections");
    }

    @Test
    public void showReturnConnectionsView() throws Exception {

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", mock(Connection.class));
        when(repository.findConnections()).thenReturn(connections);

        ModelAndView mav = handlerAdapter.handle(request, response, controller);

        assertViewName(mav, "connections");
    }

    @Test
    public void showReturnAllUserConnections() throws Exception {

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        connections.add("twitter", mock(Connection.class));
        when(repository.findConnections()).thenReturn(connections);

        ModelAndView mav = handlerAdapter.handle(request, response, controller);

        assertModelAttributeAvailable(mav, "connections");
        assertModelAttributeValue(mav, "connections", connections);
    }

}
