package fr.keemto.core.fetcher;


public class FetcherConfigurationException extends RuntimeException {


    public FetcherConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FetcherConfigurationException(String message) {
        super(message);
    }

}
