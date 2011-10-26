package com.trivago.mail.pigeon;

import com.trivago.mail.pigeon.configuration.Settings;
import com.trivago.mail.pigeon.importer.Csv;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.io.IOException;


public class Importer
{
	public static void main(String[] args) throws IOException
	{
		DOMConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.xml"));
		Settings s = Settings.create("/home/mmueller/Development/Checkouts/mail-pigeon/pigeon-common/src/main/resources/configuration.properties", true);
		File file = new File("/home/mmueller/resultset.csv");
		Csv csv = new Csv(file);
		csv.importData(false);
	}
}
