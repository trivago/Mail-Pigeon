package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import org.neo4j.graphdb.Node;

public class SenderColumnGenerator implements Table.ColumnGenerator
{
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId)
	{
		Node mailNode = ConnectionFactory.getNewsletterIndex().get(IndexTypes.NEWSLETTER_ID, itemId).getSingle();
		final Node startNode = mailNode.getRelationships(RelationTypes.SENT_EMAIL).iterator().next().getStartNode();
		return new Label(startNode.getProperty(Sender.NAME).toString());
	}
}
