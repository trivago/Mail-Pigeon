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
package com.trivago.mail.pigeon.web.components.mail;


import com.trivago.mail.pigeon.bean.Mail;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;

public class NewsletterList extends CustomComponent
{
	private static final Logger log = Logger.getLogger(NewsletterList.class);

	private Table viewTable;

	private BeanContainer<Long, Mail> beanContainer;

	public NewsletterList()
	{

		final NewsletterList nl = this;
		final Panel rootPanel = new Panel("Newsletter");
		rootPanel.setWidth("800px");
		
		viewTable = new Table();
		viewTable.setWidth("100%");
		beanContainer = new BeanContainer<>(Mail.class);


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
		Button statusPopup = new Button("Show delivery status");
		statusPopup.setImmediate(true);
		statusPopup.setIcon(new ThemeResource("../runo/icons/16/globe.png"));
		statusPopup.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Window modalNewWindow = new PopupActiveNewsletters();
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
						"id", "subject", "sendDate", "Sender", "done", "Actions"
				});

		viewTable.setColumnHeaders(new String[]
				{
						"ID", "Subject", "Send Date", "Sender", "Finished","Actions"
				});


		viewTable.setColumnExpandRatio(6,2);
		

		HorizontalLayout topButtonLayout = new HorizontalLayout();
		topButtonLayout.addComponent(senderListNewButton);
		// topButtonLayout.addComponent(statusPopup);
		topButtonLayout.setMargin(false, false, true, false);
		
		rootPanel.addComponent(topButtonLayout);
		rootPanel.addComponent(viewTable);

		setCompositionRoot(rootPanel);
	}

	public List<Mail> getMailList()
	{
		final IndexHits<Node> allSenders = Mail.getAll();
		ArrayList<Mail> allMails = new ArrayList<>();

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
