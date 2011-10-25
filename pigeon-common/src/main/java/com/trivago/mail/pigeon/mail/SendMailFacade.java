package com.trivago.mail.pigeon.mail;

import com.trivago.mail.pigeon.configuration.Settings;
import com.trivago.mail.pigeon.json.MailTransport;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendMailFacade
{
	public void sendMail(MailTransport mailTransport) throws MessagingException
	{
		Session session = Session.getDefaultInstance(Settings.create().getConfiguration().getProperties("mail.*"));

		MimeMessage message = new MimeMessage(session);

		String to = mailTransport.getTo();
		String from = mailTransport.getFrom();
		String replyTo = mailTransport.getReplyTo();
		String subject = mailTransport.getSubject();
		String html = mailTransport.getHtml();
		String text = mailTransport.getText();

		Address fromAdr = new InternetAddress(from);
		Address toAdr = new InternetAddress(to);
		Address rplyAdr = new InternetAddress(replyTo);


		message.setSubject(subject);
		message.setFrom(fromAdr);
		message.setRecipient(Message.RecipientType.TO, toAdr);
		message.setReplyTo(new Address[] {rplyAdr});
		message.setSender(fromAdr);

		// Content
		MimeBodyPart messageTextPart = new MimeBodyPart();
		messageTextPart.setText(text);
		messageTextPart.setContent(html, "text/html");


		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageTextPart);

		// Put parts in message
		message.setContent(multipart);

		Transport.send(message);

	}
}
