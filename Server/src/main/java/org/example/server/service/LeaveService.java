package org.example.server.service;

import org.example.server.db_utils.DBUtils;
import org.example.server.repository.LeaveRepository;

import javax.sql.DataSource;

public class LeaveService {

    private static LeaveService leaveService = null;
    private final LeaveRepository leaveRepository;
    private final DataSource dataSource;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }

    public static LeaveService createOrGetLeaveService() {
        if(leaveService == null) {
            leaveService = new LeaveService(LeaveRepository.createOrGetLeaveRepository());
            System.out.println("LeaveService 싱글톤 생성");
            return leaveService;
        }

        System.out.println("LeaveService 싱글톤 반환.");
        return leaveService;
    }




}
