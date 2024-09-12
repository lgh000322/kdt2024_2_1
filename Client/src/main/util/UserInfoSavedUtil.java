package main.util;

import main.domain.user.Role;
import main.dto.user_dto.UserInfo;

public class UserInfoSavedUtil {
	private static UserInfo userInfo;

	private static String userId;
	
	private static Role role;
	
	public static UserInfo getUserInfo() {
		return userInfo;
	}

	public static void setUserInfo(UserInfo userInfo) {
		UserInfoSavedUtil.userInfo = userInfo;
	}

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		UserInfoSavedUtil.userId = userId;
	}

	public static Role getRole() {
		return role;
	}

	public static void setRole(Role role) {
		UserInfoSavedUtil.role = role;
	}

	
	

	
}
