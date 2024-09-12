package main.dto.leave_dto;

import java.sql.Date;

/**
*
*  휴가 정보를 Admin 에게 보내기위한 dto -> 수정 필요.
* */
public class LeaveLogOfAdminDto
{
    private Date requestDate;
    private Date startDate;
    private Date endDate;
    private Boolean status;


    public Date getRequestDate() {
        return requestDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public LeaveLogOfAdminDto(LeaveLogOfAdminDto.Builder builder) {
        this.requestDate = builder.requestDate;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
    }

    public static class Builder {
        private Date requestDate;
        private Date startDate;
        private Date endDate;
        private Boolean status;

        public LeaveLogOfAdminDto.Builder requestDate(Date requestDate) {
            this.requestDate = requestDate;
            return this;
        }

        public LeaveLogOfAdminDto.Builder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public LeaveLogOfAdminDto.Builder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public LeaveLogOfAdminDto.Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public LeaveLogOfAdminDto build() {
            return new LeaveLogOfAdminDto(this);
        }

    }
}
