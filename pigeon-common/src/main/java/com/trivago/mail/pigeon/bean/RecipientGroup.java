/**
 * Copyright (C) 2011-2012 trivago GmbH <mario.mueller@trivago.com>, <christian.krause@trivago.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trivago.mail.pigeon.bean;


import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

import java.util.Date;

/**
 *
 * @author Mario Mueller mario.mueller@trivago.com
 */
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
				writeProperty(ID, groupId);
				writeProperty("type", getClass().getName());
				writeProperty(NAME, "DefaultGroup");
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
			writeProperty(ID, groupId);
			writeProperty("type", getClass().getName());
			writeProperty(NAME, name);
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
		return getProperty(Long.class, ID, false);
	}

	public String getName()
	{
		return getProperty(String.class, NAME);
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
