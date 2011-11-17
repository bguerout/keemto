package fr.keemto.provider.exchange;

public class DuplicateMailException extends RuntimeException {

    public DuplicateMailException(String s) {
        super(s);
    }

    public DuplicateMailException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
