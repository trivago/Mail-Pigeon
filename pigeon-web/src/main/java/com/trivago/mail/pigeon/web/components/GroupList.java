package com.trivago.mail.pigeon.web.components;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.web.components.groups.GroupColumnGenerator;
import com.trivago.mail.pigeon.web.components.groups.ActionButtonColumnGenerator;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupList extends CustomComponent
{
	private Table viewTable;

	private BeanContainer<Long, RecipientGroup> beanContainer;

	public GroupList()
	{
		final GroupList gl = this;
		final Panel rootPanel = new Panel("Groups");
		
		Button groupListNewButton = new Button("Add group");
		groupListNewButton.setImmediate(true);
		groupListNewButton.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
		groupListNewButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Window modalNewWindow = new ModalAddNewGroup(gl);
				event.getButton().getWindow().addWindow(modalNewWindow);
				modalNewWindow.setVisible(true);
			}
		});

		viewTable = new Table();

		final Button editButton = new Button("Edit");
		editButton.setImmediate(true);
		editButton.setIcon(new ThemeResource("../runo/icons/16/document-txt.png"));
		editButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				if (viewTable.isEditable())
				{
					viewTable.setEditable(false);
					editButton.setCaption("Edit");
					viewTable.requestRepaintAll();
					editButton.getWindow().showNotification("Save successful", Window.Notification.TYPE_HUMANIZED_MESSAGE);
				}
				else
				{
					viewTable.setEditable(true);
					editButton.setCaption("Save");
					viewTable.requestRepaintAll();
				}
			}
		});

		viewTable.setImmediate(true);
		beanContainer = new BeanContainer<Long, RecipientGroup>(RecipientGroup.class);

		List<RecipientGroup> groupList = getGroupList();
		for (RecipientGroup group : groupList)
		{
			beanContainer.addItem(group.getId(), group);
		}

		viewTable.setContainerDataSource(beanContainer);
		
		viewTable.addGeneratedColumn("memberNumber", new GroupColumnGenerator());
		
		viewTable.addGeneratedColumn("Actions", new ActionButtonColumnGenerator());

		// First set the vis. cols, then the headlines (the other way round leads to an exception)
		viewTable.setVisibleColumns(new String[]
		{
				"id", "name", "memberNumber", "Actions"
		});

		viewTable.setColumnHeaders(new String[]
		{
				"ID", "Name", "Member #", "Actions"
		});

		viewTable.setColumnExpandRatio(3,2);
		viewTable.setColumnExpandRatio(4, 2);

		HorizontalLayout topButtonLayout = new HorizontalLayout();
		topButtonLayout.setSpacing(true);
		topButtonLayout.setMargin(false, false, true, false);
		topButtonLayout.addComponent(groupListNewButton);
		topButtonLayout.addComponent(editButton);

		rootPanel.addComponent(topButtonLayout);
		rootPanel.addComponent(viewTable);

		setCompositionRoot(rootPanel);
	}

	public List<RecipientGroup> getGroupList()
	{
		final IndexHits<Node> allGroups = RecipientGroup.getAll();
		ArrayList<RecipientGroup> groupList = new ArrayList<RecipientGroup>();

		if (allGroups.size() == 0)
		{
			return groupList;
		}

		for (Node groupNode : allGroups)
		{
			RecipientGroup g = new RecipientGroup(groupNode);
			groupList.add(g);
		}

		return groupList;
	}

	public Table getViewTable()
	{
		return viewTable;
	}

	public BeanContainer<Long, RecipientGroup> getBeanContainer()
	{
		return beanContainer;
	}
}
