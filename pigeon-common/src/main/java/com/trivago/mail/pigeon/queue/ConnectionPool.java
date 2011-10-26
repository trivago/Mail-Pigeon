package com.trivago.mail.pigeon.queue;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.trivago.mail.pigeon.configuration.Settings;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Represents the connection pool for the 
 */
public class ConnectionPool
{
	private static final Logger log = Logger.getLogger(ConnectionPool.class);

	private static Connection connection;

	private static Settings settings = Settings.create();

	public static Connection getConnection()
	{
		if (connection == null)
		{
			ConnectionFactory factory = new ConnectionFactory();
			Configuration configuration = settings.getConfiguration();

			factory.setUsername(configuration.getString("rabbit.username"));
			factory.setPassword(configuration.getString("rabbit.password"));
			factory.setVirtualHost(configuration.getString("rabbit.vhost"));
			factory.setHost(configuration.getString("rabbit.hostname"));
			factory.setPort(configuration.getInt("rabbit.port"));

			try
			{
				connection = factory.newConnection();
			}
			catch (IOException e)
			{
				log.error(e);
				throw new RuntimeException(e);
			}
		}

		return connection;
	}
}
