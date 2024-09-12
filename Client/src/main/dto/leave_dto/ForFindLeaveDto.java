package main.dto.leave_dto;


import main.dto.user_dto.UserRoleDto;

/**
* 휴가를 조회할때  클라이언트로부터 넘겨받는
 * 휴가정보 dto
 *  -> tel아니면 email을 넘겨 받아야 한다. 화면상 넘겨줄수 있는 unique 키가 tel 아니면 email임.
 *  그리고 관리자의 경우 유저명을치면 해당 유저의 tel이나 email을 넘겨주어야함.
* */
public class ForFindLeaveDto {
	 private UserRoleDto userRoleDto;
	    private String userId;
	    private String userName;

	    public UserRoleDto getUserRoleDto() {
	        return userRoleDto;
	    }

	    public void setUserRoleDto(UserRoleDto userRoleDto) {
	        this.userRoleDto = userRoleDto;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public String getUserName() {
	        return userName;
	    }

	    public void setUserName(String userName) {
	        this.userName = userName;
	    }

}
