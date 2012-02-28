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
package com.trivago.mail.pigeon.web.components.groups;

import com.trivago.mail.pigeon.importer.Csv;
import com.trivago.mail.pigeon.web.components.recipients.UploadCsvFileComponent;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.io.File;


public class ModalRecipientImportCsv extends Window
{
	private static final Logger log = Logger.getLogger(ModalRecipientImportCsv.class);

	public ModalRecipientImportCsv(final long groupId)
	{
		super();
		setModal(true);
		setClosable(false);
		setWidth("600px");

		Panel rootPanel = new Panel("Import Recipients via CSV");
		VerticalLayout vl = new VerticalLayout();

		Label infoText = new Label("You can upload a bunch of users into a group. Create a CSV file with no! headers. \n"
				+ "You can leave out the user_id by just setting the column empty => ,\"Firstname Lastname\",\"first.last@trivago.com\".");
		Label formatInfoText = new Label("Please format you CSV like: user_id,\"user_full_name\",\"email\"");

		final CheckBox forceUpdate = new CheckBox("Force update of existing recipients?");
		Button cancelButton = new Button("Cancel");

		vl.addComponent(infoText);
		vl.addComponent(formatInfoText);
		final UploadCsvFileComponent ucsv = new UploadCsvFileComponent();
		vl.addComponent(ucsv);

		Button importButton = new Button("Import");
		importButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				if (!ucsv.isUploadFinished())
				{
					ucsv.setComponentError(new UserError("Please upload a csv first!"));
				}
				else
				{
					ucsv.setComponentError(null);

					try
					{
						File uploadedFile = ucsv.getCsvFile();
						Csv importer = new Csv(uploadedFile, groupId);
						importer.importData(forceUpdate.booleanValue());
						event.getButton().getApplication().getMainWindow().showNotification("Import done", Notification.TYPE_HUMANIZED_MESSAGE);
						event.getButton().getWindow().setVisible(false);
						event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
						
					}
					catch (Exception e)
					{
						log.error("Error while importing csv", e);
						event.getButton().getWindow().showNotification("The import failed.", e.getLocalizedMessage(), Notification.TYPE_ERROR_MESSAGE);
					}
				}
			}
		});

		cancelButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				event.getButton().getWindow().setVisible(false);
				event.getButton().getWindow().getParent().removeComponent(event.getButton().getWindow());
			}
		});

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(importButton);
		buttonLayout.addComponent(cancelButton);

		vl.addComponent(buttonLayout);
		rootPanel.addComponent(vl);
		addComponent(rootPanel);
	}
}
