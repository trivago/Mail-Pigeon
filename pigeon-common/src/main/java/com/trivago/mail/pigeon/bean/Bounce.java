package com.trivago.mail.pigeon.bean;


import com.rabbitmq.client.AMQP;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Date;

public class Bounce
{
	private Node dataNode;

	public Bounce(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Bounce(final long rawBounceNodeId)
	{
		dataNode = ConnectionFactory.getDatabase().getNodeById(rawBounceNodeId);
	}

	public Bounce()
	{
		dataNode = ConnectionFactory.getDatabase().getNodeById(ConnectionFactory.DEFAULT_BOUNCE_NODE);

		if (dataNode == null)
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			ConnectionFactory.getBounceIndex().add(this.dataNode, IndexTypes.BOUNCE_ID, ConnectionFactory.DEFAULT_BOUNCE_NODE);
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.BOUNCE_REFERENCE);
		}
	}

	public Node getDataNode()
	{
		return dataNode;
	}

	public Relationship addBouncedMail(Mail mail, Recipient recipient)
	{
		Node mailDataNode = mail.getDataNode();
		Relationship relation = dataNode.createRelationshipTo(mailDataNode, RelationTypes.BOUNCED_MAIL);
		relation.setProperty("date", new Date());
		relation.setProperty(Recipient.ID, recipient.getId());
		relation.setProperty(Recipient.EMAIL, recipient.getEmail());

		Node recipientDataNode = recipient.getDataNode();
		Relationship recipientRelation = dataNode.createRelationshipTo(recipientDataNode, RelationTypes.BOUNCED_USER);
		recipientRelation.setProperty("date", new Date());
		recipientRelation.setProperty(Mail.ID, mail.getId());
		recipientRelation.setProperty(Mail.SUBJECT, mail.getSubject());

		return relation;
	}

	public Iterable<Relationship> getBouncedMails()
	{
		return dataNode.getRelationships(RelationTypes.BOUNCED_MAIL);
	}

	public Iterable<Relationship> getBouncedUser()
	{
		return dataNode.getRelationships(RelationTypes.BOUNCED_MAIL);
	}
}
