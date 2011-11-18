package fr.keemto.provider.exchange;

import java.util.List;

public interface MailRepository {

    void persist(List<Mail> mails);

    long getMostRecentMailTime();

    List<Mail> getMailsFrom(String email);
}
