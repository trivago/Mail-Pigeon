package com.trivago.mail.pigeon.web.components.wizard.setup.steps;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.WizardStep;

public class WizardGreetingPageComponent implements WizardStep
{
	@Override
	public String getCaption()
	{
		return "Introduction";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout content = new VerticalLayout();
		StringBuilder sb = new StringBuilder("<h2>Mail Pidgeon</h2><p>");
		sb.append("This is the setup wizard, which guides you through the initial steps of this application.");
		sb.append("</p>");
		Label text = new Label(sb.toString(), Label.CONTENT_XHTML);

		content.addComponent(text);
		return content;
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}
}
