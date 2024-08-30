package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.repository.MailRepository;

import javax.sql.DataSource;

public class MailService {
    private final MailRepository mailRepository;

    private static MailService mailService = null;
    private final DataSource dataSource;

    public MailService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
        this.dataSource = DBUtils.createOrGetDataSource();
    }

    public static MailService createOrGetMailService() {
        if (mailService == null) {
            mailService = new MailService(MailRepository.createOrGetMailRepository());
            return mailService;
        }

        return mailService;
    }
}
