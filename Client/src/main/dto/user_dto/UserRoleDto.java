package main.dto.user_dto;

public enum  UserRoleDto {

    USER("일반 유저"),
    ADMIN("관리자");

    private final String description;

    public static UserRoleDto fromString(String role) {
        return UserRoleDto.valueOf(role.toUpperCase());
    }

    UserRoleDto(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
