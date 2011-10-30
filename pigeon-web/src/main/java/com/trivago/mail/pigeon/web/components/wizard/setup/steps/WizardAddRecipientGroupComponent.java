package com.trivago.mail.pigeon.web.components.wizard.setup.steps;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Date;

public class WizardAddRecipientGroupComponent implements WizardStep
{

	private Panel rootPanel;
	private VerticalLayout verticalLayout;
	private TextField tfName;

	@Override
	public String getCaption()
	{
		return "Add a Recipient Group";
	}

	@Override
	public Component getContent()
	{
		Label label = new Label("<p>The Recipient Group is the highest order of collection recipients. "
				+ "You need a Recipient Group in order to send a Newsletter.</p>", Label.CONTENT_XHTML);
		rootPanel = new Panel("Add new group");
		verticalLayout = new VerticalLayout();
		tfName = new TextField("Name");
		verticalLayout.addComponent(label);
		verticalLayout.addComponent(tfName);
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

		if (!tfName.getValue().equals(""))
		{
			tfName.setComponentError(null);

			long grou_id = Math.round(new Date().getTime() * Math.random());

			try
			{
				RecipientGroup g = new RecipientGroup(grou_id, tfName.getValue().toString());
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
		return false;
	}
}
