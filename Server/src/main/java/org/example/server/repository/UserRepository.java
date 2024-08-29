package org.example.server.repository;

public class UserRepository {
    private static UserRepository userRepository = null;

    public static UserRepository createOrGetUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository();
            System.out.println("싱글톤 memberRepository 생성됨");
            return userRepository;
        }

        System.out.println("싱글톤 memberRepository를 재사용");
        return userRepository;
    }

    /**
     * 아래부턴 쿼리문을 작성한다.
     * 예시는 다음과 같다.
     *  public void methodEX(){
     *         System.out.println("memberRepository 실행");
     *     }
     */


}
