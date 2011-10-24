package com.trivago.mail.pigeon.bean;


import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Date;

public class Mail
{
	private Node dataNode;

	private static final String ID = "newsletter_id";
	private static final String DATE = "send_date";
	private static final String SUBJECT = "subject";

	public Mail(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Mail(final int newsletterId)
	{
		dataNode = ConnectionFactory.getNewsletterIndex().get(IndexTypes.NEWSLETTER_ID, newsletterId).getSingle();
	}

	public Mail(final int newsletterId, final Date sendDate, final String subject)
	{
		dataNode = ConnectionFactory.getDatabase().createNode();
		dataNode.setProperty(ID, newsletterId);
		dataNode.setProperty(DATE, sendDate);
		dataNode.setProperty(SUBJECT, subject);
		ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.NEWSLETTER_ID, newsletterId);
		ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.NEWSLETTER_REFERENCE);
	}


	public int getId()
	{
		return (Integer)dataNode.getProperty(ID);
	}

	public Date getSendDate()
	{
		return (Date)dataNode.getProperty(DATE);
	}

	public String getSubject()
	{
		return (String)dataNode.getProperty(SUBJECT);
	}

	public Node getDataNode()
	{
		return this.dataNode;
	}
	
	public Relationship addRecipient(Recipient recipient)
	{
		Node recipientNode = recipient.getDataNode();
		Relationship relation = dataNode.createRelationshipTo(recipientNode, RelationTypes.DELIVERED_TO);
		relation.setProperty(DATE, new Date());
		return relation;
	}

	public Iterable<Relationship> getRecipients()
	{
		return dataNode.getRelationships(RelationTypes.DELIVERED_TO);
	}
}
