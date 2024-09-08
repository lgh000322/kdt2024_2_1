package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.repository.UserRepository;
import org.example.server.repository.WorkRepository;

import javax.sql.DataSource;

public class SchedulerService {
    private static SchedulerService schedulerService = null;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource;

    public SchedulerService(WorkRepository workRepository, UserRepository userRepository) {
        this.workRepository = workRepository;
        this.userRepository = userRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }

    public static SchedulerService createOrGetSchedulerService() {
        if (schedulerService == null) {
            schedulerService = new SchedulerService(WorkRepository.createOrGetWorkRepository(), UserRepository.createOrGetUserRepository());
            System.out.println("싱글톤 스케줄서비스 생성됨");
            return schedulerService;
        }

        System.out.println("싱글톤 스케줄서비스 반환됨");
        return schedulerService;
    }
}
