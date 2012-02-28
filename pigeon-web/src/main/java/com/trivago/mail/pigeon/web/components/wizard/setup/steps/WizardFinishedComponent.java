/**
 * Copyright (C) 2011-2012 trivago GmbH <mario.mueller@trivago.com>, <christian.krause@trivago.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
