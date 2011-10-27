package com.trivago.mail.pigeon.bean;


import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

import java.util.Date;

public class Mail
{
	private Node dataNode;

	public static final String ID = "newsletter_id";
	public static final String DATE = "send_date";
	public static final String SUBJECT = "subject";
	public static final String TEXT = "text_content";
	public static final String HTML = "html_content";



	public Mail(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Mail(final long mailId)
	{
		dataNode = ConnectionFactory.getNewsletterIndex().get(IndexTypes.NEWSLETTER_ID, mailId).getSingle();
	}

	public Mail(final long mailId, final String text, final String html, final Date sendDate, final String subject, final Sender sender)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			dataNode.setProperty(ID, mailId);
			dataNode.setProperty("type", getClass().getName());
			dataNode.setProperty(DATE, sendDate.getTime());
			dataNode.setProperty(SUBJECT, subject);
			dataNode.setProperty(TEXT, text);
			dataNode.setProperty(HTML, html);
			ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.NEWSLETTER_ID, mailId);
			ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.NEWSLETTER_REFERENCE);
			tx.success();
			sender.addSentMail(this);
		}
		catch (Exception e)
		{
			tx.failure();
		}
		finally
		{
			tx.finish();
		}
	}


	public long getId()
	{
		return (Long) dataNode.getProperty(ID);
	}

	public Date getSendDate()
	{
		return  new Date((Long) dataNode.getProperty(DATE));
	}

	public String getSubject()
	{
		return (String) dataNode.getProperty(SUBJECT);
	}

	public Node getDataNode()
	{
		return this.dataNode;
	}

	public String getText()
	{
		return (String) this.dataNode.getProperty(TEXT);
	}

	public String getHtml()
	{
		return (String) this.dataNode.getProperty(HTML);
	}

	public Relationship addRecipient(Recipient recipient)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		Relationship relation = null;
		try
		{
			Node recipientNode = recipient.getDataNode();
			relation = dataNode.createRelationshipTo(recipientNode, RelationTypes.DELIVERED_TO);
			relation.setProperty(DATE, new Date().getTime());
			tx.success();
		}
		catch (Exception e)
		{
			tx.failure();
		}
		finally
		{
			tx.finish();
		}
		return relation;
	}

	public Iterable<Relationship> getRecipients()
	{
		return dataNode.getRelationships(RelationTypes.DELIVERED_TO);
	}

	public Iterable<Relationship> getBouncedMails()
	{
		return dataNode.getRelationships(RelationTypes.BOUNCED_MAIL);
	}

	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getNewsletterIndex().get("type", Mail.class.getName());
	}
}
