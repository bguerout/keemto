package fr.xevents.spikes;

import org.springframework.social.connect.Connection;

public interface InterfaceWithTypeArguments<S, A extends Connection<S>> {

    void test(A connection);

}
