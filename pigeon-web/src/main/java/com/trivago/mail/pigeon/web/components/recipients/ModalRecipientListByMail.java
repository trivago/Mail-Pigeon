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
package com.trivago.mail.pigeon.web.components.recipients;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.web.components.mail.MailOpenChart;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;


public class ModalRecipientListByMail extends Window
{
	public ModalRecipientListByMail(Mail mail)
	{
		super("Recipient List for mailing " + mail.getSubject());
		setModal(true);
		setWidth("900px");

		TabSheet tabsheet = new TabSheet();

		RecipientByMailList rl = new RecipientByMailList(mail);
		tabsheet.addTab(rl).setCaption("Recipient List");

		MailOpenChart chart = new MailOpenChart(mail);
		tabsheet.addTab(chart).setCaption("Statistics");

		addComponent(tabsheet);
	}
}
