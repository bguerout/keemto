package fr.keemto.core;


public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException(String s) {
        super(s);
    }

    public DuplicateEventException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
