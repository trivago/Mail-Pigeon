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

import com.vaadin.terminal.UserError;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;

import java.io.IOException;
import java.io.OutputStream;

public class UploadHtmlFileComponent extends CustomComponent
		implements Upload.SucceededListener,
				   Upload.FailedListener,
				   Upload.Receiver
{

	Panel root;

	boolean uploadFinished = false;
	private Label label;
	private Upload upload;

	private OutputStream fos;

	public UploadHtmlFileComponent()
	{
		root = new Panel("Upload HTML Version");
		setCompositionRoot(root);

		// Create the Upload component.
		upload = new Upload("HTML-File", this);

		// Use a custom button caption instead of plain "Upload".
		upload.setButtonCaption("Upload Now");

		// Listen for events regarding the success of upload.
		upload.addListener((Upload.SucceededListener) this);
		upload.addListener((Upload.FailedListener) this);

		root.addComponent(upload);
		root.addComponent(new Label("Click 'Browse' to " +
				"select a file and then click 'Upload'."));
	}

	@Override
	public void uploadFailed(Upload.FailedEvent event)
	{

		label = new Label("Uploading "
				+ event.getFilename() + " of type '"
				+ event.getMIMEType() + "' failed.");
		root.addComponent(label);
		upload.setComponentError(new UserError("Upload failed!"));
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType)
	{
		label = new Label();
		try
		{
			// Open the file for writing.
			fos = new OutputStream()
			{
				private StringBuilder string = new StringBuilder();
				@Override
				public void write(int b) throws IOException
				{
					this.string.append((char) b );
				}

				public String toString(){
					return this.string.toString();
				}
			};

		}
		catch (final Exception e)
		{
			// Error while opening the file. Not reported here.
			e.printStackTrace();
			return null;
		}

		return fos; // Return the output stream to write to
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent event)
	{
		// Log the upload on screen.
		label = new Label("File " + event.getFilename()
				+ " of type '" + event.getMIMEType()
				+ "' uploaded.");
		root.addComponent(label);
		uploadFinished = true;
	}

	public String getHtmlData()
	{
		return fos.toString();
	}

	public boolean isUploadFinished()
	{
		return uploadFinished;
	}
}
