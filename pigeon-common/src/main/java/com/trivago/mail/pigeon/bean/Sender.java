package com.trivago.mail.pigeon.bean;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

import java.util.Date;

public class Sender extends AbstractBean
{
	public static final String ID = "sender_id";

	public static final String NAME = "name";

	public static final String FROM_MAIL = "from_mail";

	public static final String REPLYTO_MAIL = "reply_to_mail";

	public Sender(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Sender(final long senderId)
	{
		dataNode = ConnectionFactory.getSenderIndex().get(IndexTypes.SENDER_ID, senderId).getSingle();
	}

	public Sender(final long senderId, final String fromMail, final String replytoMail, final String name)
	{
		dataNode = ConnectionFactory.getSenderIndex().get(IndexTypes.SENDER_ID, senderId).getSingle();
		if (dataNode != null)
		{
			throw new RuntimeException("This sender does already exist");
		}

		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			writeProperty(ID, senderId);
			writeProperty("type", getClass().getName());
			writeProperty(NAME, name);
			writeProperty(FROM_MAIL, fromMail);
			writeProperty(REPLYTO_MAIL, replytoMail);

			ConnectionFactory.getSenderIndex().add(this.dataNode, IndexTypes.SENDER_ID, senderId);
			ConnectionFactory.getSenderIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.SENDER_REFERENCE);
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
	}

	public Node getDataNode()
	{
		return dataNode;
	}

	public long getId()
	{
		return getProperty(Long.class, ID, false);
	}

	public String getName()
	{
		return getProperty(String.class, NAME);
	}

	public String getFromMail()
	{
		return getProperty(String.class, FROM_MAIL);
	}

	public String getReplytoMail()
	{
		return getProperty(String.class, REPLYTO_MAIL);
	}

	public void setName(String name)
	{
		writeProperty(NAME, name);
	}

	public void setFromMail(String fromMail)
	{
		writeProperty(FROM_MAIL, fromMail);
	}

	public void setReplytoMail(String replytoMail)
	{
		writeProperty(REPLYTO_MAIL, replytoMail);
	}

	public Relationship addSentMail(Mail mail)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		Relationship relation = null;
		try
		{
			Node recipientNode = mail.getDataNode();
			relation = dataNode.createRelationshipTo(recipientNode, RelationTypes.SENT_EMAIL);
			relation.setProperty("date", new Date().getTime());
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

	public Iterable<Relationship> getSentMails()
	{
		return dataNode.getRelationships(RelationTypes.SENT_EMAIL);
	}

	public int getSentMailsCount()
	{
		int count = 0;
		final Iterable<Relationship> sentMails = getSentMails();
		for (Relationship rel : sentMails)
		{
			++count;
		}
		return count;
	}


	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getSenderIndex().get("type", Sender.class.getName());
	}
}
