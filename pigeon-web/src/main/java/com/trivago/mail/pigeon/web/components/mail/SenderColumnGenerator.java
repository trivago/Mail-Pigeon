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
package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import org.neo4j.graphdb.Node;

public class SenderColumnGenerator implements Table.ColumnGenerator
{
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId)
	{
		Node mailNode = ConnectionFactory.getNewsletterIndex().get(IndexTypes.NEWSLETTER_ID, itemId).getSingle();
		final Node startNode = mailNode.getRelationships(RelationTypes.SENT_EMAIL).iterator().next().getStartNode();
		return new Label(startNode.getProperty(Sender.NAME).toString());
	}
}
