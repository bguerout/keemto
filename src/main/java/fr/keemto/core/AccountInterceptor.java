package fr.keemto.core;

public interface AccountInterceptor {

    public void accountCreated(AccountKey key);

    public void accountDeleted(AccountKey key);
}
