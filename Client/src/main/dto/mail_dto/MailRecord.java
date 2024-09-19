package main.dto.mail_dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class MailRecord {
	private final SimpleLongProperty mailKeyNo;
	private final SimpleLongProperty mailNo;
	private final SimpleStringProperty mailReceived;
	private final SimpleStringProperty mailTitle;
	private final SimpleStringProperty mailReceivedDate;

	public MailRecord(Long mailKeyNo,Long mailNo, String mailReceived, String mailTitle, String mailReceivedDate) {
		this.mailKeyNo=new SimpleLongProperty(mailKeyNo);
		this.mailNo = new SimpleLongProperty(mailNo);
		this.mailReceived = new SimpleStringProperty(mailReceived);
		this.mailTitle = new SimpleStringProperty(mailTitle);
		this.mailReceivedDate = new SimpleStringProperty(mailReceivedDate);
	}

	// Getters
	public Long getMailKeyNo() {
		return mailKeyNo.get();
	}
	public Long getMailNo() {
		return mailNo.get();
	}

	public String getMailReceived() {
		return mailReceived.get();
	}

	public String getMailTitle() {
		return mailTitle.get();
	}

	public String getMailReceivedDate() {
		return mailReceivedDate.get();
	}

	// Setters
	public void setMailKeyNo(Long mailKeyNo) {
		this.mailKeyNo.set(mailKeyNo);
	}
	
	public void setMailNo(Long mailNo) {
		this.mailNo.set(mailNo);
	}

	public void setMailReceived(String mailReceived) {
		this.mailReceived.set(mailReceived);
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle.set(mailTitle);
	}

	public void setMailReceivedDate(String mailReceivedDate) {
		this.mailReceivedDate.set(mailReceivedDate);
	}

	// Property getters for binding
	public SimpleLongProperty mailKeyNoProperty() {
		return mailKeyNo;
	}
	public SimpleLongProperty mailNoProperty() {
		return mailNo;
	}

	public SimpleStringProperty mailReceivedProperty() {
		return mailReceived;
	}

	public SimpleStringProperty mailTitleProperty() {
		return mailTitle;
	}

	public SimpleStringProperty mailReceivedDateProperty() {
		return mailReceivedDate;
	}
}
