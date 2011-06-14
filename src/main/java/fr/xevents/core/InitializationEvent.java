package fr.xevents.core;

/**
 * A NullEvent (from NullPattern) created at EPOCH.
 * 
 */
public class InitializationEvent extends Event {

    public InitializationEvent(String owner) {
        super(0, owner, "initialization event");
    }

}
