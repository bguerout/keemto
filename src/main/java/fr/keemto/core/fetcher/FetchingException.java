package fr.keemto.core.fetcher;

public class FetchingException extends RuntimeException {

    public FetchingException() {
        super();
    }

    public FetchingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FetchingException(String message) {
        super(message);
    }

    public FetchingException(Throwable cause) {
        super(cause);
    }

}
