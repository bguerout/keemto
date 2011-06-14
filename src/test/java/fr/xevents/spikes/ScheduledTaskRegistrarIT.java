package fr.xevents.spikes;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class ScheduledTaskRegistrarIT {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskRegistrarIT.class);

    private final ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
    private final CountDownLatch latch = new CountDownLatch(10);

    @Test
    public void registerAScheduledTaskWithRegistrar() throws Exception {
        CountDownTask task = new CountDownTask();
        Map<Runnable, Long> fixedDelayTasks = new HashMap<Runnable, Long>();
        fixedDelayTasks.put(task, new Long(100));
        registrar.setFixedDelayTasks(fixedDelayTasks);

        registrar.afterPropertiesSet();

        latch.await(2000, TimeUnit.MILLISECONDS);

        assertThat(latch.getCount(), equalTo((long) 0));
    }

    public class CountDownTask implements Runnable {

        @Override
        public void run() {
            latch.countDown();
            log.debug("DummyTask has been triggered, CountDownLatch is decreased.");
        }

        // private final EventSynchronizer synchronizer;
        // private final String providerId;
        // private final UserRepository userRepository;
        //
        // public DummyTask(EventSynchronizer synchronizer, UserRepository userRepository, String providerId) {
        // this.synchronizer = synchronizer;
        // this.userRepository = userRepository;
        // this.providerId = providerId;
        // }
        //
        // @Override
        // public void run() {
        // System.out.println("jhjkhhjklhjklhkl");
        // // List<User> users = userRepository.getAllUsers();
        // // synchronizer.updateEvents(users, providerId);
        // }
    }

}
