package com.trivago.mail.pigeon.web.components.templates;

import com.trivago.mail.pigeon.bean.MailTemplate;
import com.trivago.mail.pigeon.storage.Util;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;


public class ModalAddTemplate extends Window
{
    public ModalAddTemplate(final TemplateList tl)
    {
        this(tl, null);
    }

	public ModalAddTemplate(final TemplateList tl, final Long templateId)
	{
		setResizable(true);
		setWidth("972px");
		setHeight("700px");
		Panel rootPanel = new Panel("Add new Template");
		TabSheet tSheet = new TabSheet();
		HorizontalLayout hLayout = new HorizontalLayout();

		final TextField title = new TextField("Template description");
		final TextField subject = new TextField("Newsletter Subject");

		final TextArea textContent = new TextArea("Text Version");
		textContent.setRows(40);
		textContent.setColumns(100);

		final CKEditorTextField htmlContent = new CKEditorTextField();
		htmlContent.setWidth("100%");
		htmlContent.setHeight("650px");


        // Load the content, if we receive a template id
        if (templateId != null)
        {
            MailTemplate mt = new MailTemplate(templateId);
            title.setValue(mt.getTitle());
            subject.setValue(mt.getSubject());
            textContent.setValue(mt.getText());
            htmlContent.setValue(mt.getHtml());
        }


		Button saveButton = new Button("Save");
		Button cancel = new Button("Cancel");


		saveButton.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				boolean hasError = false;
				if (title.getValue().equals(""))
				{
					title.setComponentError(new UserError("Name must not be empty"));
					hasError = true;
				}
				else
				{
					title.setComponentError(null);
				}

				if (subject.getValue().equals(""))
				{
					subject.setComponentError(new UserError("Subject must not be empty"));
					hasError = true;
				}
				else
				{
					subject.setComponentError(null);
				}

				if (htmlContent.getValue().equals(""))
				{
					htmlContent.setComponentError(new UserError("Please provide some HTML content"));
					hasError = true;
				}
				else
				{
					htmlContent.setComponentError(null);
				}

				if (textContent.getValue().equals(""))
				{
					textContent.setComponentError(new UserError("Please provide some text content"));
					hasError = true;
				}
				else
				{
					textContent.setComponentError(null);
				}

				if (!hasError)
				{
                    if (templateId == null)
                    {
                        long templateId = Util.generateId();
                        try
                        {
                            MailTemplate mt = new MailTemplate(templateId, title.getValue().toString(), textContent.getValue().toString(), htmlContent.getValue().toString(), subject.getValue().toString());
                            event.getButton().getWindow().setVisible(false);
                            event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
                            event.getButton().getWindow().getParent().showNotification("Saved successfully", Notification.TYPE_HUMANIZED_MESSAGE);
                            tl.getBeanContainer().addItem(mt.getId(), mt);
                        }
                        catch (RuntimeException e)
                        {
                            // Should never happen ... hopefully :D
                        }
                    }
                    else
                    {
                        MailTemplate mt = new MailTemplate(templateId);

                        mt.setHtml(htmlContent.getValue().toString());
                        mt.setSubject(subject.getValue().toString());
                        mt.setText(textContent.getValue().toString());
                        mt.setTitle(title.getValue().toString());

                        event.getButton().getWindow().setVisible(false);
                        event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
                        event.getButton().getWindow().getParent().showNotification("Saved successfully", Notification.TYPE_HUMANIZED_MESSAGE);

                        final int beanIndex = tl.getBeanContainer().indexOfId(mt.getId());
                        tl.getBeanContainer().removeItem(mt.getId());
                        tl.getBeanContainer().addItemAt(beanIndex, mt.getId(), mt);
                    }
                    TemplateSelectBox.reloadSelect();
				}
			}
		});


		hLayout.addComponent(saveButton);
		hLayout.addComponent(cancel);
		hLayout.setSpacing(true);

		VerticalLayout metaDataLayout = new VerticalLayout();

		Panel textFieldPanel = new Panel("Meta Data");
		VerticalLayout metaLayout = new VerticalLayout();
		metaLayout.addComponent(title);
		metaLayout.addComponent(subject);
		textFieldPanel.addComponent(metaLayout);

		Panel helpPanel = new Panel("Template Documentation");
		assembleHelpComponents(helpPanel);
		
		metaDataLayout.addComponent(textFieldPanel);
		metaDataLayout.addComponent(helpPanel);

		tSheet.addTab(metaDataLayout).setCaption("Meta Data");

		VerticalLayout textLayout = new VerticalLayout();
		textLayout.addComponent(textContent);
		tSheet.addTab(textLayout).setCaption("Text Content");

		VerticalLayout htmlLayout = new VerticalLayout();
		htmlLayout.addComponent(htmlContent);
		tSheet.addTab(htmlLayout).setCaption("HTML Content");

		rootPanel.addComponent(tSheet);
		rootPanel.addComponent(hLayout);
		addComponent(rootPanel);
	}

	private void assembleHelpComponents(final Panel helpRootPanel)
	{
		VerticalLayout vHelpLayout = new VerticalLayout();

		Link velocityLink = new Link("Velocity User Guide", new ExternalResource("http://velocity.apache.org/engine/releases/velocity-1.7/user-guide.html"));
		velocityLink.setTargetName("_blank");
		Label helpText1 = new Label("<p>You can find the basic template language user guide at the vendors homepage.</p>", Label.CONTENT_XHTML);
		Label helpText2 = new Label("<p>The standard set of commands is enriched with our application specific variables."
				+ "Those will help you to get the newsletter personalized. See below for a list of variables and and macros.</p>", Label.CONTENT_XHTML);


		Accordion accordion = new Accordion();

		// Sender
		StringBuilder senderText = new StringBuilder("<dl>");
		senderText.append("<dt>sender.uuid.id</dt><dd>The unique id of the sender</dd>");
		senderText.append("<dt>sender.name</dt><dd>The name of the sender</dd>");
		senderText.append("<dt>sender.email.from</dt><dd>The 'from' eMail of the sender</dd>");
		senderText.append("<dt>sender.email.replyto</dt><dd>The reply-to eMail of the sender</dd>");
		senderText.append("</dl>");
		Label senderLabel = new Label(senderText.toString(), Label.CONTENT_XHTML);
		VerticalLayout senderBox = new VerticalLayout();
		senderBox.addComponent(senderLabel);
		senderBox.setMargin(true);
		accordion.addTab(senderBox).setCaption("Sender");

		// Recipient
		StringBuilder recipientText = new StringBuilder("<dl>");
		recipientText.append("<dt>recipient.uuid.id</dt><dd>The unique id of the recipient</dd>");
		recipientText.append("<dt>recipient.uuid.external</dt><dd>The external (e.g. imported from the live system) id of the recipient</dd>");
		recipientText.append("<dt>recipient.name</dt><dd>The name of the recipient</dd>");
		recipientText.append("<dt>recipient.email</dt><dd>The eMail of the recipient</dd>");
		recipientText.append("<dt>recipient.name.title</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("<dt>recipient.name.first</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("<dt>recipient.name.last</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("<dt>recipient.gender</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("<dt>recipient.birthday</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("<dt>recipient.language</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("<dt>recipient.location.city</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("<dt>recipient.location.country</dt><dd>The reply-to eMail of the sender</dd>");
		recipientText.append("</dl>");
		Label recipientLabel = new Label(recipientText.toString(), Label.CONTENT_XHTML);
		VerticalLayout recipientBox = new VerticalLayout();
		recipientBox.addComponent(recipientLabel);
		recipientBox.setMargin(true);
		accordion.addTab(recipientBox).setCaption("Recipient");

		// Mail
		StringBuilder mailText = new StringBuilder("<dl>");
		mailText.append("<dt>recipient.uuid.id</dt><dd>The unique id of the recipient</dd>");
		mailText.append("<dt>recipient.uuid.external</dt><dd>The external (e.g. imported from the live system) id of the recipient</dd>");
		mailText.append("</dl>");
		Label mailLabel = new Label(mailText.toString(), Label.CONTENT_XHTML);
		VerticalLayout mailBox = new VerticalLayout();
		mailBox.addComponent(mailLabel);
		mailBox.setMargin(true);
		accordion.addTab(mailBox).setCaption("Mail");

		// Campaign
		StringBuilder campaignText = new StringBuilder("<dl>");
		campaignText.append("<dt>recipient.uuid.id</dt><dd>The unique id of the recipient</dd>");
		campaignText.append("<dt>recipient.uuid.external</dt><dd>The external (e.g. imported from the live system) id of the recipient</dd>");
		campaignText.append("<dt>recipient.name</dt><dd>The name of the recipient</dd>");
		campaignText.append("<dt>recipient.email</dt><dd>The eMail of the recipient</dd>");
		campaignText.append("<dt>sender.email.replyto</dt><dd>The reply-to eMail of the sender</dd>");
		campaignText.append("</dl>");
		Label campaignLabel = new Label(campaignText.toString(), Label.CONTENT_XHTML);
		VerticalLayout campaignBox = new VerticalLayout();
		campaignBox.addComponent(campaignLabel);
		campaignBox.setMargin(true);
		accordion.addTab(campaignBox).setCaption("Campaign");

		vHelpLayout.addComponent(helpText1);
		vHelpLayout.addComponent(velocityLink);
		vHelpLayout.addComponent(helpText2);
		vHelpLayout.addComponent(accordion);

		helpRootPanel.addComponent(vHelpLayout);
	}
}
