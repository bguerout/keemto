package fr.keemto.core;

/**
 * A NullEvent (from NullPattern) created at EPOCH.
 * 
 */
public class InitializationEvent extends Event {

    public InitializationEvent(String owner, String providerId) {
        super(0, owner, "initialization event", providerId);
    }

}
