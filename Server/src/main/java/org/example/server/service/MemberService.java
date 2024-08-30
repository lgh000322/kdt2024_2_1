package org.example.server.service;

<<<<<<< Updated upstream:Server/src/main/java/org/example/server/service/MemberService.java

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
=======
import org.example.server.repository.UserRepository;

public class UserService {
    private static UserService userService = null;
    private final UserRepository userRepository;

    public static UserService createOrGetUserService() {
        if (userService == null) {
            userService = new UserService(UserRepository.createOrGetUserRepository());
            System.out.println("boardService 싱글톤 생성");
            return userService;
>>>>>>> Stashed changes:Server/src/main/java/org/example/server/service/UserService.java
        }

        System.out.println("boardService 싱글톤 반환");
        return userService;
    }
    

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
