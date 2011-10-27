package com.trivago.mail.pigeon.web.components.mail;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UploadHtmlFileComponent extends CustomComponent
		implements Upload.SucceededListener,
				   Upload.FailedListener,
				   Upload.Receiver
{

	Panel root;
	File htmlFile;

	boolean uploadFinished = false;
	private Label label;
	private Upload upload;

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
		FileOutputStream fos = null; // Output stream to write to
		htmlFile = new File("/tmp/uploads/html_" + filename);
		try
		{
			// Open the file for writing.
			fos = new FileOutputStream(htmlFile);
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

	public File getHtmlFile()
	{
		return htmlFile;
	}

	public boolean isUploadFinished()
	{
		return uploadFinished;
	}
}
