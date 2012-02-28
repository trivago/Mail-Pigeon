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
package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Mail;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

public class PopupActiveNewsletters extends com.vaadin.ui.Window
{
	private static final Logger log = Logger.getLogger(PopupActiveNewsletters.class);


	public PopupActiveNewsletters()
	{
		setClosable(false);
		setWidth("400px");

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);

		Button closeButton = new Button("Close");
		vl.addComponent(closeButton);

		IndexHits<Node> all = Mail.getAll();
		for (Node mailNode : all)
		{
			Mail mail = new Mail(mailNode);
			if (!mail.isDone())
			{
				NewsletterProgressComponent pc = new NewsletterProgressComponent(mail);
				vl.addComponent(pc);
			}
		}

		closeButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				event.getButton().getWindow().setVisible(false);
				event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
			}
		});


		addComponent(vl);
	}
}
