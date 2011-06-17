package fr.xevents.core.fetcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.xevents.core.EventRepository;
import fr.xevents.core.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext.xml" })
public class FetchingIT {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FetchingRegistrar registrar;

    @Test
    public void shouldExecuteFetcherAsychronouslyWithDelay() throws Exception {
        CountDownLatch latch = new CountDownLatch(10);
        User user = new User("bguerout");
        FetcherHandler handler = new CountDownHandler(latch, user);

        registrar.registerHandler(handler);
        latch.await(2000, TimeUnit.MILLISECONDS);

        assertThat(latch.getCount(), equalTo((long) 0));
    }

    public class CountDownHandler extends FetcherHandler {

        private final CountDownLatch latch;

        public CountDownHandler(CountDownLatch latch, User user) {
            super(null, user, null);
            this.latch = latch;
        }

        @Override
        public long getDelay() {
            return 100;
        }

        @Override
        public void run() {
            latch.countDown();
        }
    }

}
