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

import fr.xevents.core.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext.xml" })
public class ScheduledFetchingIT {

    @Autowired
    private FetchingRegistrar registrar;

    @Test
    public void shouldExecuteFetcherAsychronouslyWithDelay() throws Exception {
        CountDownLatch latch = new CountDownLatch(10);
        User user = new User("bguerout");
        EventTask countDownTask = new CountDownTask(latch, user);

        registrar.registerTask(countDownTask);
        latch.await(2000, TimeUnit.MILLISECONDS);

        assertThat(latch.getCount(), equalTo((long) 0));
    }

    public class CountDownTask extends EventTask {

        private final CountDownLatch latch;

        public CountDownTask(CountDownLatch latch, User user) {
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
