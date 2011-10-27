package com.trivago.mail.pigeon.web.components.groups;

import java.util.Date;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.vaadin.data.Property;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class ModalAddNewGroup extends Window
{
	private String name;
	
	public ModalAddNewGroup(final GroupList gl)
	{
		super();

		setModal(true);
		setWidth("300px");
		setClosable(false);
		
		Panel rootPanel = new Panel("Add new group");
		
		VerticalLayout verticalLayout = new VerticalLayout();

		VerticalLayout formLayout = new VerticalLayout();

		final TextField tfName = new TextField("Name");
		tfName.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				name = event.getProperty().getValue().toString();
			}
		});
		
		verticalLayout.addComponent(tfName);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();

		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel");

		saveButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				if (tfName.getValue().equals(""))
				{
					tfName.setComponentError(new UserError("Name must not be empty"));
				}
				else
				{
					tfName.setComponentError(null);
				}

				/* TODO XXX check for existing group: no duplicates! */

				if (!tfName.getValue().equals(""))
				{
					tfName.setComponentError(null);
					
					long grou_id = Math.round(new Date().getTime() * Math.random());
					
					try
					{
						RecipientGroup g = new RecipientGroup(grou_id, tfName.getValue().toString());
						event.getButton().getWindow().setVisible(false);
						event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
						event.getButton().getWindow().getParent().showNotification("Saved successfully", Notification.TYPE_HUMANIZED_MESSAGE);
						gl.getBeanContainer().addItem(g.getId(), g);

						// The sender select could be placed anywhere but must be reloaded to reflect the changes.
						//GroupSelectBox.reloadSelect(); /* TODO XXX */
					}
					catch (RuntimeException e)
					{
						// Should never happen ... hopefully :D
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				event.getButton().getWindow().setVisible(false);
				event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
			}
		});
		
		cancelButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				event.getButton().getWindow().setVisible(false);
				event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
			}
		});

		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(cancelButton);

		verticalLayout.addComponent(formLayout);
		verticalLayout.addComponent(buttonLayout);
		rootPanel.addComponent(verticalLayout);
		this.addComponent(rootPanel);
	}
}
