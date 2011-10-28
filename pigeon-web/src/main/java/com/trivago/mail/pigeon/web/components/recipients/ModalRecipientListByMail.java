package com.trivago.mail.pigeon.web.components.recipients;

import com.trivago.mail.pigeon.bean.Mail;
import com.vaadin.ui.Window;


public class ModalRecipientListByMail extends Window
{
	public ModalRecipientListByMail(Mail mail)
	{
		super("Recipient List for mailing " + mail.getSubject());
		setModal(true);
		setWidth("900px");
		RecipientByMailList rl = new RecipientByMailList(mail);
		addComponent(rl);
	}
}
