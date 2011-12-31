package com.trivago.mail.pigeon.bean;


import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

import java.util.Date;
import java.util.Iterator;

public class Mail extends AbstractBean
{
	public static final String ID = "newsletter_id";
	public static final String DATE = "send_date";
	public static final String SUBJECT = "subject";
	public static final String TEXT = "text_content";
	public static final String HTML = "html_content";
	public static final String DONE = "done";
	public static final String SENT = "sent";

	public Mail(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Mail(final long mailId)
	{
		dataNode = ConnectionFactory.getNewsletterIndex().get(IndexTypes.NEWSLETTER_ID, mailId).getSingle();
	}

	public Mail(final long mailId, final MailTemplate template, final Date sendDate, final Sender sender)
	{
		this(mailId, template.getText(), template.getHtml(), sendDate, template.getSubject(), sender);
	}

	public Mail(final long mailId, final String text, final String html, final Date sendDate, final String subject, final Sender sender)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			writeProperty(ID, mailId);
			writeProperty("type", getClass().getName());
			writeProperty(DATE, sendDate.getTime());
			writeProperty(SUBJECT, subject);
			writeProperty(TEXT, text);
			writeProperty(HTML, html);
			writeProperty(DONE, false);
			writeProperty(SENT, false);
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
		return getProperty(Long.class, ID, false);
	}

	public Date getSendDate()
	{
		return getWrappedProperty(Date.class, Long.class, DATE);
	}

	public String getSubject()
	{
		return getProperty(String.class, SUBJECT);
	}

	public Node getDataNode()
	{
		return this.dataNode;
	}

	public String getText()
	{
		return getProperty(String.class, TEXT);
	}

	public String getHtml()
	{
		return getProperty(String.class, HTML);
	}

	public boolean isDone()
	{
		return getProperty(Boolean.class, DONE);
	}

	public void setDone()
	{
		writeProperty(DONE, true);
	}

	public boolean isSent()
	{
		return getProperty(Boolean.class, SENT);
	}

	public void setSent()
	{
		writeProperty(SENT, true);
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

	public Relationship addToCampaign(Campaign campaign)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		Relationship relation = null;
		try
		{
			Node campaignDataNode = campaign.getDataNode();
			relation = campaignDataNode.createRelationshipTo(dataNode, RelationTypes.PART_OF_CAMPAIGN);
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
		return ConnectionFactory.getNewsletterIndex().get(IndexTypes.TYPE, Mail.class.getName());
	}

	public Campaign getCampaign()
	{
		final Iterable<Relationship> relationships = dataNode.getRelationships(RelationTypes.PART_OF_CAMPAIGN);

		Iterator<Relationship> iterator = relationships.iterator();
		if (iterator.hasNext())
			return new Campaign(iterator.next().getStartNode());
		else
			return null;
	}
}
