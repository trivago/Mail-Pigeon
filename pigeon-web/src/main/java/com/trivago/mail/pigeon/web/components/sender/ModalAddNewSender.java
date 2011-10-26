package com.trivago.mail.pigeon.web.components.sender;

import com.trivago.mail.pigeon.bean.Sender;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

import java.util.Date;

public class ModalAddNewSender extends Window
{
	private String name;

	private String fromMail;

	private String replytoMail;

	public ModalAddNewSender(final SenderList sl)
	{
		super();

		setModal(true);
		setWidth("300px");
		
		Panel rootPanel = new Panel("Add new Sender");
		final VerticalLayout verticalLayout = new VerticalLayout();

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
		tfFromMail.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				fromMail = event.getProperty().getValue().toString();
			}
		});

		TextField tfReplyTo = new TextField("ReplyTo E-Mail");
		tfReplyTo.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				replytoMail = event.getProperty().getValue().toString();
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

		saveButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				if (name == null)
				{
					event.getButton().getApplication().getMainWindow().showNotification("Name must not be empty", Notification.TYPE_ERROR_MESSAGE);
				}

				if (fromMail == null)
				{
					event.getButton().getApplication().getMainWindow().showNotification("From E-Mail must not be empty", Notification.TYPE_ERROR_MESSAGE);
				}

				if (replytoMail== null)
				{
					event.getButton().getApplication().getMainWindow().showNotification("Reply-To E-Mail must not be empty", Notification.TYPE_ERROR_MESSAGE);
				}

				if (name != null && fromMail != null && replytoMail != null)
				{
					long senderId = Math.round(new Date().getTime() * Math.random());
					try
					{
						Sender s = new Sender(senderId, fromMail, replytoMail, name);
						event.getButton().getWindow().setVisible(false);
						event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
						event.getButton().getWindow().getParent().showNotification("Saved successfully", Notification.TYPE_HUMANIZED_MESSAGE);
						sl.getBeanContainer().addItem(s.getId(), s);
					}
					catch (RuntimeException e)
					{
						// Should never happen ... hopefully :D
					}
				}
			}
		});


		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(cancelButton);

		verticalLayout.addComponent(buttonLayout);
		rootPanel.addComponent(verticalLayout);
		this.addComponent(rootPanel);
	}
}
