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
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;

import java.util.Date;

public class Campaign extends AbstractBean
{
	public static final String ID = "campaign_id";
	public static final String DATE = "send_date";
	public static final String TITLE = "title";
	public static final String URL_PARAM = "url_param";

	public Campaign(final Node underlayingNode)
	{
		this.dataNode = underlayingNode;
	}

	public Campaign(final long campaignId)
	{
		dataNode = ConnectionFactory.getCampaignIndex().get(IndexTypes.CAMPAIGN_ID, campaignId).getSingle();
	}

	public Campaign(final long campaignId, final String urlParams, final Date creationDate, final String campaignTitle)
	{
		Transaction tx = ConnectionFactory.getDatabase().beginTx();
		try
		{
			dataNode = ConnectionFactory.getDatabase().createNode();
			writeProperty(ID, campaignId);
			writeProperty("type", getClass().getName());
			writeProperty(DATE, creationDate.getTime());
			writeProperty(TITLE, campaignTitle);
			writeProperty(URL_PARAM, urlParams);
			ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.CAMPAIGN_ID, campaignId);
			ConnectionFactory.getNewsletterIndex().add(this.dataNode, IndexTypes.TYPE, getClass().getName());
			ConnectionFactory.getDatabase().getReferenceNode().createRelationshipTo(dataNode, RelationTypes.CAMPAIGN_REFERENCE);
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

	public Date getCreationDate()
	{
		return getWrappedProperty(Date.class, Long.class, DATE);
	}

	public void setCreationDate(final Date creationDate)
	{
		writeProperty(DATE, creationDate.getTime());
	}

	public String getTitle()
	{
		return getProperty(String.class, TITLE);
	}

	public void setTitle(final String title)
	{
		writeProperty(TITLE, title);
	}

	public Node getDataNode()
	{
		return this.dataNode;
	}

	public String getUrlParams()
	{
		return getProperty(String.class, URL_PARAM);
	}

	public void setUrlParams(final String urlParams)
	{
		writeProperty(URL_PARAM, urlParams);
	}

	public static IndexHits<Node> getAll()
	{
		return ConnectionFactory.getCampaignIndex().get("type", Campaign.class.getName());
	}
}
