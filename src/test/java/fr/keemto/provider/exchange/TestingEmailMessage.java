package fr.keemto.provider.exchange;

import microsoft.exchange.webservices.data.*;

import java.util.Date;

public class TestingEmailMessage extends EmailMessage {

    private String id;
    private String subject;
    private String body;
    private Date createdAt;
    private String sender;

    public TestingEmailMessage(String id, String subject, String body, Date createdAt, String sender) throws Exception {
        super(new ExchangeService());
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.createdAt = createdAt;
        this.sender = sender;
    }

    @Override
    public ItemId getId() throws ServiceLocalException {
        try {
            return ItemId.getItemIdFromString(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSubject() throws ServiceLocalException {
        return subject;
    }

    @Override
    public EmailAddress getSender() {
        return new EmailAddress(sender, sender);
    }


    @Override
    public Date getDateTimeCreated() throws ServiceLocalException {
        return createdAt;
    }

    @Override
    public MessageBody getBody() throws ServiceLocalException {
        return new MessageBody(body);
    }

    @Override
    public UniqueBody getUniqueBody() throws ServiceLocalException {
        return null;
    }

    public void addRecipients(String emailAddress) throws ServiceLocalException {
        EmailAddress to = new EmailAddress(emailAddress);
        getToRecipients().add(to);
    }
}
