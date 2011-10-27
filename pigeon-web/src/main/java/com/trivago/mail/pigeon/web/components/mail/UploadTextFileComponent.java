package com.trivago.mail.pigeon.web.components.mail;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mmueller
 * Date: 26.10.11
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class UploadTextFileComponent extends CustomComponent
		implements Upload.SucceededListener,
				   Upload.FailedListener,
				   Upload.Receiver
{

	Panel root;
	File textFile;
	Label statusLabel;

	public UploadTextFileComponent()
	{
		root = new Panel("Upload Text Version");
		setCompositionRoot(root);

		// Create the Upload component.
		final Upload upload = new Upload("Text-File", this);

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
		// Log the failure on screen.
		root.addComponent(new Label("Uploading "
				+ event.getFilename() + " of type '"
				+ event.getMIMEType() + "' failed."));
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType)
	{
		FileOutputStream fos = null; // Output stream to write to
		textFile = new File("/tmp/uploads/" + filename);
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
		root.addComponent(new Label("File " + event.getFilename()
				+ " of type '" + event.getMIMEType()
				+ "' uploaded."));
	}

	public File getTextFile()
	{
		return textFile;
	}
}
