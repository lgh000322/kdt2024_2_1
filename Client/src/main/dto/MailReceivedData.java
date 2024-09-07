package main.dto;

import java.util.ArrayList;
import java.util.List;

import main.domain.mail.Mail;

public class MailReceivedData {
    private Mail mail;
    private String sendUserEmail;
    private List<String> receivedUserEmails = new ArrayList<>();


    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public String getSendUserEmail() {
        return sendUserEmail;
    }

    public void setSendUserEmail(String sendUserEmail) {
        this.sendUserEmail = sendUserEmail;
    }

    public void setReceivedUserEmails(List<String> receivedUserEmails) {
        this.receivedUserEmails = receivedUserEmails;
    }

    public List<String> getReceivedUserEmails() {
        return receivedUserEmails;
    }

}
