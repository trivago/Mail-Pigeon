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

import com.trivago.mail.pigeon.web.components.GroupManagementPanel;
import com.trivago.mail.pigeon.web.components.RecipientSelectionPanel;
import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletContext;

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
		DOMConfigurator.configure("log4j.xml");
        window = new Window("My Vaadin Application");
        setMainWindow(window);

		//RecipientSelectionPanel recipientSelectionPanel = new RecipientSelectionPanel(this);
		//window.addComponent(recipientSelectionPanel);
        
        GroupManagementPanel groupManagementPanel = new GroupManagementPanel(this);
		window.addComponent(groupManagementPanel);
    }
}
