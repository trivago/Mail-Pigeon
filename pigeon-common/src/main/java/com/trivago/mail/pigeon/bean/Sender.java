package com.trivago.mail.pigeon.bean;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mmueller
 * Date: 17.10.11
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class Sender
{
	private Node dataNode;

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
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			dataNode.setProperty(ID, senderId);
			dataNode.setProperty("type", getClass().getName());
			dataNode.setProperty(NAME, name);
			dataNode.setProperty(FROM_MAIL, fromMail);
			dataNode.setProperty(REPLYTO_MAIL, replytoMail);

			ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.SENDER_ID, senderId);
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.USER_REFERENCE);
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
		return (Long) dataNode.getProperty(ID);
	}

	public String getName()
	{
		return (String) dataNode.getProperty(NAME);
	}

	public String getFromMail()
	{
		return (String) dataNode.getProperty(FROM_MAIL);
	}

	public String getReplytoMail()
	{
		return (String) dataNode.getProperty(REPLYTO_MAIL);
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


}
