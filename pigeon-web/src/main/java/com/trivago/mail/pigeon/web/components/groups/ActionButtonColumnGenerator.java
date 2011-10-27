package com.trivago.mail.pigeon.web.components.groups;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

public class ActionButtonColumnGenerator implements Table.ColumnGenerator
{
	private final Logger log = Logger.getLogger(ActionButtonColumnGenerator.class);
	
	private Table viewTable;
	
	@Override
	public Object generateCell(final Table source, final Object itemId, final Object columnId)
	{
		HorizontalLayout hl = new HorizontalLayout();
		viewTable = new Table();
		
		final Button showMembersButton = new Button("Show members");
		showMembersButton.setIcon(new ThemeResource("../runo/icons/16/users.png"));
		showMembersButton.setImmediate(true);
		
		final Button deleteButton = new Button("Delete");
		deleteButton.setIcon(new ThemeResource("../runo/icons/16/trash.png"));
		deleteButton.setImmediate(true);
		
		showMembersButton.setData(itemId);
		deleteButton.setData(itemId);
		
		/* TODO XXX */
		showMembersButton.addListener(new Button.ClickListener() {
	        public void buttonClick(ClickEvent event) {
	            // Get the item identifier from the user-defined data.
	            Integer itemId = (Integer)event.getButton().getData();
	            source.getWindow().showNotification("Button "+
	                                   itemId.intValue()+" clicked.");
	        } 
	    });
		
		deleteButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Transaction tx = ConnectionFactory.getDatabase().beginTx();
				try
				{
					RecipientGroup r = new RecipientGroup((Long) itemId);
					for (Relationship rs : r.getDataNode().getRelationships())
					{
						rs.delete();
					}
					r.getDataNode().delete();
					source.removeItem(itemId);
					source.getWindow().showNotification("Successfully deleted.");
					tx.success();
				}
				catch (Exception e)
				{
					source.getWindow().showNotification("Error while deleting entry: " + e.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
					log.error(e);
					tx.failure();
				}
				finally
				{
					tx.finish();
				}
			}
		});

		hl.addComponent(showMembersButton);
		hl.addComponent(deleteButton);
		
		return hl;
	}
}
