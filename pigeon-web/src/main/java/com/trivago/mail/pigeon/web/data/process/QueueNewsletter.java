package com.trivago.mail.pigeon.web.data.process;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.trivago.mail.pigeon.bean.*;
import com.trivago.mail.pigeon.json.MailTransport;
import com.trivago.mail.pigeon.queue.ConnectionPool;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Relationship;
import org.svenson.JSON;

import java.io.IOException;


public class QueueNewsletter
{
	private static final String channelName = "messages";

	private static final Logger log = Logger.getLogger(QueueNewsletter.class);

	private TemplateProcessor templateProcessor;

	public void queueNewsletter(Mail mail, Sender sender, RecipientGroup group)
	{
		templateProcessor = new TemplateProcessor();

		final Iterable<Relationship> recipients = group.getRecipients();
		log.info("Pushing new newsletter (ID: " + mail.getId() + ") into queue.");

		Campaign campaign = mail.getCampaign();
		for (Relationship recipient : recipients)
		{
			Recipient recipientBean = new Recipient(recipient.getEndNode());
			queueNewsletter(mail, sender, recipientBean, campaign);
			mail.addRecipient(recipientBean);
		}
	}

	private void queueNewsletter(Mail mail, Sender sender, Recipient recipient, Campaign campaign)
	{
		Connection conn = ConnectionPool.getConnection();
		Channel channel = null;
		MailTransport transport = templateProcessor.processMail(mail, recipient, sender, campaign);

		if (transport == null)
		{
			log.warn("Template processor returned null instead of a mail transport object. This is probably a bug!");
			return;
		}

		if (transport.shouldAbortSending() && !transport.shouldEnforceSending())
		{
			log.info("Skipped mail to " + transport.getTo() + " because transport aborted sending.");
			return;
		}
		
		String json = JSON.defaultJSON().forValue( transport );

		try
		{
			channel = conn.createChannel();
			channel.exchangeDeclare("mailpidgeon", "direct", true);
			channel.queueDeclare(channelName, true, false, false, null);
			channel.queueBind(channelName, "mailpidgeon", "mailpidgeon");

			byte[] messageBodyBytes = json.getBytes();
			channel.basicPublish("mailpidgeon", "mailpidgeon", null, messageBodyBytes);

		}
		catch (IOException e)
		{
			log.error(e);
		}
		finally
		{
			if (channel != null)
			{
				try
				{
					channel.close();
				}
				catch (IOException e)
				{
					log.error("Could not close channel", e);
				}
			}
		}
	}

	public int getProgress(long newsletterId)
	{
		Connection conn = ConnectionPool.getConnection();
		Channel channel = null;
		try
		{
			channel = conn.createChannel();
			AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(channelName);
			return declareOk.getMessageCount();
		}
		catch (Exception e)
		{
			log.error("Error while fetching progress", e);
		}
		finally {
			assert channel != null;
			try
			{
				channel.close();
			}
			catch (IOException e)
			{
				log.error("Could not close channel", e);
			}
		}
		return 0;
	}
}
