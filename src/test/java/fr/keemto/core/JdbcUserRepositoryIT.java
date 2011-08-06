package fr.keemto.core;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext.xml" })
public class JdbcUserRepositoryIT {

    @Inject
    private UserRepository repository;

    @Test
    public void shouldReturnAllUsers() throws Exception {

        List<User> users = repository.getAllUsers();

        assertThat(users, notNullValue());
        assertThat(users.size(), greaterThan(0));
    }

    @Test
    public void shouldMapRowToUser() throws Exception {

        List<User> users = repository.getAllUsers();

        assertThat(users.size(), greaterThan(0));
        User user = users.get(0);
        assertThat(user.getUsername(), equalTo("stnevex"));

    }

}
