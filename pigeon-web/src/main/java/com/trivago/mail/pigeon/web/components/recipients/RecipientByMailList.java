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
import com.trivago.mail.pigeon.bean.Recipient;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.List;

public class RecipientByMailList extends CustomComponent
{
	protected Mail mail;

	protected Table viewTable;

	protected BeanContainer<Long, Recipient> beanContainer;

	public RecipientByMailList(Mail mail)
	{
		this.mail = mail;
		init();
	}

	private void init()
	{
		final Panel rootPanel = new Panel("Sender");
		rootPanel.setWidth("800px");

		viewTable = new Table();
		viewTable.setWidth("100%");

		viewTable.setData(mail.getId());
		viewTable.setImmediate(true);
		beanContainer = new BeanContainer<Long, Recipient>(Recipient.class);

		List<Recipient> recipientList = getRecipientList();
		for (Recipient recipient : recipientList)
		{
			beanContainer.addItem(recipient.getId(), recipient);
		}

		viewTable.setContainerDataSource(beanContainer);
		viewTable.addGeneratedColumn("openmail", new OpenMailColumnGenerator());

		// First set the vis. cols, then the headlines (the other way round leads to an exception)
		viewTable.setVisibleColumns(new String[]
				{
						"id", "firstname", "lastname", "email", "openmail", //"bounce"
				});

		viewTable.setColumnHeaders(new String[]
				{
						"ID", "Firstname", "Lastname", "E-Mail", "Opened Mail", //"Bounced"
				});

		viewTable.setColumnExpandRatio(3, 2);
		viewTable.setColumnExpandRatio(4, 2);

		rootPanel.addComponent(viewTable);
		setCompositionRoot(rootPanel);
	}

	public List<Recipient> getRecipientList()
	{
		ArrayList<Recipient> recipients = new ArrayList<>();
		Iterable<Relationship> recipientsList = mail.getRecipients();
		for (Relationship rel : recipientsList)
		{
			Node userNode = rel.getEndNode();
			Recipient s = new Recipient(userNode);
			recipients.add(s);
		}
		return recipients;
	}
}
