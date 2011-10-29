package com.trivago.mail.pigeon.web.components.sender;

import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.Util;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;

public class ModalAddNewSender extends Window
{
	private String name;

	private String fromMail;

	private String replytoMail;

	public ModalAddNewSender(final SenderList sl)
	{
		super();
		setClosable(false);
		setModal(true);
		setWidth("300px");
		
		Panel rootPanel = new Panel("Add new Sender");
		final VerticalLayout verticalLayout = new VerticalLayout();
		final TextField tfName = new TextField("Name");
		final TextField tfFromMail = new TextField("From E-Mail");
		final TextField tfReplyTo = new TextField("ReplyTo E-Mail");

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
				if (tfName.getValue().equals(""))
				{
					tfName.setComponentError(new UserError("Name must not be empty"));
				}
				else
				{
					tfName.setComponentError(null);
				}

				if (tfFromMail.getValue().equals(""))
				{
					tfFromMail.setComponentError(new UserError("From E-Mail must not be empty"));
				}
				else
				{
					tfFromMail.setComponentError(null);
				}

				if (tfReplyTo.getValue().equals(""))
				{
					tfReplyTo.setComponentError(new UserError("Reply-To E-Mail must not be empty"));
				}
				else
				{
					tfReplyTo.setComponentError(null);
				}

				if (!tfName.getValue().equals("")
						&& !tfFromMail.getValue().equals("")
						&& !tfReplyTo.getValue().equals(""))
				{
					tfName.setComponentError(null);
					tfFromMail.setComponentError(null);
					tfReplyTo.setComponentError(null);
					
					long senderId = Util.generateId();
					try
					{
						Sender s = new Sender(senderId, tfFromMail.getValue().toString(), tfReplyTo.getValue().toString(), tfName.getValue().toString());
						event.getButton().getWindow().setVisible(false);
						event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
						event.getButton().getWindow().getParent().showNotification("Saved successfully", Notification.TYPE_HUMANIZED_MESSAGE);
						sl.getBeanContainer().addItem(s.getId(), s);

						// The sender select could be placed anywhere but must be reloaded to reflect the changes.
						SenderSelectBox.reloadSelect();
					}
					catch (RuntimeException e)
					{
						// Should never happen ... hopefully :D
					}
				}
			}
		});

		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(cancelButton);

		verticalLayout.addComponent(buttonLayout);
		rootPanel.addComponent(verticalLayout);
		this.addComponent(rootPanel);
	}
}
