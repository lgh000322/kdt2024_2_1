package org.example.server.controller;

import org.example.server.dto.RequestData;
import org.example.server.service.MemberService;

public class MemberController implements Controller {
    private static MemberController memberController = null;
    private final MemberService memberService;


    private MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    public static MemberController createOrGetMemberController() {
        if (memberController == null) {
            memberController = new MemberController(MemberService.createOrGetMemberService());
            System.out.println("싱글톤 memberController 생성됨");
            return memberController;
        }

        return memberController;
    }

    @Override
    public <T> T execute(RequestData requestData) {
        return null;
    }
}
