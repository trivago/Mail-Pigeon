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

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Date;

public class WizardAddRecipientGroupComponent implements WizardStep
{

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
		Panel rootPanel = new Panel("Add new group");
		final VerticalLayout verticalLayout = new VerticalLayout();
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
