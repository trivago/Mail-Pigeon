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
