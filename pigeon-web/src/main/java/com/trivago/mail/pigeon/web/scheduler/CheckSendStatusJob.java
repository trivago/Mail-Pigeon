package com.trivago.mail.pigeon.web.scheduler;

import com.vaadin.ui.ProgressIndicator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CheckSendStatusJob implements Job
{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		ProgressIndicator pi = (ProgressIndicator) context.get("progressIndicator");
//		long newsletterId = (Long) context.get("newsletterId");
//		long recipientGroupId = (Long) context.get("recipientGroupId");
//		float actualValue = (Float)pi.getValue();

		
	}
}
