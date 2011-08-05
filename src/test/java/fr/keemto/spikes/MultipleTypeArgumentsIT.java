package fr.keemto.spikes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.GenericTypeResolver;
import org.springframework.social.connect.Connection;
import org.springframework.social.twitter.api.Twitter;

public class MultipleTypeArgumentsIT {

    @Test
    public void genericType() throws Exception {
        ClassWithTypeArguments typor = new ClassWithTypeArguments();

        Class<?>[] args = GenericTypeResolver.resolveTypeArguments(typor.getClass(), InterfaceWithTypeArguments.class);

        assertThat(args.length, equalTo(2));
        assertTrue(args[0].equals(Twitter.class));
        assertTrue(args[1].equals(Connection.class));
    }

    public final class ClassWithTypeArguments implements InterfaceWithTypeArguments<Twitter, Connection<Twitter>> {

        @Override
        public void test(Connection<Twitter> connection) {
        }
    }
}
