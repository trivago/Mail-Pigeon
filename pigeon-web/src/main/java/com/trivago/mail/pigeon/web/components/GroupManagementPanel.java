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
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
		
		final Layout verticalLayoutLeft = new VerticalLayout();

		baseLayout.addComponent(verticalLayoutLeft);

		verticalLayoutLeft.setMargin(true);

		Table groupTable = new Table("Groups");
		groupTable.addContainerProperty("id", Integer.class, null);
		groupTable.addContainerProperty("name", String.class, null);
		groupTable.addContainerProperty("memberCount", Integer.class, null);
		groupTable.addContainerProperty("actions", HorizontalLayout.class, null);
		
		groupTable.setColumnHeader("id", "Id");
		groupTable.setColumnHeader("name", "Name");
		groupTable.setColumnHeader("memberCount", "Member #");
		groupTable.setColumnHeader("actions", "Actions");
		
		Object people1[][] = {
				{11, "Galileo",  77},
                {69, "Monnier",  83},
                {23, "Vaisala",  79},
                {42, "Oterma",   86}
        };
		
		for(int i = 0; i < people1.length; i++)
		{
			HorizontalLayout rowActionButtonsPanel = new HorizontalLayout();
			
			Button editButton = new Button("Edit");
			Button showMembersButton = new Button("Show members");
			Button deleteButton = new Button("delete");
			
			editButton.setData(people1[i][0]);
			showMembersButton.setData(people1[i][0]);
			deleteButton.setData(people1[i][0]);
			
			editButton.addListener(new Button.ClickListener() {
		        public void buttonClick(ClickEvent event) {
		            // Get the item identifier from the user-defined data.
		            Integer itemId = (Integer)event.getButton().getData();
		            getWindow().showNotification("Button "+
		                                   itemId.intValue()+" clicked.");
		        }
		    });
			
			showMembersButton.addListener(new Button.ClickListener() {
		        public void buttonClick(ClickEvent event) {
		            // Get the item identifier from the user-defined data.
		            Integer itemId = (Integer)event.getButton().getData();
		            getWindow().showNotification("Button "+
		                                   itemId.intValue()+" clicked.");
		        } 
		    });
			
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
			
			groupTable.addItem(new Object[] {people1[i][0], people1[i][1], people1[i][2], rowActionButtonsPanel} , people1[i][0]);
		}
		
		verticalLayoutLeft.addComponent(groupTable);

		panel.addComponent(baseLayout);
		setCompositionRoot(panel);
	}
}
