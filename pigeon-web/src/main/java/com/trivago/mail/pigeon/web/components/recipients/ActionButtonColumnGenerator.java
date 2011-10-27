package com.trivago.mail.pigeon.web.components.recipients;

import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;


public class ActionButtonColumnGenerator implements Table.ColumnGenerator
{
	private final Logger log = Logger.getLogger(ActionButtonColumnGenerator.class);
	@Override
	public Object generateCell(final Table source, final Object itemId, final Object columnId)
	{
		HorizontalLayout hl = new HorizontalLayout();
		Button deleteButton = new Button();
		deleteButton.setImmediate(true);
		deleteButton.setIcon(new ThemeResource("../runo/icons/16/document-delete.png"));

		deleteButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Transaction tx = ConnectionFactory.getDatabase().beginTx();
				try
				{
					Recipient s = new Recipient((Long) itemId);
					for (Relationship r : s.getDataNode().getRelationships())
					{
						r.delete();
					}
					s.getDataNode().delete();
					source.removeItem(itemId);
					source.getWindow().showNotification("Successfully deleted.");
					tx.success();
				}
				catch (Exception e)
				{
					log.error("Error while deleting entry", e);
					source.getWindow().showNotification("Error while deleting entry: " + e.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
					tx.failure();
				}
				finally
				{
					tx.finish();
				}
			}
		});

		hl.addComponent(deleteButton);
		return hl;
	}
}
