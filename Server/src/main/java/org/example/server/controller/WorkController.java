package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.dto.ResponseData;
import org.example.server.service.WorkService;


public class WorkController implements Controller {
    private static WorkController workController = null;
    private final WorkService workService;

    public static WorkController createOrGetWorkController() {
        if (workController == null) {
            workController = new WorkController(WorkService.createOrGetWorkService());
            System.out.println("WorkController 싱글톤 생성");
            return workController;
        }

        System.out.println("WorkController 싱글톤 반환");
        return workController;
    }

    public WorkController(WorkService workService) {
        this.workService = workService;
    }



    @Override
    public ResponseData execute(RequestData requestData) {
        return null;
    }
}
