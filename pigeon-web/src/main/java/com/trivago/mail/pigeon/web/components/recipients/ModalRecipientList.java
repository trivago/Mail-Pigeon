package com.trivago.mail.pigeon.web.components.recipients;

import com.vaadin.ui.Window;

public class ModalRecipientList extends Window
{
	public ModalRecipientList(long groupId)
	{
		super("Recipient List for GroupId " + groupId);
		setWidth("900px");
		RecipientList rl = new RecipientList(groupId);
		addComponent(rl);
	}
}
