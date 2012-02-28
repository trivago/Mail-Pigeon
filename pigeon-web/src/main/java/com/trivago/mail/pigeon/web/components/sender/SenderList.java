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
package com.trivago.mail.pigeon.web.components.sender;

import com.trivago.mail.pigeon.bean.Sender;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;

public class SenderList extends CustomComponent
{
	private Table viewTable;

	private BeanContainer<Long, Sender> beanContainer;

	public SenderList()
	{
		final SenderList sl = this;
		final Panel rootPanel = new Panel("Sender");
		rootPanel.setWidth("800px");
		
		Button senderListNewButton = new Button("Add Sender");
		senderListNewButton.setImmediate(true);
		senderListNewButton.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
		senderListNewButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Window modalNewWindow = new ModalAddNewSender(sl);
				event.getButton().getWindow().addWindow(modalNewWindow);
				modalNewWindow.setVisible(true);
			}
		});

		viewTable = new Table();
		viewTable.setWidth("100%");
		final Button editButton = new Button("Edit");
		editButton.setImmediate(true);
		editButton.setIcon(new ThemeResource("../runo/icons/16/document-txt.png"));
		editButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				if (viewTable.isEditable())
				{
					viewTable.setEditable(false);
					editButton.setCaption("Edit");
					viewTable.requestRepaintAll();
					editButton.getWindow().showNotification("Save successful", Window.Notification.TYPE_HUMANIZED_MESSAGE);
				}
				else
				{
					viewTable.setEditable(true);
					editButton.setCaption("Save");
					viewTable.requestRepaintAll();
				}
			}
		});

		viewTable.setImmediate(true);
		beanContainer = new BeanContainer<Long, Sender>(Sender.class);

		List<Sender> senderList = getSenderList();
		for (Sender sender : senderList)
		{
			beanContainer.addItem(sender.getId(), sender);
		}

		viewTable.setContainerDataSource(beanContainer);
		viewTable.addGeneratedColumn("Actions", new ActionButtonColumnGenerator());

		// First set the vis. cols, then the headlines (the other way round leads to an exception)
		viewTable.setVisibleColumns(new String[]
				{
						"id", "name", "fromMail", "replytoMail", "sentMailsCount", "Actions"
				});

		viewTable.setColumnHeaders(new String[]
				{
						"ID", "Name", "E-Mail", "Reply To", "E-Mails sent", "Actions"
				});

		viewTable.setColumnExpandRatio(3,2);
		viewTable.setColumnExpandRatio(4, 2);

		HorizontalLayout topButtonLayout = new HorizontalLayout();
		topButtonLayout.setSpacing(true);
		topButtonLayout.setMargin(false, false, true, false);
		topButtonLayout.addComponent(senderListNewButton);
		topButtonLayout.addComponent(editButton);

		rootPanel.addComponent(topButtonLayout);
		rootPanel.addComponent(viewTable);

		setCompositionRoot(rootPanel);

	}

	public List<Sender> getSenderList()
	{
		final IndexHits<Node> allSenders = Sender.getAll();
		ArrayList<Sender> senderList = new ArrayList<>();

		if (allSenders.size() == 0)
		{
			return senderList;
		}

		for (Node sendNode : allSenders)
		{
			Sender s = new Sender(sendNode);
			senderList.add(s);
		}

		return senderList;
	}

	public Table getViewTable()
	{
		return viewTable;
	}

	public BeanContainer<Long, Sender> getBeanContainer()
	{
		return beanContainer;
	}
}
