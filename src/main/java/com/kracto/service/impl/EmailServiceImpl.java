package com.kracto.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.kracto.service.EmailService;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Override
	@Async
	public void sendForVerifyAccount(String url, String to, String subject, String lang) throws MessagingException {
		send(url, to, subject, "verify-email-" + lang + ".html");
	}

	@Override
	@Async
	public void sendForResetPassword(String url, String to, String subject, String lang) throws MessagingException {
		send(url, to, subject, "forgot-password-" + lang + ".html");
	}

	private void send(String url, String to, String subject, String template) throws MessagingException {
		// Prepare the evaluation context
		final Context ctx = new Context();
		ctx.setVariable("url", url);
		// Prepare message using a Spring helper
		final MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject(subject);
		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process(template, ctx);
		helper.setText(htmlContent, true);
		// Send mail
		mailSender.send(message);
	}

}
