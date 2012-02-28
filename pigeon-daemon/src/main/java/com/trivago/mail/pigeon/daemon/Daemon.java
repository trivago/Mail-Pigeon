/**
 * Copyright (C) 2011-2012 trivago GmbH <mario.mueller@trivago.com>, <christian.krause@trivago.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
