package org.example.server.repository;

public class MemberRepository {
    private static MemberRepository memberRepository = null;

    public static MemberRepository createOrGetMemberRepository() {
        if (memberRepository == null) {
            memberRepository = new MemberRepository();
            System.out.println("싱글톤 memberRepository 생성됨");
            return memberRepository;
        }

        System.out.println("싱글톤 memberRepository를 재사용");
        return memberRepository;
    }

    /**
     * 아래부턴 쿼리문을 작성한다.
     * 예시는 다음과 같다.
     *  public void methodEX(){
     *         System.out.println("memberRepository 실행");
     *     }
     */


}
