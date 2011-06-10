package fr.xevents.spikes;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.core.GenericTypeResolver;
import org.springframework.social.connect.Connection;
import org.springframework.social.twitter.api.TwitterApi;

public class MultipleTypeArgumentsTest {

    @Test
    @SuppressWarnings("unchecked")
    public void genericType() throws Exception {
        ClassWithTypeArguments typor = new ClassWithTypeArguments();

        Class[] args = GenericTypeResolver.resolveTypeArguments(typor.getClass(), InterfaceWithTypeArguments.class);

        assertThat(args.length, equalTo(2));
        assertThat(args[0], equalTo(TwitterApi.class));
        assertThat(args[1], equalTo(Connection.class));
    }

    public final class ClassWithTypeArguments implements InterfaceWithTypeArguments<TwitterApi, Connection<TwitterApi>> {

        @Override
        public void test(Connection<TwitterApi> connection) {
        }
    }
}
