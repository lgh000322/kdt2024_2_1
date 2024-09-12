package main.dto.leave_dto;

import java.time.LocalDate;

public class ForRequestLeaveDto {

	private LocalDate requestDate;

	private LocalDate startDate;

	private LocalDate endDate;

	private String userId;

	private ForRequestLeaveDto(Builder builder) {
		this.requestDate = builder.requestDate;
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;

		this.userId = builder.userId;
	}

	public static class Builder {

		private LocalDate requestDate;

		private LocalDate startDate;

		private LocalDate endDate;

		private String userId;

		public Builder requestDate(LocalDate requestDate) {
			this.requestDate = requestDate;
			return this;
		}

		public Builder startDate(LocalDate startDate) {
			this.startDate = startDate;
			return this;
		}

		public Builder endDate(LocalDate endDate) {
			this.endDate = endDate;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public ForRequestLeaveDto build() {
			return new ForRequestLeaveDto(this);
		}

	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public String getUserId() {
		return userId;
	}
}
