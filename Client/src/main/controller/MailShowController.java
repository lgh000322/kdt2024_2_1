package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import main.domain.mail.Mail;

public class MailShowController {
	@FXML
	private TextArea ReceivedContents;

	@FXML
	private Label ReceivedTitle;

	@FXML
	private Label PostUser;

	public void setMailWindow(Mail mail,String writerEmail) {
		if (mail != null) {
			ReceivedContents.setText(mail.getContents());
			ReceivedTitle.setText(mail.getTitle());
			PostUser.setText(writerEmail);
		}
	}
}
