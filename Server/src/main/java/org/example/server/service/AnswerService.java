package org.example.server.service;

import org.example.server.repository.AnswerRepository;

public class AnswerService {
    private static AnswerService answerService = null;
    private final AnswerRepository answerRepository;

    public static AnswerService createOrGetAnswerService() {
        if (answerService == null) {
            answerService = new AnswerService(AnswerRepository.createOrGetAnswerRepository());
            System.out.println("AnswerService 싱글톤 생성");
            return answerService;
        }

        System.out.println("AnswerService 싱글톤 반환");
        return answerService;
    }
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    //댓글 조회
    public void AnswerSearch(){
            answerRepository.searchAnswerDB();
    }

    //댓글 달기
    public void AnswerAdd() {
            answerRepository.addAnsweronDB();
    }
}
