package com.trivago.mail.pigeon.bean;


import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

public class MailTemplate extends AbstractBean
{
	public static final String ID = "template_id";
	public static final String SUBJECT = "subject";
	public static final String TEXT = "text_content";
	public static final String HTML = "html_content";

	public MailTemplate(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public MailTemplate(final long templateId)
	{
		dataNode = ConnectionFactory.getTemplateIndex().get(IndexTypes.TEMPLATE_ID, templateId).getSingle();
	}

	public MailTemplate(final long templateId, final String text, final String html, final String subject)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			dataNode.setProperty(ID, templateId);
			dataNode.setProperty("type", getClass().getName());
			dataNode.setProperty(SUBJECT, subject);
			dataNode.setProperty(TEXT, text);
			dataNode.setProperty(HTML, html);
			ConnectionFactory.getTemplateIndex().add(this.dataNode, IndexTypes.TEMPLATE_ID, templateId);
			ConnectionFactory.getTemplateIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.MAIL_TEMPLATE_REFERENCE);
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

	public long getId()
	{
		return (Long) dataNode.getProperty(ID);
	}

	public String getSubject()
	{
		return (String) dataNode.getProperty(SUBJECT);
	}

	public void setSubject(final String subject)
	{
		writeProperty(SUBJECT, subject);
	}

	public Node getDataNode()
	{
		return this.dataNode;
	}

	public String getText()
	{
		return (String) this.dataNode.getProperty(TEXT);
	}

	public void setText(final String text)
	{
		writeProperty(TEXT, text);
	}

	public String getHtml()
	{
		return (String) this.dataNode.getProperty(HTML);
	}

	public void setHtml(final String html)
	{
		writeProperty(HTML, html);
	}

	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getTemplateIndex().get("type", MailTemplate.class.getName());
	}
}
