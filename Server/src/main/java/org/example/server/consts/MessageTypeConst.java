package org.example.server.consts;

public abstract class MessageTypeConst {
    //회원가입
    public static final String MESSAGE_JOIN = "/user/join";

    //회원의 로그인
    public static final String MESSAGE_LOGIN = "/user/login";

    //회원의 로그아웃
    public static final String MESSAGE_LOGOUT = "/user/logout";

    //회원 조회
    public static final String MESSAGE_SEARCH = "/user/search";

    // 회원 조회 (관리자 페이지)
    public static final String MESSAGE_SEARCH_ADMIN = "/user/search/admin";

    // 회원 정보 수정
    public static final String MESSAGE_UPDATE = "/user/update";

    //모든 회원(일반유저) 조회
    public static final String MESSAGE_SEARCH_ALL = "/user/search/all";


    // 모든 회원 조회 (관리자)
    public static final String MESSAGE_SEARCH_ALL_BYADMIN = "/user/search/all/byadmin";

    //모든 회원의 이름과 이메일만 조회
    public static final String MESSAGE_SEARCH_ALL_USERNAME_AND_EMAIL = "/user/search/all/with/someInfo";

    // 회원 아이디 중복 검증
    public static final String MESSAGE_USER_ID_VALIDATION = "/user/idValidation";

    //근태 조회
    public static final String MESSAGE_WORK_SEARCH = "/work/search";

    //출근 업데이트
    public static final String MESSAGE_WORK_START = "/work/start";

    //퇴근 업데이트
    public static final String MESSAGE_WORK_FINISH = "/work/finish";

    //조퇴 업데이트
    public static final String MESSAGE_WORK_OUT_EARLY = "/work/out/early";

    //휴가 신청
    public static final String MESSAGE_LEAVE_REQUEST = "/leave/request";

    //휴가 변경
    public static final String MESSAGE_LEAVE_EDIT = "/leave/edit";

    //휴가 조회
    public static final String MESSAGE_LEAVE_SEARCH = "/leave/search";

    //급여 내역 조회
    public static final String MESSAGE_SALARY_SEARCH = "/salary/search";

    //급여 내역 추가
    public static final String MESSAGE_SALARY_ADD = "/salary/add";

    //메일 추가
    public static final String MESSAGE_MAIL_ADD = "/mail/add";

    //메일함 조회
    public static final String MESSAGE_STORE_SEARCH = "/mail/store/search";

    //특정 메일 조회
    public static final String MESSAGE_MAIL_ONE_SEARCH = "/mail/one/search";

    //특정 메일 삭제
    public static final String MESSAGE_MAIL_ONE_DELETE = "/mail/one/delete";

    //모든 게시판 조회
    public static final String MESSAGE_BOARD_LIST_SEARCH = "/board/list/search";

    //특정 게시글 조회
    public static final String MESSAGE_BOARD_ONE_SEARCH = "/board/one/search";

    //게시글 작성
    public static final String MESSAGE_BOARD_ADD = "/board/add";

    //게시글 삭제
    public static final String MESSAGE_BOARD_DELETE = "/board/delete";

    //게시글 수정
    public static final String MESSAGE_BOARD_UPDATE = "/board/update";

    //댓글 조회
    public static final String MESSAGE_ANSWER_SEARCH = "/answer/search";

    //댓글 작성
    public static final String MESSAGE_ANSWER_ADD = "/answer/add";
}

