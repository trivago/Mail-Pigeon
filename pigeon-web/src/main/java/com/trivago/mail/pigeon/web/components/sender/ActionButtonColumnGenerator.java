package com.trivago.mail.pigeon.web.components.sender;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;




public class ActionButtonColumnGenerator implements Table.ColumnGenerator
{
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId)
	{
		HorizontalLayout hl = new HorizontalLayout();
		Button deleteButton = new Button("X");
		deleteButton.setIcon(new ThemeResource("../runo/icons/16/trash.png"));

		hl.addComponent(deleteButton);
		return hl;
	}
}
