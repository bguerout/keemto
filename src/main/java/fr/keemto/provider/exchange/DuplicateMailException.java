package fr.keemto.provider.exchange;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateMailException extends RuntimeException {

    public DuplicateMailException(String s) {
        super(s);
    }

    public DuplicateMailException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
