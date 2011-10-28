package com.trivago.mail.pigeon.bean;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

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
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			dataNode.setProperty(ID, userId);
			dataNode.setProperty("type", getClass().getName());
			dataNode.setProperty(NAME, name);
			dataNode.setProperty(EMAIL, email);
			ConnectionFactory.getUserIndex().add(this.dataNode, IndexTypes.USER_ID, userId);
			ConnectionFactory.getUserIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
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

	public String getEmail()
	{
		return (String) dataNode.getProperty(EMAIL);
	}

	public void setName(String name)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode.setProperty(NAME, name);
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

	public void setEmail(String email)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode.setProperty(EMAIL, email);
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

	public Relationship addRecievedNewsletter(Mail mail)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		Relationship relation = null;
		try
		{
			Node newsletterNode = mail.getDataNode();
			relation = dataNode.createRelationshipTo(newsletterNode, RelationTypes.RECIEVED);
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

	public Iterable<Relationship> getReceivedNewsletters()
	{
		return dataNode.getRelationships(RelationTypes.RECIEVED);
	}

	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getUserIndex().get("type", Recipient.class.getName());
	}
}
