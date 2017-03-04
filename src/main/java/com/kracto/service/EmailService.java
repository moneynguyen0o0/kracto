package com.kracto.service;

import javax.mail.MessagingException;

public interface EmailService {

	void sendForVerifyAccount(String url, String to, String subject, String lang) throws MessagingException;
	void sendForResetPassword(String url, String to, String subject, String lang) throws MessagingException;

}
