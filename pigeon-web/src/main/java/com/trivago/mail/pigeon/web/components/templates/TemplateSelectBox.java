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
import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

public class TemplateSelectBox extends CustomComponent
{
	private static final Logger log = Logger.getLogger(TemplateSelectBox.class);
	private Long selectedTemplate;

	private static Select select;

	public TemplateSelectBox()
	{
		Panel rootPanel = new Panel("Select Mail Template");
		select = new Select("Available Mail Templates");
		select.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);

        // We want to allow a null value as we could choose manual upload over using a template
		select.setNullSelectionAllowed(true);

		reloadSelect();

        select.addListener(new Select.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{

				selectedTemplate = (Long) event.getProperty().getValue();
				log.debug("Group Select Value: " + selectedTemplate);
			}
		});
		rootPanel.addComponent(select);
		setCompositionRoot(rootPanel);
		log.debug("Init groups done.");
	}

	public static void reloadSelect()
	{
		log.debug("Refresh select triggered");
		if (select.getItemIds().size() > 0)
		{
			log.debug("Clearing select");
			select.removeAllItems();
		}

		final IndexHits<Node> indexHits = MailTemplate.getAll();

		for (Node node : indexHits)
		{
            MailTemplate mt = new MailTemplate(node);
			select.addItem(mt.getId());
			select.setItemCaption(mt.getId(), mt.getTitle());
			log.debug("Added " + mt.getId() + " with name " + mt.getTitle());
		}
	}

	public Long getSelectedTemplate()
	{
		return selectedTemplate;
	}
}
