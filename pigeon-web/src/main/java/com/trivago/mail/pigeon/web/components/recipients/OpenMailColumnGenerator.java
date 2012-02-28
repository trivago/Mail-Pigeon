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
package com.trivago.mail.pigeon.web.components.recipients;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.storage.RelationTypes;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class OpenMailColumnGenerator implements Table.ColumnGenerator
{

	private static final Logger log = Logger.getLogger(OpenMailColumnGenerator.class);

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId)
	{
		long newsletterId = (Long) source.getData();
		long recipientId = (Long) itemId;

		Mail m = new Mail(newsletterId);
		Recipient r = new Recipient(recipientId);

		Node mailNode = m.getDataNode();
		Node userNode = r.getDataNode();

		ThemeResource icon = new ThemeResource("../runo/icons/16/help.png");

		Iterable<Relationship> relationships = userNode.getRelationships(RelationTypes.OPENED);
		for (Relationship rel : relationships)
		{
			if(rel.getEndNode().equals(mailNode))
			{
				icon = new ThemeResource("../runo/icons/16/ok.png");
				break;
			}
		}
		return new Embedded("",icon);
	}
}
