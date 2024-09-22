package main.dto.answer_dto;

public class AnswerInBoardDto {
	private String answerUserId;
	private String answerContent;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return answerUserId + ": " + answerContent;
	}

	public String getAnswerUserId() {
		return answerUserId;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public AnswerInBoardDto(Builder builder) {
		this.answerContent = builder.answerContent;
		this.answerUserId = builder.answerUserId;
	}

	public static class Builder {
		private String answerUserId;
		private String answerContent;

		public Builder answerUserId(String answerUserId) {
			this.answerUserId = answerUserId;
			return this;
		}

		public Builder answerContent(String answerContent) {
			this.answerContent = answerContent;
			return this;
		}

		public AnswerInBoardDto build() {
			return new AnswerInBoardDto(this);
		}

	}
}
