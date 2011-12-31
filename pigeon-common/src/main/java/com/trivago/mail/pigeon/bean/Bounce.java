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
 * Represents a bounce node. Bounces are mails that are not successful delivered to a recipient for technical reasons.
 *
 * @author Mario Mueller mario.mueller@trivago.com
 */
public class Bounce extends AbstractBean
{
	/**
	 * Construct the bounce from a node e.g. returned by a search.
	 *
	 * There is no check for the node to be of the classes type. Be careful.
	 *
	 * @param underlayingNode The node that should be used as data source
	 */
	public Bounce(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	/**
	 * ID based constructor. This is not the neo4j node ID, but the mail-pigeon internal id.
	 *
	 * @param rawBounceNodeId the  
	 */
	public Bounce(final long rawBounceNodeId)
	{
		dataNode = ConnectionFactory.getDatabase().getNodeById(rawBounceNodeId);
	}

	public Bounce()
	{
		dataNode = ConnectionFactory.getDatabase().getNodeById(ConnectionFactory.DEFAULT_BOUNCE_NODE);

		if (dataNode == null)
		{
			Transaction tx = ConnectionFactory.getDatabase().beginTx();
			try
			{
				dataNode = ConnectionFactory.getDatabase().createNode();
				ConnectionFactory.getBounceIndex().add(this.dataNode, IndexTypes.BOUNCE_ID, ConnectionFactory.DEFAULT_BOUNCE_NODE);
				ConnectionFactory.getBounceIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
				ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.BOUNCE_REFERENCE);

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
	}


	/**
	 * Adds a new relation between a mail and the recipient and the bounce node.
	 * @param mail The mail bean
	 * @param recipient The recipient bean
	 */
	public void addBouncedMail(Mail mail, Recipient recipient)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			log.debug(String.format("Creating new bounce relation %s -> %s", mail.getId(), recipient.getId()));
			Node mailDataNode = mail.getDataNode();
			Relationship relation = dataNode.createRelationshipTo(mailDataNode, RelationTypes.BOUNCED_MAIL);
			relation.setProperty("date", new Date());
			relation.setProperty(Recipient.ID, recipient.getId());
			relation.setProperty(Recipient.EMAIL, recipient.getEmail());

			Node recipientDataNode = recipient.getDataNode();
			Relationship recipientRelation = dataNode.createRelationshipTo(recipientDataNode, RelationTypes.BOUNCED_USER);
			recipientRelation.setProperty("date", new Date().getTime());
			recipientRelation.setProperty(Mail.ID, mail.getId());
			recipientRelation.setProperty(Mail.SUBJECT, mail.getSubject());

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

	public Iterable<Relationship> getBouncedMails()
	{
		return dataNode.getRelationships(RelationTypes.BOUNCED_MAIL);
	}

	public Iterable<Relationship> getBouncedUser()
	{
		return dataNode.getRelationships(RelationTypes.BOUNCED_MAIL);
	}

	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getBounceIndex().get("type", Bounce.class.getName());
	}
}
