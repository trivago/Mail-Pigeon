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
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.web.components.recipients.ModalRecipientList;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
		
		final Button showMembersButton = new Button();
		showMembersButton.setIcon(new ThemeResource("../runo/icons/16/users.png"));
		showMembersButton.setDescription("Show recipients in this group");
		showMembersButton.setImmediate(true);
		
		final Button deleteButton = new Button();
		deleteButton.setIcon(new ThemeResource("../runo/icons/16/trash.png"));
		deleteButton.setDescription("Delete group and all users in it.");
		deleteButton.setImmediate(true);

		final Button csvImportButton = new Button();
		csvImportButton.setIcon(new ThemeResource("../runo/icons/16/folder.png"));
		csvImportButton.setDescription("Import CSV file of users into this group");
		csvImportButton.setImmediate(true);

		final Button refreshButton = new Button();
		refreshButton.setIcon(new ThemeResource("../runo/icons/16/reload.png"));
		refreshButton.setDescription("Refresh the recipient count in this group (e.g. after an import)");
		refreshButton.setImmediate(true);

		showMembersButton.setData(itemId);
		deleteButton.setData(itemId);
		csvImportButton.setData(itemId);
		refreshButton.setData(itemId);
		
		csvImportButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				ModalRecipientImportCsv modalNewWindow = new ModalRecipientImportCsv((Long)event.getButton().getData());
				event.getButton().getWindow().addWindow(modalNewWindow);
				modalNewWindow.setVisible(true);
			}
		});

		refreshButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				source.removeGeneratedColumn("memberNumber");
				source.addGeneratedColumn("memberNumber", new GroupColumnGenerator());
				source.removeGeneratedColumn("Actions");
				source.addGeneratedColumn("Actions", new ActionButtonColumnGenerator());
			}
		});

		showMembersButton.addListener(new Button.ClickListener() {
	        public void buttonClick(ClickEvent event) {
	            long itemId = (Long)event.getButton().getData();

				ModalRecipientList rlist = new ModalRecipientList(itemId);
	            source.getWindow().addWindow(rlist);
				rlist.setVisible(true);
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
					ConnectionFactory.getGroupIndex().remove(r.getDataNode());
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
		hl.addComponent(csvImportButton);
		hl.addComponent(refreshButton);
		hl.addComponent(deleteButton);
		hl.setSpacing(true);
		
		return hl;
	}
}
