package fr.xevents.web;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/webmvc-config.xml",
        "classpath*:/spring-tests.xml" })
public class HomeControllerTest {

    @Autowired
    private AnnotationMethodHandlerAdapter handlerAdapter;

    @Mock
    private EventRepository eventRepository;

    private HomeController controller;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        controller = new HomeController(eventRepository);
    }

    @Test
    public void shouldReturnHomeViewWithEvents() throws Exception {
        request.setMethod("GET");
        request.setRequestURI("/");
        ArrayList<Event> events = Lists.newArrayList(new Event(1, "user", "message"));
        when(eventRepository.getAllEvents()).thenReturn(events);

        final ModelAndView mav = handlerAdapter.handle(request, response, controller);
        assertViewName(mav, "home");
        assertModelAttributeAvailable(mav, "events");
        assertModelAttributeValue(mav, "events", events);

    }
}
