package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.process.QueueNewsletter;
import com.trivago.mail.pigeon.storage.Util;
import com.trivago.mail.pigeon.web.components.groups.GroupSelectBox;
import com.trivago.mail.pigeon.web.components.sender.SenderSelectBox;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.util.Date;

public class ModalAddNewsletter extends Window
{
	private static final Logger log = Logger.getLogger(GroupSelectBox.class);
	
	public ModalAddNewsletter(final NewsletterList nl)
	{
		super();

		setModal(true);
		setWidth("600px");
		setClosable(false);

		Panel rootPanel = new Panel("Add new Newsletter");
		final VerticalLayout verticalLayout = new VerticalLayout();
		final SenderSelectBox senderSelectBox = new SenderSelectBox();
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		final GroupSelectBox groupSelectBox = new GroupSelectBox();
		final UploadTextFileComponent uploadTextfile = new UploadTextFileComponent();
		final UploadHtmlFileComponent uploadHtmlfile = new UploadHtmlFileComponent();
		final TextField tfSubject = new TextField("Subject");
		final DateField tfSendDate = new DateField("Send Date");
		final Button cancelButton = new Button("Cancel");
		final Button saveButton = new Button("Send");

		tfSendDate.setInvalidAllowed(false);
		tfSendDate.setResolution(DateField.RESOLUTION_MIN);
		tfSendDate.setValue(new Date());


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
				boolean hasError = false;
				// Validation
				if (tfSubject.getValue().equals(""))
				{
					hasError = true;
					tfSubject.setComponentError(new UserError("Subject cannot be empty"));
				}
				else
				{
					tfSubject.setComponentError(null);
				}

				if (tfSendDate.getValue() == null)
				{
					hasError = true;
					tfSendDate.setComponentError(new UserError("Date cannot be empty"));
				}
				else
				{
					tfSendDate.setComponentError(null);
				}

				if (!uploadTextfile.isUploadFinished())
				{
					hasError = true;
					uploadTextfile.setComponentError(new UserError("You must provide a text file"));
				}
				else
				{
					uploadTextfile.setComponentError(null);
				}

				if (!uploadHtmlfile.isUploadFinished())
				{
					hasError = true;
					uploadHtmlfile.setComponentError(new UserError("You must provide a html file"));
				}
				else
				{
					uploadHtmlfile.setComponentError(null);
				}

				if (senderSelectBox.getSelectedSender() == 0)
				{
					hasError = true;
					senderSelectBox.setComponentError(new UserError("You must select a sender"));
				}
				else
				{
					senderSelectBox.setComponentError(null);
				}

				if (groupSelectBox.getSelectedGroup() == 0)
				{
					hasError = true;
					groupSelectBox.setComponentError(new UserError("You must select a recipient group"));
				}
				else
				{
					groupSelectBox.setComponentError(null);
				}
				log.debug("Has Error: " + hasError);
				if (!hasError)
				{
					log.info("No validation errors found, processing request");
					long mailId = Util.generateId();
					try
					{
						Sender s =new Sender(senderSelectBox.getSelectedSender());
						String text = uploadTextfile.getTextData();
						String html = uploadHtmlfile.getHtmlData();

						Mail m = new Mail(mailId, text, html, (Date)tfSendDate.getValue(), tfSubject.getValue().toString(), s);

						QueueNewsletter queueNewsletter = new QueueNewsletter();
						queueNewsletter.queueNewsletter(m, s, new RecipientGroup(groupSelectBox.getSelectedGroup()));

						event.getButton().getWindow().setVisible(false);
						event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
						event.getButton().getWindow().getParent().showNotification("Queued successfully", Notification.TYPE_HUMANIZED_MESSAGE);

						nl.getBeanContainer().addItem(m.getId(), m);

					}
					catch (RuntimeException e)
					{
						log.error("RuntimeException", e);
						event.getButton().getApplication().getMainWindow().showNotification("An error occured: " + e.getLocalizedMessage(), Notification.TYPE_ERROR_MESSAGE);
					}
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
