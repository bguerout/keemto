/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.keemto.scheduling;

import fr.keemto.config.CoreConfig;
import fr.keemto.config.SchedulingConfig;
import fr.keemto.core.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, SchedulingConfig.class}, loader = AnnotationConfigContextLoader.class)
public class ScheduledTaskIT {

    @Autowired
    private TaskScheduler scheduler;

    @Test
    public void shouldExecuteFetcherAsychronouslyWithDelay() throws Exception {

        CountDownLatch latch = new CountDownLatch(10);
        Task countDownTask = new CountDownTask(latch);

        scheduler.scheduleTask(countDownTask);
        latch.await(2000, TimeUnit.MILLISECONDS);

        assertThat(latch.getCount(), equalTo((long) 0));
    }

    public class CountDownTask implements Task {

        private final CountDownLatch latch;

        public CountDownTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public String getTaskId() {
            return "countdown";
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
