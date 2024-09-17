package org.example.server.dto.user_dto;

import org.example.server.domain.work_log.Status;

import java.time.LocalDate;

public class UserSearchData {
    private LocalDate date;

    private Status status;

    private UserSearchData(Builder builder) {
        this.date=builder.date;
        this.status = builder.status;
    }
    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public static class Builder{
        private LocalDate date;

        private Status status;

        public Builder date(LocalDate date) {
            this.date=date;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public UserSearchData build(){
            return new UserSearchData(this);
        }

    }
}
