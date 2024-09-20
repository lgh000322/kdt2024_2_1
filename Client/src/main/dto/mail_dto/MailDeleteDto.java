package main.dto.mail_dto;

import main.domain.mail.MailType;

public class MailDeleteDto {
	private MailType mailType;

	private Long mailNum;

	public MailType getMailType() {
		return mailType;
	}

	public void setMailType(MailType mailType) {
		this.mailType = mailType;
	}

	public Long getMailNum() {
		return mailNum;
	}

	public void setMailNum(Long mailNum) {
		this.mailNum = mailNum;
	}
}
