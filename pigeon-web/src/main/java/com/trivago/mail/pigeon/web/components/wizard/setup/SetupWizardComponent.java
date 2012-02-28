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
package com.trivago.mail.pigeon.web.components.wizard.setup;

import com.trivago.mail.pigeon.web.MainApp;
import com.trivago.mail.pigeon.web.components.wizard.setup.steps.WizardAddRecipientGroupComponent;
import com.trivago.mail.pigeon.web.components.wizard.setup.steps.WizardAddSenderComponent;
import com.trivago.mail.pigeon.web.components.wizard.setup.steps.WizardFinishedComponent;
import com.trivago.mail.pigeon.web.components.wizard.setup.steps.WizardGreetingPageComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.*;

public class SetupWizardComponent extends CustomComponent implements WizardProgressListener
{
	private final Wizard wizard;

	public SetupWizardComponent()
	{
		wizard = new Wizard();
		wizard.addStep(new WizardGreetingPageComponent(), "start");
		wizard.addStep(new WizardAddSenderComponent(), "sender");
		wizard.addStep(new WizardAddRecipientGroupComponent(), "recipientgroup");
		wizard.addStep(new WizardFinishedComponent(), "done");


		wizard.setUriFragmentEnabled(false);
		wizard.addListener(this);
		wizard.setHeight("600px");
		wizard.setWidth("800px");

		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setMargin(true);


		vl.addComponent(wizard);
		vl.setComponentAlignment(wizard, Alignment.TOP_CENTER);
		setCompositionRoot(vl);
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent wizardStepActivationEvent)
	{
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent wizardStepSetChangedEvent)
	{
	}

	@Override
	public void wizardCompleted(WizardCompletedEvent wizardCompletedEvent)
	{
		wizard.setVisible(false);
		MainApp app = ((MainApp) wizardCompletedEvent.getComponent().getApplication());
		app.initMenu();
		app.setDashBoard();
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent wizardCancelledEvent)
	{
		wizard.setVisible(false);
		MainApp app = ((MainApp) wizardCancelledEvent.getComponent().getApplication());
		app.initMenu();
		app.setDashBoard();
	}
}
