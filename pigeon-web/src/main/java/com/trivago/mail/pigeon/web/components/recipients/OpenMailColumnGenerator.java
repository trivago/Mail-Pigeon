package com.trivago.mail.pigeon.web.components.recipients;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.storage.RelationTypes;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.awt.*;

public class OpenMailColumnGenerator implements Table.ColumnGenerator
{

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId)
	{
		long newsletterId = (Long) source.getData();
		long recipientId = (Long) itemId;

		Mail m = new Mail(newsletterId);
		Recipient r = new Recipient(recipientId);

		Node mailNode = m.getDataNode();
		Node userNode = r.getDataNode();

		ThemeResource icon = new ThemeResource("../runo/icons/16/help.png");

		Iterable<Relationship> relationships = userNode.getRelationships(RelationTypes.OPENED);
		for (Relationship rel : relationships)
		{
			if(rel.getEndNode().equals(mailNode))
			{
				icon = new ThemeResource("../runo/icons/16/ok.png");
				break;
			}
		}
		return new Embedded("",icon);
	}
}
