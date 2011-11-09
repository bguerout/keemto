package fr.keemto.provider.exchange;

public class ExchangeServiceException extends RuntimeException
{
    public ExchangeServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ExchangeServiceException(String message)
    {
        super(message);
    }
}
