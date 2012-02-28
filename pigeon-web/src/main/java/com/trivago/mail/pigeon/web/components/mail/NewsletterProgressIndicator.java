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
package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.web.data.process.QueueNewsletter;
import com.vaadin.ui.ProgressIndicator;
import org.neo4j.graphdb.Relationship;


public class NewsletterProgressIndicator extends ProgressIndicator
{

	final int queuedMails;

	final long newsletterId;

	final Mail mail;

	private WorkerThread workerThread;

	public NewsletterProgressIndicator(long newsletterId)
	{
		super(new Float(0.0));
		this.newsletterId = newsletterId;
		mail = new Mail(newsletterId);

		int cnt = 0;

		Iterable<Relationship> recipients = mail.getRecipients();
		for (Relationship rec : recipients)
		{
			++cnt;
		}

		queuedMails = cnt;
		setPollingInterval(500);
	}

	public void init()
	{
		workerThread = new WorkerThread(this);
		workerThread.start();
	}

	class WorkerThread extends Thread
	{
		int current = 0;

		private NewsletterProgressIndicator selfRef;

		public WorkerThread(NewsletterProgressIndicator selfRef)
		{
			this.selfRef = selfRef;
		}

		public void run()
		{
			while (true)
			{
				try
				{
					QueueNewsletter qn = new QueueNewsletter();
					int progress = qn.getProgress(newsletterId);
					current = queuedMails-progress;

					if (current == queuedMails)
					{
						mail.setDone();
						break;
					}

					double result = (queuedMails / 100.00) * current;
					selfRef.setValue(new Float(result / 100.00));

					sleep(500);
				}
				catch (InterruptedException e)
				{

				}
			}
		}
	}

	public WorkerThread getWorkerThread()
	{
		return workerThread;
	}
}
