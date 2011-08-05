package fr.keemto.web;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

public abstract class ControllerTestCase {

    protected AnnotationMethodHandlerAdapter handlerAdapter;
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handlerAdapter = new AnnotationMethodHandlerAdapter();
    }
}
