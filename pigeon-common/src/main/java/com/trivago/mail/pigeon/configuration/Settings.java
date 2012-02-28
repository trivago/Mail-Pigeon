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
package com.trivago.mail.pigeon.configuration;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.URL;

public class Settings
{
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Settings.class);

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
		log.trace("Settings instance requested");
		if (fileName == null && instance != null && !nocache)
		{
			log.trace("Returning cached instance");
			return instance;
		}
		else if (fileName == null && instance == null)
		{
			log.trace("Requesting ENV PIDGEON_CONFIG as path to properties as fileName was null");

			String propertyFileName = System.getenv("PIDGEON_CONFIG");

			if (propertyFileName == null || propertyFileName.equals(""))
			{
				log.warn("ENV is empty and no filename was given -> no config properties found! Using configuration.properties");
			}

			URL resource = Thread.currentThread().getContextClassLoader().getResource("configuration.properties");
			propertyFileName = resource.toExternalForm();
			instance = new Settings();

			try
			{
				instance.setConfiguration(new PropertiesConfiguration(propertyFileName));
			}
			catch (ConfigurationException e)
			{
				log.error(e);
				throw new ConfigurationRuntimeException(e);
			}
		}
		else if (fileName != null && instance == null)
		{
			log.trace("Requesting file properties from " + fileName);
			instance = new Settings();

			try
			{
				instance.setConfiguration(new PropertiesConfiguration(fileName));
			}
			catch (ConfigurationException e)
			{
				log.error(e);
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
