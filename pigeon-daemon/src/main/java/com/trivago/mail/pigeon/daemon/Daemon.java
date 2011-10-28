package com.trivago.mail.pigeon.daemon;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.trivago.mail.pigeon.json.MailTransport;
import com.trivago.mail.pigeon.mail.MailFacade;
import com.trivago.mail.pigeon.queue.ConnectionPool;
import org.svenson.JSONParser;

import java.io.IOException;

public class Daemon
{
	private static final String channelName = "messages";

	/**
	 * Main Daemon method containing the event loop.
	 *
	 * @param args command line args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException
	{

		Connection conn = null;
		Channel channel = null;

		try
		{
			conn = ConnectionPool.getConnection();
			channel = conn.createChannel();

			boolean autoAck = false;
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(channelName, autoAck, consumer);
			MailFacade mailFacade = new MailFacade();

			while (true)
			{
				QueueingConsumer.Delivery delivery;
				try
				{
					delivery = consumer.nextDelivery();
				}
				catch (InterruptedException ie)
				{
					continue;
				}

				String jsonContent = new String(delivery.getBody());
				MailTransport mailTransport = JSONParser.defaultJSONParser().parse(MailTransport.class, jsonContent);
				mailFacade.sendMail(mailTransport);
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}

		}
		finally
		{
			if (channel != null)
			{
				channel.close();
			}
			if (conn != null)
			{
				conn.close();
			}
		}
	}
}
