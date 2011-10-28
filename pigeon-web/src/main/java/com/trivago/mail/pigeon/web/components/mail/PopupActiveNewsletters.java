package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.web.components.groups.GroupSelectBox;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;

public class PopupActiveNewsletters extends com.vaadin.ui.Window
{
	private static final Logger log = Logger.getLogger(PopupActiveNewsletters.class);


	public PopupActiveNewsletters()
	{
		setClosable(false);
		setWidth("400px");

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);

		Button closeButton = new Button("Close");
		vl.addComponent(closeButton);

		IndexHits<Node> all = Mail.getAll();
		for (Node mailNode : all)
		{
			Mail mail = new Mail(mailNode);
			if (!mail.isDone())
			{
				NewsletterProgressComponent pc = new NewsletterProgressComponent(mail);
				vl.addComponent(pc);
			}
		}

		closeButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				event.getButton().getWindow().setVisible(false);
				event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
			}
		});


		addComponent(vl);
	}
}
