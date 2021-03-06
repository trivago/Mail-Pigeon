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
package com.trivago.mail.pigeon.web.components.templates;

import com.trivago.mail.pigeon.bean.MailTemplate;
import com.trivago.mail.pigeon.web.components.templates.ActionButtonColumnGenerator;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;

public class TemplateList extends CustomComponent
{
	private static final Logger log = Logger.getLogger(TemplateList.class);

	private Table viewTable;

	private BeanContainer<Long, MailTemplate> beanContainer;

	public TemplateList()
	{

		final TemplateList nl = this;
		final Panel rootPanel = new Panel("Newsletter");
		rootPanel.setWidth("800px");

		viewTable = new Table();
		viewTable.setWidth("100%");
		beanContainer = new BeanContainer<Long, MailTemplate>(MailTemplate.class);


		Button senderListNewButton = new Button("New Template");
		senderListNewButton.setImmediate(true);
		senderListNewButton.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
		senderListNewButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Window modalNewWindow = new ModalAddTemplate(nl);
				event.getButton().getWindow().addWindow(modalNewWindow);
				modalNewWindow.setVisible(true);
			}
		});


		List<MailTemplate> mailList = getTemplateList();
		for (MailTemplate mail : mailList)
		{
			beanContainer.addItem(mail.getId(), mail);
		}

		viewTable.setContainerDataSource(beanContainer);
		viewTable.addGeneratedColumn("Actions", new ActionButtonColumnGenerator());

		// First set the vis. cols, then the headlines (the other way round leads to an exception)
		viewTable.setVisibleColumns(new String[]
				{
						"id", "subject", "Actions"
				});

		viewTable.setColumnHeaders(new String[]
				{
						"ID", "Subject", "Actions"
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

	public List<MailTemplate> getTemplateList()
	{
		final IndexHits<Node> allTemplateNodes = MailTemplate.getAll();
		ArrayList<MailTemplate> allTemplates = new ArrayList<MailTemplate>();

		if (allTemplateNodes.size() == 0)
		{
			return allTemplates;
		}

		for (Node sendNode : allTemplateNodes)
		{
			MailTemplate s = new MailTemplate(sendNode);
			allTemplates.add(s);
		}

		return allTemplates;
	}

	public Table getViewTable()
	{
		return viewTable;
	}

	public BeanContainer<Long, MailTemplate> getBeanContainer()
	{
		return beanContainer;
	}
}
