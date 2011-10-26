/*
 * GroupManagementPanel
 *
 * Version $Id:$
 *
 * 2011-10-17 1.0-SNAPSHOT
 */
package com.trivago.mail.pigeon.web.components;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

/**
 * Panel Component to manage user groups.
 *
 * @author Christian Krause
 * @since 1.0-SNAPSHOT
 */
public class GroupManagementPanel extends CustomComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* TODO XXX In which data type should the uploaded csv saved? String? */
	private RecipientGroup[] recipientGroup;

	private HorizontalLayout baseLayout;

	private Application application;

	/**
	 * Contructs the panel. You have to attach it to main window yourself.
	 * <p/>
	 * Each field is represented by a class member that gets its value from the event listener that
	 * is attached to each field. This might sound a bit
	 *
	 * @param app The Main Application
	 */
	public GroupManagementPanel(final Application app)
	{
		this.application = app;
		Panel panel = new Panel("Group management");
		baseLayout = new HorizontalLayout();
		baseLayout.setHeight("850px");
		
		final Layout verticalLayoutLeft = new VerticalLayout();

		baseLayout.addComponent(verticalLayoutLeft);

		Table groupTable = new Table("Groups");
		
		groupTable.addContainerProperty("id", Integer.class, null);
		groupTable.addContainerProperty("name", String.class, null);
		groupTable.addContainerProperty("memberCount", Integer.class, null);
		groupTable.addContainerProperty("actions", HorizontalLayout.class, null);
		
		groupTable.setColumnHeader("id", "Id");
		groupTable.setColumnHeader("name", "Name");
		groupTable.setColumnHeader("memberCount", "Member #");
		groupTable.setColumnHeader("actions", "Actions");
		
		groupTable.setWidth("850px");
		
		Button newButton = new Button("New group");
		newButton.setIcon(new ThemeResource("../runo/icons/16/document.png"));
		verticalLayoutLeft.addComponent(newButton);
		
		Object dummyData[][] = {
				{11, "Galileo",  77},
                {69, "Monnier",  83},
                {23, "Vaisala",  79},
                {42, "Oterma",   86}
        };
		
		
		
		
		
		final IndexHits<Node> allGroups = RecipientGroup.getAll();
		ArrayList<RecipientGroup> groupList = new ArrayList<RecipientGroup>();

		for (Node groupNode : allGroups)
		{
			RecipientGroup g = new RecipientGroup(groupNode);
			groupList.add(g);
			getWindow().showNotification(g.toString());
			Label debugLabel = new Label(g.toString());
			baseLayout.addComponent(debugLabel);
		}
		
		
		
		
		
		for(int i = 0; i < dummyData.length; i++)
		{
			HorizontalLayout rowActionButtonsPanel = new HorizontalLayout();
			
			Button editButton = new Button("Edit");
			editButton.setIcon(new ThemeResource("../runo/icons/16/document-txt.png"));
			
			Button showMembersButton = new Button("Show members");
			showMembersButton.setIcon(new ThemeResource("../runo/icons/16/users.png"));
			
			Button deleteButton = new Button("Delete");
			deleteButton.setIcon(new ThemeResource("../runo/icons/16/trash.png"));
			
			editButton.setData(dummyData[i][0]);
			showMembersButton.setData(dummyData[i][0]);
			deleteButton.setData(dummyData[i][0]);
			
			/* TODO XXX */
			editButton.addListener(new Button.ClickListener() {
		        public void buttonClick(ClickEvent event) {
		            // Get the item identifier from the user-defined data.
		            Integer itemId = (Integer)event.getButton().getData();
		            getWindow().showNotification("Button "+
		                                   itemId.intValue()+" clicked.");
		        }
		    });
			
			/* TODO XXX */
			showMembersButton.addListener(new Button.ClickListener() {
		        public void buttonClick(ClickEvent event) {
		            // Get the item identifier from the user-defined data.
		            Integer itemId = (Integer)event.getButton().getData();
		            getWindow().showNotification("Button "+
		                                   itemId.intValue()+" clicked.");
		        } 
		    });
			
			/* TODO XXX */
			deleteButton.addListener(new Button.ClickListener() {
		        public void buttonClick(ClickEvent event) {
		            // Get the item identifier from the user-defined data.
		            Integer itemId = (Integer)event.getButton().getData();
		            getWindow().showNotification("Button "+
		                                   itemId.intValue()+" clicked.");
		        } 
		    });
			
			rowActionButtonsPanel.addComponent(editButton);
			rowActionButtonsPanel.addComponent(showMembersButton);
			rowActionButtonsPanel.addComponent(deleteButton);
			
			groupTable.addItem(new Object[] {dummyData[i][0], dummyData[i][1], dummyData[i][2], rowActionButtonsPanel} , dummyData[i][0]);
		}
		
		verticalLayoutLeft.addComponent(groupTable);

		panel.addComponent(baseLayout);
		setCompositionRoot(panel);
	}
}
