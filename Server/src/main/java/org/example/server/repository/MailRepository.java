package org.example.server.repository;

public class MailRepository {
    private static MailRepository mailRepository = null;

    public static MailRepository createOrGetMailRepository(){
        if (mailRepository == null) {
            mailRepository = new MailRepository();
            return mailRepository;
        }

        return mailRepository;
    }
}
