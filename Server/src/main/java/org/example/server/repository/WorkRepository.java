package org.example.server.repository;

public class WorkRepository {
    private static WorkRepository workRepository = null;

    public static WorkRepository createOrGetWorkRepository() {
        if (workRepository == null) {
            workRepository = new WorkRepository();
            System.out.println("WorkRepository 싱글톤 생성");
            return workRepository;
        }

        System.out.println("WorkRepository 싱글톤 반환");
        return workRepository;
    }
}
