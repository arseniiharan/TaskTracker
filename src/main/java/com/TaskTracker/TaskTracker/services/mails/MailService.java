package com.TaskTracker.TaskTracker.services.mails;

public interface MailService {
    void sendActiveMail(String emailTo, String subject, String message);
}
