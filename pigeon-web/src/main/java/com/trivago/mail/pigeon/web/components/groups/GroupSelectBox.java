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
package com.trivago.mail.pigeon.web.components.groups;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

public class GroupSelectBox extends CustomComponent
{
	private static final Logger log = Logger.getLogger(GroupSelectBox.class);
	private long selectedGroup;

	private static Select select;

	public GroupSelectBox()
	{
		Panel rootPanel = new Panel("Select Recipient Group");
		select = new Select("Available Groups");
		select.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
		select.setNullSelectionAllowed(false);
		reloadSelect();
		select.addListener(new Select.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{

				selectedGroup = (Long) event.getProperty().getValue();
				log.debug("Group Select Value: " + selectedGroup);
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

		final IndexHits<Node> indexHits = RecipientGroup.getAll();

		for (Node node : indexHits)
		{
			final Long itemId = (Long) node.getProperty(RecipientGroup.ID);
			select.addItem(itemId);
			select.setItemCaption(itemId, node.getProperty(RecipientGroup.NAME).toString());
			log.debug("Added " + itemId + " with name " + node.getProperty(RecipientGroup.NAME).toString());
		}
	}

	public long getSelectedGroup()
	{
		return selectedGroup;
	}
}
