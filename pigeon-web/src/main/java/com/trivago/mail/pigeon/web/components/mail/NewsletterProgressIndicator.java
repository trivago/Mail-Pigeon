package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.process.QueueNewsletter;
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
