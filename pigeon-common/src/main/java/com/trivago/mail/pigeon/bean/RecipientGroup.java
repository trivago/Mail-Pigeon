package com.trivago.mail.pigeon.bean;


import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Date;

public class RecipientGroup
{
	private Node dataNode;

	public static final String ID = "user_id";

	public static final String NAME = "name";

	public static final String EMAIL = "email";

	public static final String DATE = "date";

	public RecipientGroup(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public RecipientGroup(final int newsletterId)
	{
		dataNode = ConnectionFactory.getNewsletterIndex().get(IndexTypes.NEWSLETTER_ID, newsletterId).getSingle();
	}

	public RecipientGroup(final int groupId, final String name)
	{
		dataNode = ConnectionFactory.getDatabase().createNode();
		dataNode.setProperty(ID, groupId);
		dataNode.setProperty(NAME, name);
		ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.GROUP_ID, groupId);
		ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.GROUP_REFERENCE);
	}

	public Node getDataNode()
	{
		return dataNode;
	}

	public int getId()
	{
		return (Integer)dataNode.getProperty(ID);
	}

	public String getName()
	{
		return (String)dataNode.getProperty(NAME);
	}

	public Relationship addRecipient(Recipient recipient)
	{
		Node recipientNode = recipient.getDataNode();
		Relationship relation = dataNode.createRelationshipTo(recipientNode, RelationTypes.BELONGS_TO_GROUP);
		relation.setProperty(DATE, new Date());
		return relation;
	}

	public Iterable<Relationship> getRecipients()
	{
		return dataNode.getRelationships(RelationTypes.BELONGS_TO_GROUP);
	}
}
