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
package com.trivago.mail.pigeon.web;

import com.trivago.mail.pigeon.bean.RecipientGroup;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.trivago.mail.pigeon.web.components.groups.GroupList;
import com.trivago.mail.pigeon.web.components.mail.NewsletterList;
import com.trivago.mail.pigeon.web.components.menu.MenuBar;
import com.trivago.mail.pigeon.web.components.recipients.RecipientList;
import com.trivago.mail.pigeon.web.components.sender.SenderList;
import com.trivago.mail.pigeon.web.components.templates.TemplateList;
import com.trivago.mail.pigeon.web.components.wizard.setup.SetupWizardComponent;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletContext;
import java.util.NoSuchElementException;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MainApp extends Application
{
	private Window window;
	private MenuBar menu;
    
	@Override
	public void init()
	{
		//DOMConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.xml"));
//        BasicConfigurator.configure();
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
			menu = new MenuBar(this);
			window.addComponent(menu);
			setDashBoard();
		}

	}

	public void initMenu()
	{
		menu = new MenuBar(this);
		window.addComponent(menu);
	}

	public void clearWindow()
	{
		window.removeAllComponents();
		initMenu();
	}

	public void setDashBoard()
	{
		VerticalLayout dbLayout = new VerticalLayout();
		dbLayout.addComponent(new Label("Can I haz Dashboard?"));
		window.addComponent(dbLayout);

	}

	public void setNewsletterList()
	{
		NewsletterList newsletterList = new NewsletterList();
		VerticalLayout nlLayout = new VerticalLayout();
		nlLayout.addComponent(newsletterList);
		nlLayout.setMargin(true);
		clearWindow();
		window.addComponent(nlLayout);
	}

	public void setSenderList()
	{
		SenderList senderList = new SenderList();
		VerticalLayout slLayout = new VerticalLayout();
		slLayout.addComponent(senderList);
		slLayout.setMargin(true);
		clearWindow();
		window.addComponent(slLayout);
	}

	public void setRecipientGroupList()
	{
		GroupList groupList = new GroupList();
		VerticalLayout rgLayout = new VerticalLayout();
		rgLayout.addComponent(groupList);
		rgLayout.setMargin(true);
		clearWindow();
		window.addComponent(rgLayout);
	}

	public void setRecipientList()
	{
		RecipientList recipientList = new RecipientList();
		VerticalLayout rLayout = new VerticalLayout();
		rLayout.addComponent(recipientList);
		rLayout.setMargin(true);
		clearWindow();
		window.addComponent(rLayout);
	}

	public void setTemplateList()
	{
		TemplateList templateList = new TemplateList();
		VerticalLayout tlLayout = new VerticalLayout();
		tlLayout.addComponent(templateList);
		tlLayout.setMargin(true);
		clearWindow();
		window.addComponent(tlLayout);
	}

	public void startWizard()
	{
		SetupWizardComponent wb = new SetupWizardComponent();
		window.addComponent(wb);
	}

	public MenuBar getMenu()
	{
		return menu;
	}

	public void setMenu(MenuBar menu)
	{
		this.menu = menu;
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