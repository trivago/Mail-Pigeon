package com.trivago.mail.pigeon.web.components.sender;

import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.List;

public class SenderList extends CustomComponent
{
	public SenderList()
	{
		Panel rootPanel = new Panel();
		Label senderListLabel = new Label("Sender List");
		Button senderListNewButton = new Button("Add Sender");

		senderListNewButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Window modalNewWindow = new ModalAddNewSender();
				event.getButton().getWindow().addWindow(modalNewWindow);
				modalNewWindow.setVisible(true);
			}
		});

		Table senderListTable = new Table();
		BeanContainer<Long, Sender> beanContainer = new BeanContainer<Long, Sender>(Sender.class);
		List<Sender> senderList = getSenderList();
		for (Sender sender : senderList)
		{
			beanContainer.addItem(sender.getId(), sender);
		}

		senderListTable.setContainerDataSource(beanContainer);
		senderListTable.addGeneratedColumn("Actions", new ActionButtonColumnGenerator());

		// First set the vis. cols, then the headlines (the other way round leads to an exception)
		senderListTable.setVisibleColumns(new String[]
		{
				"id", "name", "fromMail", "replytoMail", "sentMails", "Actions"
		});

		senderListTable.setColumnHeaders(new String[]
		{
				"ID", "Name", "E-Mail", "Reply To", "E-Mails sent", "Actions"
		});

		rootPanel.addComponent(senderListNewButton);
		rootPanel.addComponent(senderListTable);

		setCompositionRoot(rootPanel);

	}

	public List<Sender> getSenderList()
	{
		final IndexHits<Node> allSenders = ConnectionFactory.getSenderIndex().get("type", Sender.class.getName());
		ArrayList<Sender> senderList = new ArrayList<Sender>();

		if (allSenders.size() == 0)
		{
			return senderList;
		}

		for (Node sendNode : allSenders)
		{
			Sender s = new Sender(sendNode);
			senderList.add(s);
		}

		return senderList;
	}
}
