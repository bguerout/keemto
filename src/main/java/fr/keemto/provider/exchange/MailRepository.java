package fr.keemto.provider.exchange;

import java.util.List;

public interface MailRepository {

    void persist(List<Email> emails);

    long getMostRecentMailCreationTime();

    List<Email> getMails(String email,long newerThan);
}
