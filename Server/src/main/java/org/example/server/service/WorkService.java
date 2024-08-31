package org.example.server.service;

import org.example.server.repository.WorkRepository;

public class WorkService {
    private static WorkService workService = null;
    private final WorkRepository workRepository;

    public static WorkService createOrGetWorkService() {
        if (workService == null) {
            workService = new WorkService(WorkRepository.createOrGetWorkRepository());
            System.out.println("WorkService 싱글톤 생성");
            return workService;
        }

        System.out.println("WorkService 싱글톤 반환");
        return workService;
    }
    public WorkService(WorkRepository workService) {
        this.workRepository = workService;
    }
}
