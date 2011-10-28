package com.trivago.mail.pigeon.web.components.recipients;

import com.trivago.mail.pigeon.bean.Recipient;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
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
