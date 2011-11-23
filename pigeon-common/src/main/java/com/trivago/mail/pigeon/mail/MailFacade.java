package com.trivago.mail.pigeon.mail;

import com.trivago.mail.pigeon.configuration.Settings;
import com.trivago.mail.pigeon.json.MailTransport;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class MailFacade
{
	private static final Logger log = Logger.getLogger(MailFacade.class);

	public void sendMail(MailTransport mailTransport)
	{
		log.debug("Mail delivery started");
		File propertyfile = ((PropertiesConfiguration) Settings.create().getConfiguration()).getFile();
		Properties config = new Properties();

		try
		{
			config.load(new FileReader(propertyfile));
		}
		catch (IOException e)
		{
			log.error(e);
		}

		Session session = Session.getDefaultInstance(config);

		log.debug("Received session");

		MimeMessage message = new MimeMessage(session);

		String to = mailTransport.getTo();
		String from = mailTransport.getFrom();
		String replyTo = mailTransport.getReplyTo();
		String subject = mailTransport.getSubject();
		String html = mailTransport.getHtml();
		String text = mailTransport.getText();

		try
		{
			Address fromAdr = new InternetAddress(from);
			Address toAdr = new InternetAddress(to);
			Address rplyAdr = new InternetAddress(replyTo);


			message.setSubject(subject);
			message.setFrom(fromAdr);
			message.setRecipient(Message.RecipientType.TO, toAdr);
			message.setReplyTo(new Address[]{rplyAdr});
			message.setSender(fromAdr);
			message.addHeader("Return-path", replyTo);
			message.addHeader("X-TRV-MID", mailTransport.getmId());
			message.addHeader("X-TRV-UID", mailTransport.getuId());

			// Content
			MimeBodyPart messageTextPart = new MimeBodyPart();
			messageTextPart.setText(text);
			messageTextPart.setContent(html, "text/html");


			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageTextPart);

			// Put parts in message
			message.setContent(multipart);

			log.debug("Dispatching message");
			Transport.send(message);
			log.debug("Mail delivery ended");
		}
		catch (MessagingException e)
		{
			log.error(e);
		}

	}

	public void readBounceAccount() throws MessagingException
	{
		Session session = Session.getDefaultInstance(Settings.create().getConfiguration().getProperties("mail.*"));
		Store store = session.getStore("pop3");

		store.connect();

		// Get folder
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);

		if (folder.hasNewMessages())
		{
			// Get directory
			Message message[] = folder.getMessages();
			BounceFacade bounceFacade = new BounceFacade();

			for (int i = 0; i < message.length; i++)
			{
				Message msg = message[i];
				boolean isBounce = bounceFacade.processBounce(msg);
				if (isBounce)
				{
					msg.setFlag(Flags.Flag.SEEN, true);
					msg.saveChanges();
				}
			}
		}


	}
}
