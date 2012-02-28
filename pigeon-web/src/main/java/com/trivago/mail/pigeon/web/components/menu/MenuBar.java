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
package com.trivago.mail.pigeon.web.components.menu;

import com.trivago.mail.pigeon.web.MainApp;

import java.lang.reflect.InvocationTargetException;

public class MenuBar extends com.vaadin.ui.MenuBar
{
	private MainApp app;

	private Command command;

	public MenuBar(MainApp app)
	{
		super();
		this.app = app;

		command = new MenuBar.Command()
		{
			public void menuSelected(MenuItem selectedItem)
			{
				changeContentByMenuId(selectedItem.getDescription());
			}
		};
		setWidth("972px");

		initMenu();

	}

	private void initMenu()
	{
		// Top Items:
		MenuItem newsletter = addItem("Newsletter", null);
		MenuItem recipient = addItem("Recipient", null);
		MenuItem sender = addItem("Sender", null);

		newsletter.addItem("List", command).setDescription("NewsletterList");
		newsletter.addItem("Templates", command).setDescription("TemplateList");
		recipient.addItem("List", command).setDescription("RecipientList");
		recipient.addItem("Groups", command).setDescription("RecipientGroupList");
		sender.addItem("List", command).setDescription("SenderList");
	}

	public void changeContentByMenuId(String menuId)
	{
		try
		{
			String methodName = "set" + menuId;
			Class[] types = new Class[] {};
			app.getClass().getMethod(methodName, types).invoke(app);
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
		}
	}
}
