package fr.keemto.core.fetching;

import fr.keemto.core.AccountKey;

public interface FetchingTask extends Runnable {

    void run() throws FetchingException;

    long getDelay();

    AccountKey getFetchedAccountKey();
}
