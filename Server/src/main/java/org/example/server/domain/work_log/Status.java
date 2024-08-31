package org.example.server.domain.work_log;

public enum Status {
    ABSENCE("결근"),
    TARDINESS("지각"),
    ATTENDANCE("출석"),
    LEAVE("휴가");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}