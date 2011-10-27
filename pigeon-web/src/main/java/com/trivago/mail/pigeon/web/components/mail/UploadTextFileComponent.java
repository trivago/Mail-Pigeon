package com.trivago.mail.pigeon.web.components.mail;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UploadTextFileComponent extends CustomComponent
		implements Upload.SucceededListener,
				   Upload.FailedListener,
				   Upload.Receiver
{

	private Panel root;
	private boolean uploadFinished = false;
	private Label label;
	private Upload upload;

	private OutputStream fos;

	public UploadTextFileComponent()
	{
		root = new Panel("Upload Text Version");
		setCompositionRoot(root);

		// Create the Upload component.
		upload = new Upload("Text-File", this);

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

	public String getTextData()
	{
		return fos.toString();
	}

	public boolean isUploadFinished()
	{
		return uploadFinished;
	}
}
