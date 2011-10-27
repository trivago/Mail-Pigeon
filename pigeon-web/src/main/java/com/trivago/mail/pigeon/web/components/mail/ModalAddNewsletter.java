package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.web.components.groups.GroupSelectBox;
import com.trivago.mail.pigeon.web.components.sender.SenderSelectBox;
import com.vaadin.data.Property;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;

import java.util.Date;

public class ModalAddNewsletter extends Window
{
	private String subject;
	
	private Date sendDate;

	private long senderId;
	
	public ModalAddNewsletter(final NewsletterList nl)
	{
		super();

		setModal(true);
		setWidth("600px");

		Panel rootPanel = new Panel("Add new Newsletter");
		final VerticalLayout verticalLayout = new VerticalLayout();
		final SenderSelectBox senderSelectBox = new SenderSelectBox();
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		final GroupSelectBox groupSelectBox = new GroupSelectBox();
		final UploadTextFileComponent uploadTextfile = new UploadTextFileComponent();
		final UploadHtmlFileComponent uploadHtmlfile = new UploadHtmlFileComponent();

		final TextField tfSubject = new TextField("Subject");
		tfSubject.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				subject = event.getProperty().getValue().toString();
			}
		});

		final DateField tfSendDate = new DateField("Send Date");
		tfSendDate.setInvalidAllowed(false);
		tfSendDate.setResolution(DateField.RESOLUTION_MIN);
		tfSendDate.setValue(new Date());
		tfSendDate.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				sendDate = (Date) event.getProperty().getValue();
			}
		});

		final Button cancelButton = new Button("Cancel");

		cancelButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				event.getButton().getWindow().setVisible(false);
				event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
			}
		});
		final Button saveButton = new Button("Send");

		saveButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				// Validation
				if (tfSubject.getValue().equals(""))
				{
					tfSubject.setComponentError(new UserError("Subject cannot be empty"));
				}
				else
				{
					tfSubject.setComponentError(null);
				}

				if (tfSendDate.getValue() == null)
				{
					tfSendDate.setComponentError(new UserError("Date cannot be empty"));
				}
				else
				{
					tfSendDate.setComponentError(null);
				}

				if (!uploadTextfile.isUploadFinished())
				{
					uploadTextfile.setComponentError(new UserError("You must provide a text file"));
				}
				else
				{
					uploadTextfile.setComponentError(null);
				}

				if (!uploadHtmlfile.isUploadFinished())
				{
					uploadHtmlfile.setComponentError(new UserError("You must provide a html file"));
				}
				else
				{
					uploadHtmlfile.setComponentError(null);
				}

				if (senderSelectBox.getSelectedSender() == 0)
				{
					senderSelectBox.setComponentError(new UserError("You must select a sender"));
				}
				else
				{
					senderSelectBox.setComponentError(null);
				}

				if (groupSelectBox.getSelectedGroup() == 0)
				{
					groupSelectBox.setComponentError(new UserError("You must select a recipient group"));
				}
				else
				{
					groupSelectBox.setComponentError(null);
				}
			}
		});



		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(cancelButton);

		Panel metaData = new Panel("Basic Data");
		metaData.addComponent(tfSubject);
		metaData.addComponent(tfSendDate);

		verticalLayout.addComponent(metaData);
		verticalLayout.addComponent(senderSelectBox);
		verticalLayout.addComponent(groupSelectBox);
		verticalLayout.addComponent(uploadTextfile);
		verticalLayout.addComponent(uploadHtmlfile);
		verticalLayout.addComponent(buttonLayout);

		rootPanel.addComponent(verticalLayout);
		this.addComponent(rootPanel);
	}
}
