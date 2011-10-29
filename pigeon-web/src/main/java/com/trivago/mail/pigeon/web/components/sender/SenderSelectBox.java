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
