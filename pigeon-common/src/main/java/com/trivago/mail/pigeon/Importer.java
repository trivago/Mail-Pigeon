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
package com.trivago.mail.pigeon;

import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.configuration.Settings;
import com.trivago.mail.pigeon.importer.Csv;
import com.trivago.mail.pigeon.json.MailTransport;
import com.trivago.mail.pigeon.mail.MailFacade;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.io.IOException;


public class Importer
{
	public static void main(String[] args) throws IOException
	{
		DOMConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.xml"));
		Settings s = Settings.create("/home/mmueller/Development/Checkouts/mail-pigeon/pigeon-common/src/main/resources/configuration.properties", true);

		// doCsv();

	}

	private static void doCsv() throws IOException
	{
		File file = new File("/home/mmueller/resultset.csv");
		Csv csv = new Csv(file);
		csv.importData(false);
	}

	private static void doMail()
	{
		MailFacade mf = new MailFacade();
		MailTransport mt = new MailTransport();
		mt.setmId("test-mail-id");
		mt.setuId("test-user-id");
		mt.setFrom("mario.mueller@trivago.com");
		mt.setTo("mario@xenji.com");
		mt.setReplyTo("mario.mueller@trivago.com");
		mt.setSubject("Test E-Mail from Newsletter system");
		mt.setText("Ein wenig Text zum Testen der Versandfunktion");
		mt.setHtml("<html><body><p>Ein wenig <b>formatierter</b> text zum testen der Versandfunktion</body></html>");
		mf.sendMail(mt);
	}

	private static void doAddSender()
	{
		Sender s = new Sender(1L, "mario.mueller@trivago.com", "mario.mueller@trivago.com", "Mario Mueller");
	}
}
