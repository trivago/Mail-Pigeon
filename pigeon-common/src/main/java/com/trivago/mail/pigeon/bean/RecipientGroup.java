package com.trivago.mail.pigeon.bean;


import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

import java.util.Date;

public class RecipientGroup extends AbstractBean
{
	public static final String ID = "group_id";

	public static final String NAME = "name";

	public static final String DATE = "date";

	public RecipientGroup(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public RecipientGroup(final long groupId)
	{
		dataNode = ConnectionFactory.getGroupIndex().get(IndexTypes.GROUP_ID, groupId).getSingle();
		if (dataNode == null)
		{
			Transaction tx = ConnectionFactory.getDatabase().beginTx();
			try
			{
				dataNode = ConnectionFactory.getDatabase().createNode();
				dataNode.setProperty(ID, groupId);
				dataNode.setProperty("type", getClass().getName());
				dataNode.setProperty(NAME, "DefaultGroup");
				ConnectionFactory.getGroupIndex().add(this.dataNode, IndexTypes.GROUP_ID, groupId);
				ConnectionFactory.getGroupIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
				ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.GROUP_REFERENCE);

				tx.success();
			}
			catch (Exception e)
			{
				log.error(e);
				tx.failure();
			}
			finally
			{
				tx.finish();
			}
		}
	}

	public RecipientGroup(final long groupId, final String name)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			dataNode.setProperty(ID, groupId);
			dataNode.setProperty("type", getClass().getName());
			dataNode.setProperty(NAME, name);
			ConnectionFactory.getGroupIndex().add(this.dataNode, IndexTypes.GROUP_ID, groupId);
			ConnectionFactory.getGroupIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.GROUP_REFERENCE);

			tx.success();
		}
		catch (Exception e)
		{
			log.error(e);
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

	public Relationship addRecipient(Recipient recipient)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		Relationship relation = null;
		try
		{
			Node recipientNode = recipient.getDataNode();
			relation = dataNode.createRelationshipTo(recipientNode, RelationTypes.BELONGS_TO_GROUP);
			relation.setProperty(DATE, new Date().getTime());
			tx.success();

		}
		catch (Exception e)
		{
			log.error(e);
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
		return dataNode.getRelationships(RelationTypes.BELONGS_TO_GROUP);
	}

	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getGroupIndex().get("type", RecipientGroup.class.getName());
	}
	
	public void setName(String name)
	{
		writeProperty(NAME, name);
	}
}
