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
