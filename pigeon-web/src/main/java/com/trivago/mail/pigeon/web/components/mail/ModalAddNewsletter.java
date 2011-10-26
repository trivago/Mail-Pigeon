package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.web.components.sender.SenderSelectBox;
import com.vaadin.data.Property;
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
		HorizontalLayout buttonLayout = new HorizontalLayout();
		
		TextField tfSubject = new TextField("Subject");
		tfSubject.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				subject = event.getProperty().getValue().toString();
			}
		});

		DateField tfSendDate = new DateField("Send Date");
		tfSendDate.setResolution(DateField.RESOLUTION_MIN);
		tfSendDate.addListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(Property.ValueChangeEvent event)
			{
				sendDate = (Date) event.getProperty().getValue();
			}
		});

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

		UploadTextFileComponent uploadTextfile = new UploadTextFileComponent();
		UploadHtmlFileComponent uploadHtmlfile = new UploadHtmlFileComponent();

		Button saveButton = new Button("Send");

		buttonLayout.addComponent(saveButton);
		buttonLayout.addComponent(cancelButton);

		verticalLayout.addComponent(tfSubject);
		verticalLayout.addComponent(tfSendDate);
		verticalLayout.addComponent(senderSelectBox);
		verticalLayout.addComponent(uploadTextfile);
		verticalLayout.addComponent(uploadHtmlfile);
		verticalLayout.addComponent(buttonLayout);

		rootPanel.addComponent(verticalLayout);
		this.addComponent(rootPanel);
	}
}
