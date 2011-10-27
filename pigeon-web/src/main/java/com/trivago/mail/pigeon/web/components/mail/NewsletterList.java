package com.trivago.mail.pigeon.web.components.mail;


import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.web.components.sender.ModalAddNewSender;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;

public class NewsletterList extends CustomComponent
{
	private Table viewTable;

	private BeanContainer<Long, Mail> beanContainer;

	public NewsletterList()
	{

		final NewsletterList nl = this;
		final Panel rootPanel = new Panel("Newsletter");
		rootPanel.setWidth("800px");
		
		viewTable = new Table();
		viewTable.setWidth("100%");
		beanContainer = new BeanContainer<Long, Mail>(Mail.class);


		Button senderListNewButton = new Button("New Newsletter");
		senderListNewButton.setImmediate(true);
		senderListNewButton.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
		senderListNewButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Window modalNewWindow = new ModalAddNewsletter(nl);
				event.getButton().getWindow().addWindow(modalNewWindow);
				modalNewWindow.setVisible(true);
			}
		});

		List<Mail> mailList = getMailList();
		for (Mail mail : mailList)
		{
			beanContainer.addItem(mail.getId(), mail);
		}

		viewTable.setContainerDataSource(beanContainer);
		viewTable.addGeneratedColumn("Sender", new SenderColumnGenerator());
		viewTable.addGeneratedColumn("Actions", new ActionButtonColumnGenerator());

		// First set the vis. cols, then the headlines (the other way round leads to an exception)
		viewTable.setVisibleColumns(new String[]
				{
						"id", "subject", "sendDate", "Sender", "Actions"
				});

		viewTable.setColumnHeaders(new String[]
				{
						"ID", "Subject", "Send Date", "Sender", "Actions"
				});


		viewTable.setColumnExpandRatio(5,2);
		

		HorizontalLayout topButtonLayout = new HorizontalLayout();
		topButtonLayout.addComponent(senderListNewButton);
		topButtonLayout.setMargin(false, false, true, false);
		
		rootPanel.addComponent(topButtonLayout);
		rootPanel.addComponent(viewTable);

		setCompositionRoot(rootPanel);
	}

	public List<Mail> getMailList()
	{
		final IndexHits<Node> allSenders = Mail.getAll();
		ArrayList<Mail> allMails = new ArrayList<Mail>();

		if (allSenders.size() == 0)
		{
			return allMails;
		}

		for (Node sendNode : allSenders)
		{
			Mail s = new Mail(sendNode);
			allMails.add(s);
		}

		return allMails;
	}

	public Table getViewTable()
	{
		return viewTable;
	}

	public BeanContainer<Long, Mail> getBeanContainer()
	{
		return beanContainer;
	}
}
