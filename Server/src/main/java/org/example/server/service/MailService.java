package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.repository.MailRepository;
import org.example.server.repository.UserRepository;

import javax.sql.DataSource;

public class MailService {
    private final MailRepository mailRepository;
    private final UserRepository userRepository;

    public MailService(MailRepository mailRepository, UserRepository userRepository) {
        this.mailRepository = mailRepository;
        this.userRepository = userRepository;
        this.dataSource = DBUtils.createOrGetDataSource();
    }

    private static MailService mailService = null;
    private final DataSource dataSource;


    public static MailService createOrGetMailService() {
        if (mailService == null) {
            mailService = new MailService(MailRepository.createOrGetMailRepository(), UserRepository.createOrGetUserRepository());
            return mailService;
        }

        return mailService;
    }
}