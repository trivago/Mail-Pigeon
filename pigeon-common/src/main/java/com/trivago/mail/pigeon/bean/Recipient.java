package com.trivago.mail.pigeon.bean;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Date;


public class Recipient
{
	private Node dataNode;

	public static final String ID = "user_id";

	public static final String NAME = "name";

	public static final String EMAIL = "email";

	public static final String DATE = "date";

	public Recipient(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Recipient(final long userId)
	{
		dataNode = ConnectionFactory.getUserIndex().get(IndexTypes.USER_ID, userId).getSingle();
	}

	public Recipient(final long userId, final String name, final String email)
	{
		dataNode = ConnectionFactory.getDatabase().createNode();
		dataNode.setProperty(ID, userId);
		dataNode.setProperty("type" , getClass().getName());
		dataNode.setProperty(NAME, name);
		dataNode.setProperty(EMAIL, email);
		ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.USER_ID, userId);
		ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.USER_REFERENCE);
	}

	public Node getDataNode()
	{
		return dataNode;
	}

	public long getId()
	{
		return (Long)dataNode.getProperty(ID);
	}

	public String getName()
	{
		return (String)dataNode.getProperty(NAME);
	}

	public String getEmail()
	{
		return (String)dataNode.getProperty(EMAIL);
	}

	public Relationship addRecievedNewsletter(Mail mail)
	{
		Node newsletterNode = mail.getDataNode();
		Relationship relation = dataNode.createRelationshipTo(newsletterNode, RelationTypes.RECIEVED);
		relation.setProperty(DATE, new Date());
		return relation;
	}

	public Iterable<Relationship> getReceivedNewsletters()
	{
		return dataNode.getRelationships(RelationTypes.RECIEVED);
	}
}
