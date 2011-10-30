package com.trivago.mail.pigeon.web.components.wizard.setup.steps;


import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.WizardStep;

public class WizardFinishedComponent implements WizardStep
{
	@Override
	public String getCaption()
	{
		return "Finished";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout content = new VerticalLayout();
		StringBuilder sb = new StringBuilder("<h2>Completed</h2><p>");
		sb.append("Thank you for your patience. You can now start using Mail Pidgeon");
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
		return false;
	}
}
