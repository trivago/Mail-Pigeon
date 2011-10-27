package com.trivago.mail.pigeon.web.components.recipients;

import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;

public class RecipientList extends CustomComponent
{

	private Table viewTable;

	private BeanContainer<Long, Recipient> beanContainer;

	private long groupId = -1;

	public RecipientList(long groupId)
	{
		this.groupId = groupId;
		init();
	}

	public RecipientList()
	{
		init();
	}

	private void init()
	{
		final RecipientList sl = this;
		final Panel rootPanel = new Panel("Sender");
		rootPanel.setWidth("800px");

		//		Button senderListNewButton = new Button("Add Sender");
		//		senderListNewButton.setImmediate(true);
		//		senderListNewButton.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
		//		senderListNewButton.addListener(new Button.ClickListener()
		//		{
		//			@Override
		//			public void buttonClick(Button.ClickEvent event)
		//			{
		//				Window modalNewWindow = new ModalAddNewSender(sl);
		//				event.getButton().getWindow().addWindow(modalNewWindow);
		//				modalNewWindow.setVisible(true);
		//			}
		//		});

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
		beanContainer = new BeanContainer<Long, Recipient>(Recipient.class);

		List<Recipient> recipientList = getRecipientList();
		for (Recipient recipient : recipientList)
		{
			beanContainer.addItem(recipient.getId(), recipient);
		}

		viewTable.setContainerDataSource(beanContainer);
		viewTable.addGeneratedColumn("Actions", new ActionButtonColumnGenerator());

		// First set the vis. cols, then the headlines (the other way round leads to an exception)
		viewTable.setVisibleColumns(new String[]
				{
						"id", "name", "email", "Actions"
				});

		viewTable.setColumnHeaders(new String[]
				{
						"ID", "Name", "E-Mail", "Actions"
				});

		viewTable.setColumnExpandRatio(3, 2);
		viewTable.setColumnExpandRatio(4, 2);

		HorizontalLayout topButtonLayout = new HorizontalLayout();
		topButtonLayout.setSpacing(true);
		topButtonLayout.setMargin(false, false, true, false);
		topButtonLayout.addComponent(editButton);

		rootPanel.addComponent(topButtonLayout);
		rootPanel.addComponent(viewTable);

		setCompositionRoot(rootPanel);
	}

	public List<Recipient> getRecipientList()
	{
		ArrayList<Recipient> recipients = new ArrayList<Recipient>();
		if (groupId != -1)
		{
			RecipientGroup g = new RecipientGroup(groupId);
			Iterable<Relationship> recipientsList = g.getRecipients();
			for (Relationship rel : recipientsList)
			{
				Node userNode = rel.getEndNode();
				Recipient s = new Recipient(userNode);
				recipients.add(s);
			}

		}
		else
		{
			IndexHits<Node> allRecipients = Recipient.getAll();
			for (Node node : allRecipients)
			{
				Recipient s = new Recipient(node);
				recipients.add(s);
			}
		}

		return recipients;
	}
}
