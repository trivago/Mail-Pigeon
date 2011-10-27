package com.trivago.mail.pigeon.web.components;

import com.vaadin.data.Property;
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
	
	public ModalAddNewGroup()
	{
		super();

		setModal(true);
		setWidth("300px");
		
		Panel rootPanel = new Panel("Add new group");
		
		VerticalLayout verticalLayout = new VerticalLayout();

		VerticalLayout formLayout = new VerticalLayout();

		TextField tfName = new TextField("Name");
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
