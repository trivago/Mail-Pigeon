package com.trivago.mail.pigeon.process;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.json.MailTransport;
import com.trivago.mail.pigeon.queue.ConnectionPool;
import org.neo4j.graphdb.Relationship;
import org.svenson.JSON;

import java.io.IOException;
import java.util.Date;


public class QueueNewsletter
{
	private static final String channelName = "messages";


	public void queueNewsletter(String text, String html, String subject, Sender sender, RecipientGroup group)
	{
		final Iterable<Relationship> recipients = group.getRecipients();

		long nlId = Math.round(new Date().getTime() * Math.random());
		Mail mail = new Mail(nlId, new Date(), subject);

		for (Relationship recipient : recipients)
		{
			Recipient recipientBean = new Recipient(recipient.getEndNode());
			queueNewsletter(text, html, subject, sender, recipientBean, mail);
			mail.addRecipient(recipientBean);
		}
	}

	private void queueNewsletter(String text, String html, String subject, Sender sender, Recipient recipient, Mail mail)
	{
		Connection conn = ConnectionPool.getConnection();
		Channel channel = null;

		MailTransport transport = new MailTransport();
		transport.setTo(recipient.getEmail());
		transport.setReplyTo(sender.getReplytoMail());
		transport.setFrom(sender.getFromMail());

		transport.setSubject(subject);
		transport.setHtml(html);
		transport.setText(text);

		String json = JSON.defaultJSON().forValue( transport );

		try
		{
			channel = conn.createChannel();
			channel.exchangeDeclare("mailpidgin", "direct", true);
			channel.queueDeclare(channelName, true, false, false, null);
			channel.queueBind(channelName, "mailpidgin", "mailpidgin");

			byte[] messageBodyBytes = json.getBytes();
			channel.basicPublish("mailpidgin", "mailpidgin", null, messageBodyBytes);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}