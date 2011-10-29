/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.trivago.mail.pigeon.web;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.web.components.groups.GroupList;
import com.trivago.mail.pigeon.web.components.mail.NewsletterList;
import com.trivago.mail.pigeon.web.components.sender.SenderList;
import com.trivago.mail.pigeon.web.components.wizzard.WizardBaseComponent;
import com.vaadin.Application;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.NoSuchElementException;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MainApp extends Application
{
	private Window window;

	@Override
	public void init()
	{
		DOMConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.xml"));
		window = new Window("Mail Pigeon");
		setMainWindow(window);

		int senderSize;
		try
		{
			senderSize = Sender.getAll().size();
		} catch (NoSuchElementException e)
		{
			senderSize = 0;
		}

		int recipientGroupSize;
		try
		{
			recipientGroupSize = RecipientGroup.getAll().size();
		} catch (NoSuchElementException e)
		{
			recipientGroupSize = 0;
		}

		if (senderSize == 0 && recipientGroupSize == 0)
		{
			startWizard();
		}
		else
		{
			setNormalComponents();
		}

	}

	public void setNormalComponents()
	{
		GroupList groupList = new GroupList();
		//RecipientList recipientList = new RecipientList();
		NewsletterList newsletterList = new NewsletterList();
		SenderList senderList = new SenderList();

		TabSheet tabSheet = new TabSheet();

		VerticalLayout nlLayout = new VerticalLayout();
		nlLayout.addComponent(newsletterList);
		nlLayout.setMargin(true);
		tabSheet.addTab(nlLayout).setCaption("Newsletter");

//		VerticalLayout rLayout = new VerticalLayout();
//		rLayout.addComponent(recipientList);
//		rLayout.setMargin(true);
//		tabSheet.addTab(rLayout).setCaption("Recipients");

		VerticalLayout rgLayout = new VerticalLayout();
		rgLayout.addComponent(groupList);
		rgLayout.setMargin(true);
		tabSheet.addTab(rgLayout).setCaption("Recipient Groups");

		VerticalLayout slLayout = new VerticalLayout();
		slLayout.addComponent(senderList);
		slLayout.setMargin(true);
		tabSheet.addTab(slLayout).setCaption("Sender List");

		window.addComponent(tabSheet);
	}

	public void startWizard()
	{
		WizardBaseComponent wb = new WizardBaseComponent();
		window.addComponent(wb);
	}


	@Override
	public void close()
	{
		super.close();
		// Registers a shutdown hook for the Neo4j and index service instances
		// so that it shuts down nicely when the VM exits (even if you
		// "Ctrl-C" the running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				Logger.getLogger(MainApp.class).info("Shutdown hook called");
				ConnectionFactory.getDatabase().shutdown();
			}
		});

	}
}