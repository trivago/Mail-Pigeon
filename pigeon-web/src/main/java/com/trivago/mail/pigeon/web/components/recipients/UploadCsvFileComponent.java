package com.trivago.mail.pigeon.web.components.recipients;


import com.vaadin.terminal.UserError;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UploadCsvFileComponent extends CustomComponent
		implements Upload.SucceededListener,
				   Upload.FailedListener,
				   Upload.Receiver
{
	private Panel root;
	private boolean uploadFinished = false;
	private Label label;
	private Upload upload;

	private File csvFile;

	private static final Logger log = Logger.getLogger(UploadCsvFileComponent.class);

	public UploadCsvFileComponent()
	{
		root = new Panel("Upload Text Version");
		setCompositionRoot(root);

		// Create the Upload component.
		upload = new Upload("CSV-File", this);

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
			csvFile = File.createTempFile("csv", "csv");
		}
		catch (IOException e)
		{
			log.error("Could not upload file", e);
		}
		FileOutputStream fos;
		try
		{
			// Open the file for writing.
			fos = new FileOutputStream(csvFile);
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

	public File getCsvFile()
	{
		return csvFile;
	}

	public boolean isUploadFinished()
	{
		return uploadFinished;
	}
}
