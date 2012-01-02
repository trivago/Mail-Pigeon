package com.trivago.mail.pigeon.web.components.mail;


import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.web.data.process.QueueNewsletter;
import com.vaadin.ui.*;
import org.neo4j.graphdb.Relationship;

public class NewsletterProgressComponent extends CustomComponent
{
	final ProgressIndicator indicator;

	public NewsletterProgressComponent(final Mail mail)
	{
		Panel rootPanel = new Panel("Progress for Mail with Suject " + mail.getSubject());

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);

		DateField df = new DateField("Send Date");
		df.setValue(mail.getSendDate());
		df.setReadOnly(true);
		df.setResolution(DateField.RESOLUTION_MIN);

		indicator = new ProgressIndicator(new Float(0.0));
		indicator.setPollingInterval(500);
		int cnt = 0;
		Iterable<Relationship> recipients = mail.getRecipients();

		for (Relationship rec : recipients)
		{
			++cnt;
		}
		final int recpCount = cnt;

		class WorkerThread extends Thread
		{
			int current = 0;
			public volatile int stop = 0;
			public void run()
			{
				while (true)
				{
					if (stop > 0)
					{
						break;
					}

					try
					{
						QueueNewsletter qn = new QueueNewsletter();
						int progress = qn.getProgress(mail.getId());
						current = recpCount - progress;

						if (current == recpCount)
						{
							mail.setDone();
							break;
						}

						double result = (recpCount / 100.00) * current;
						indicator.setValue(new Float(result / 100.00));

						sleep(500);
					}
					catch (InterruptedException e)
					{
					}
				}
			}
		}

		final WorkerThread workerThread = new WorkerThread();

		final Button startButton = new Button("Poll status");
		final Button stopButton = new Button("Stop polling");

		startButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{

				workerThread.start();
				event.getButton().setVisible(false);
				stopButton.setVisible(true);

			}
		});

		stopButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{

				workerThread.stop = 1;
				workerThread.interrupt();
				event.getButton().setVisible(false);
				startButton.setVisible(true);
			}
		});

		stopButton.setVisible(false);
		vl.addComponent(df);
		vl.addComponent(indicator);
		vl.addComponent(startButton);
		vl.addComponent(stopButton);

		rootPanel.addComponent(vl);
		setCompositionRoot(rootPanel);
	}


}
