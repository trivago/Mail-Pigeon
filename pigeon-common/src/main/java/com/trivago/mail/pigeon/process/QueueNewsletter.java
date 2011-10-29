package com.trivago.mail.pigeon.process;


import com.rabbitmq.client.*;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.trivago.mail.pigeon.bean.*;
import com.trivago.mail.pigeon.json.MailTransport;
import com.trivago.mail.pigeon.queue.ConnectionPool;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Relationship;
import org.svenson.JSON;
import org.svenson.JSONParser;
import scala.reflect.generic.Trees;

import java.io.IOException;
import java.util.Date;


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
