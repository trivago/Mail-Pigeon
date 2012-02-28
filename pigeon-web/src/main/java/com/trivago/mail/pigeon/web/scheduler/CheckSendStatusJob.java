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
