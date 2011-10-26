package com.trivago.mail.pigeon.web.components.sender;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

public class ModalAddNewSender extends Window
{
	private String name;

	private String fromMail;

	private String ReplytoMail;

	public ModalAddNewSender()
	{
		super();

		setModal(true);
		setWidth("300px");
		
		Panel rootPanel = new Panel("Add new Sender");

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

		TextField tfFromMail = new TextField("From E-Mail");
		tfName.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				fromMail = event.getProperty().getValue().toString();
			}
		});

		TextField tfReplyTo = new TextField("ReplyTo E-Mail");
		tfName.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				ReplytoMail = event.getProperty().getValue().toString();
			}
		});

		verticalLayout.addComponent(tfName);
		verticalLayout.addComponent(tfFromMail);
		verticalLayout.addComponent(tfReplyTo);

		HorizontalLayout buttonLayout = new HorizontalLayout();

		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel");

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
