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
