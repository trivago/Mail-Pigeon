package com.trivago.mail.pigeon.web.components.mail;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UploadTextFileComponent extends CustomComponent
		implements Upload.SucceededListener,
				   Upload.FailedListener,
				   Upload.Receiver
{

	Panel root;
	File textFile;
	Label statusLabel;
	private boolean uploadFinished = false;
	private Label label;
	private Upload upload;

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
		FileOutputStream fos = null; // Output stream to write to
		textFile = new File("/tmp/uploads/txt_" + filename);
		try
		{
			// Open the file for writing.
			fos = new FileOutputStream(textFile);
		}
		catch (final java.io.FileNotFoundException e)
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

	public File getTextFile()
	{
		return textFile;
	}

	public boolean isUploadFinished()
	{
		return uploadFinished;
	}
}
