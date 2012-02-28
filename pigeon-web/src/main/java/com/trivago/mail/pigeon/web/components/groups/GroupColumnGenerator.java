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
package com.trivago.mail.pigeon.web.components.groups;

import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.storage.IndexTypes;
import com.trivago.mail.pigeon.storage.RelationTypes;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class GroupColumnGenerator implements Table.ColumnGenerator
{
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId)
	{
		Node groupNode = ConnectionFactory.getGroupIndex().get(IndexTypes.GROUP_ID, itemId).getSingle();
		
		final Iterable<Relationship> nodeList = groupNode.getRelationships(RelationTypes.BELONGS_TO_GROUP);
		
		int counter = 0;
		for(Relationship r : nodeList)
		{
			++counter;
		}
		
		return new Label(String.valueOf(counter));
	}
}
