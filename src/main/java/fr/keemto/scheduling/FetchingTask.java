package fr.keemto.scheduling;

import fr.keemto.core.AccountKey;
import fr.keemto.core.fetching.FetchingException;

public interface FetchingTask extends Runnable {

    void run() throws FetchingException;

    long getDelay();

    AccountKey getFetchedAccountKey();
}
