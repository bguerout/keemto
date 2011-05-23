package fr.xevents.web;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/webmvc-config.xml",
        "classpath*:/spring-tests.xml" })
public abstract class ControllerTestCase {

    @Autowired
    protected AnnotationMethodHandlerAdapter handlerAdapter;
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
}
