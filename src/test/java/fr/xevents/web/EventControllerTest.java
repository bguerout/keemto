package fr.xevents.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

import fr.xevents.core.Event;
import fr.xevents.core.EventRepository;

public class EventControllerTest extends ControllerTestCase {

    @Mock
    private EventRepository eventRepository;

    private EventController controller;

    @Before
    public void initBeforeTest() throws Exception {
        controller = new EventController(eventRepository);
        request.setMethod("GET");
        request.setRequestURI("/");
    }

    @Test
    public void shouldReturnHomeViewWithEvents() throws Exception {
        ArrayList<Event> events = Lists.newArrayList(new Event(1, "user", "message"));
        when(eventRepository.getAllEvents()).thenReturn(events);

        final ModelAndView mav = handlerAdapter.handle(request, response, controller);
        assertViewName(mav, "home");
        assertModelAttributeAvailable(mav, "events");
        assertModelAttributeValue(mav, "events", events);
    }
}
