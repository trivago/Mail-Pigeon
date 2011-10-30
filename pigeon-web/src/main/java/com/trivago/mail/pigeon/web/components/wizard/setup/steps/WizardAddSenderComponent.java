package com.trivago.mail.pigeon.web.components.wizard.setup.steps;

import com.trivago.mail.pigeon.bean.Sender;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Date;


public class WizardAddSenderComponent implements WizardStep
{

	private TextField tfName;
	private VerticalLayout verticalLayout;
	private Panel rootPanel;
	private TextField tfFromMail;
	private TextField tfReplyTo;

	@Override
	public String getCaption()
	{
		return "Add a sender";
	}

	@Override
	public Component getContent()
	{
		rootPanel = new Panel("Add new Sender");
		verticalLayout = new VerticalLayout();
		Label label = new Label("The sender is a person or group that is shown as the sender of the newsletter.");
		tfName = new TextField("Name");
		tfFromMail = new TextField("From E-Mail");
		tfReplyTo = new TextField("ReplyTo E-Mail");

		verticalLayout.addComponent(label);
		verticalLayout.addComponent(tfName);
		verticalLayout.addComponent(tfFromMail);
		verticalLayout.addComponent(tfReplyTo);
		rootPanel.addComponent(verticalLayout);
		return rootPanel;
	}

	@Override
	public boolean onAdvance()
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

			long senderId = Math.round(new Date().getTime() * Math.random());
			try
			{
				Sender s = new Sender(senderId, tfFromMail.getValue().toString(), tfReplyTo.getValue().toString(), tfName.getValue().toString());
				// The sender select could be placed anywhere but must be reloaded to reflect the changes.
				//SenderSelectBox.reloadSelect();

			}
			catch (RuntimeException e)
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}
}
