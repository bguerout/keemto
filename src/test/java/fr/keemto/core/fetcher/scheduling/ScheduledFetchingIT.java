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

package fr.keemto.core.fetcher.scheduling;

import fr.keemto.core.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/applicationContext.xml"})
public class ScheduledFetchingIT {

    @Autowired
    private TaskRegistrar registrar;

    @Test
    public void shouldExecuteFetcherAsychronouslyWithDelay() throws Exception {
        CountDownLatch latch = new CountDownLatch(10);
        User user = new User("bguerout");
        FetchingTask countDownTask = new CountDownTask(latch, user);

        registrar.registerTask(countDownTask);
        latch.await(2000, TimeUnit.MILLISECONDS);

        assertThat(latch.getCount(), equalTo((long) 0));
    }

    public class CountDownTask extends FetchingTask {

        private final CountDownLatch latch;

        public CountDownTask(CountDownLatch latch, User user) {
            super(null, null);
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
