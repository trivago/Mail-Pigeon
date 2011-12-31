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
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
	}
}
