package org.example.server.service;


import org.example.server.db_utils.DBUtils;
import org.example.server.repository.MemberRepository;

import javax.sql.DataSource;

public class MemberService {
    private static MemberService memberService = null;
    private final MemberRepository memberRepository;
    private final DataSource dataSource;

    private MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        dataSource = DBUtils.createOrGetDataSource();
    }


    public static MemberService createOrGetMemberService() {
        if (memberService == null) {
            memberService = new MemberService(MemberRepository.createOrGetMemberRepository());
            System.out.println("싱글톤 memberService 생성됨");
            return memberService;
        }

        System.out.println("싱글톤 memberService를 재사용");
        return memberService;
    }

    /**
     * 아래부턴 비즈니스 로직 및 memberRepository를 호출해 쿼리문을 실행한다.
     * 예시는 다음과 같다.
     * public void methodEX() {
     *         //트랜잭션 처리후 비즈니스 로직을 실행한다.
     *         callBizz();
     *         memberRepository.methodEX();
     *     }
     *
     *     public void callBizz(){
     *         System.out.println("비즈니스 로직");
     *     }
     */




}
