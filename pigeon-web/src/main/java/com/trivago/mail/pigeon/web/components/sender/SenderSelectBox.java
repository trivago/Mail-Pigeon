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
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;


public class SenderSelectBox extends CustomComponent
{
	private long selectedSender;

	private static Select select;

	public SenderSelectBox()
	{
		Panel rootPanel = new Panel("Select Sender");
		select = new Select("Available Senders");
		select.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
		select.setNullSelectionAllowed(false);
		reloadSelect();
		select.addListener(new Select.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				selectedSender = (Long) event.getProperty().getValue();
			}
		});
		rootPanel.addComponent(select);
		setCompositionRoot(rootPanel);
	}

	public static void reloadSelect()
	{
		if (select.getItemIds().size() > 0)
		{
			select.removeAllItems();
		}
		
		final IndexHits<Node> indexHits = Sender.getAll();

		for (Node node : indexHits)
		{
			final Long itemId = (Long) node.getProperty(Sender.ID);
			select.addItem(itemId);
			select.setItemCaption(itemId, node.getProperty(Sender.NAME).toString() + " (" + node.getProperty(Sender.FROM_MAIL).toString() + ")");
		}
	}

	public long getSelectedSender()
	{
		return selectedSender;
	}
}
