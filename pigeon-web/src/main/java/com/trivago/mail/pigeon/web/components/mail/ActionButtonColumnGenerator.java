package com.trivago.mail.pigeon.web.components.mail;

import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.storage.ConnectionFactory;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;


public class ActionButtonColumnGenerator implements Table.ColumnGenerator
{
	private final Logger log = Logger.getLogger(ActionButtonColumnGenerator.class);
	@Override
	public Object generateCell(final Table source, final Object itemId, final Object columnId)
	{
		HorizontalLayout hl = new HorizontalLayout();
		Button showNlConentButton = new Button("View");
		showNlConentButton.setImmediate(true);
		showNlConentButton.setIcon(new ThemeResource("../runo/icons/16/document-txt.png"));

		showNlConentButton.addListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(Button.ClickEvent event)
			{
				Mail m = new Mail((Long) itemId);
				Window nlConentView = new Window("Newsletter Contents of ID " + itemId);
				// Create an empty tab sheet.
				TabSheet tabsheet = new TabSheet();

				Panel pText = new Panel("Text Content");
				Panel pHtml = new Panel("Text Content");
				RichTextArea textArea = new RichTextArea ();
				textArea.setValue(m.getText());
				textArea.setReadOnly(true);
				

				RichTextArea richTextArea = new RichTextArea();
				richTextArea.setValue(m.getHtml());
				richTextArea.setReadOnly(true);

				pText.addComponent(textArea);
				pHtml.addComponent(richTextArea);


				richTextArea.setHeight("50%");
				richTextArea.setWidth("100%");
				textArea.setHeight("50%");
				textArea.setWidth("100%");

				nlConentView.setResizable(true);
				nlConentView.setWidth("800px");
				nlConentView.setHeight("600px");

				tabsheet.addTab(pText);
				tabsheet.getTab(pText).setCaption("Text Version");
				tabsheet.addTab(pHtml);
				tabsheet.getTab(pHtml).setCaption("Html Version");
				
				nlConentView.addComponent(tabsheet);
				source.getWindow().addWindow(nlConentView);
				nlConentView.setVisible(true);
			}
		});

		hl.addComponent(showNlConentButton);
		return hl;
	}
}
