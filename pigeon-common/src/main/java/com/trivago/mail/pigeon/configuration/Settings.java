package com.trivago.mail.pigeon.configuration;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Settings
{

	private static Settings instance;

	private Configuration configuration;

	public static Settings create()
	{
		return create(false);
	}

	public static Settings create(boolean nocache)
	{
		return create(null, nocache);
	}

	public static Settings create(String fileName, boolean nocache)
	{
		if (fileName == null && instance != null && !nocache)
		{
			return instance;
		}
		else if (fileName == null && instance == null)
		{
			String propertyFileName = System.getenv("PIDGEON_CONFIG");

			if (propertyFileName.equals(""))
			{
				throw new ConfigurationRuntimeException("Cannot find path to configuration file in ENV[PIDGEON_CONFIG]");
			}

			instance = new Settings();

			try
			{
				instance.setConfiguration(new PropertiesConfiguration(propertyFileName));
			}
			catch (ConfigurationException e)
			{
				throw new ConfigurationRuntimeException(e);
			}
		}
		else if (fileName != null && instance == null)
		{
			instance = new Settings();

			try
			{
				instance.setConfiguration(new PropertiesConfiguration(fileName));
			}
			catch (ConfigurationException e)
			{
				throw new ConfigurationRuntimeException(e);
			}
		}
		return instance;
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(Configuration configuration)
	{
		this.configuration = configuration;
	}
}
