/*
 * RecipientSelectionPanel
 *
 * Version $Id:$
 *
 * 2011-10-17 1.0-SNAPSHOT
 */
package com.trivago.mail.pigeon.web.components;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.ui.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Panel Component to select the bunch of users who deserve a newsletter.
 *
 * @author Mario Mueller
 * @since 1.0-SNAPSHOT
 */
public class RecipientSelectionPanel extends CustomComponent
{
	private String localeCode;

	private String maxLevel;

	private String minLevel;

	private Date minRegDate;

	private Date maxRegDate;

	private Date minLastLogin;

	private Date maxLastLogin;

	private int minPoints;

	private int maxPoints;

	private int minReviews;

	private int maxReviews;

	/**
	 * Contructs the panel. You have to attach it to main window yourself.
	 *
	 * Each field is represented by a class member that gets its value from the event listener that
	 * is attached to each field. This might sound a bit 
	 * 
	 * @param application The Main Application
	 */
	public RecipientSelectionPanel(final Application application)
	{
		Panel panel = new Panel("Mail Recipient Collector");
		HorizontalLayout baseLayout = new HorizontalLayout();
		final Layout verticalLayoutLeft = new VerticalLayout();
		final Layout verticalLayoutRight = new VerticalLayout();

		baseLayout.addComponent(verticalLayoutLeft);
		baseLayout.addComponent(verticalLayoutRight);

		verticalLayoutLeft.setMargin(true);
		verticalLayoutRight.setMargin(true);

		final TextField tfLocaleCode = new TextField("Locale Code");
		tfLocaleCode.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				localeCode = event.getProperty().getValue().toString();
			}
		});
		verticalLayoutLeft.addComponent(tfLocaleCode);


		final TextField tfMinLevel = new TextField("Min Level");
		tfMinLevel.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				minLevel = event.getProperty().getValue().toString();
			}
		});
		verticalLayoutLeft.addComponent(tfMinLevel);


		final TextField tfMaxLevel = new TextField("Max Level");
		tfMaxLevel.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				maxLevel = event.getProperty().getValue().toString();
			}
		});
		verticalLayoutLeft.addComponent(tfMaxLevel);


		final DateField tfMinRegDate = new DateField("Min Register Date");
		tfMinRegDate.setDateFormat("yyyy-MM-dd H:m:s");
		tfMinRegDate.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				minRegDate = (Date) event.getProperty().getValue();
			}
		});
		verticalLayoutLeft.addComponent(tfMinRegDate);


		final DateField tfMaxRegDate = new DateField("Max Register Date");
		tfMaxRegDate.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				maxRegDate = (Date) event.getProperty().getValue();
			}
		});
		verticalLayoutLeft.addComponent(tfMaxRegDate);


		final DateField tfMinLastLoginDate = new DateField("Min Last LogIn");
		tfMinLastLoginDate.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				minLastLogin = (Date) event.getProperty().getValue();
			}
		});
		verticalLayoutLeft.addComponent(tfMinLastLoginDate);


		final DateField tfMaxLastLoginDate = new DateField("Max Last LogIn");
		tfMaxLastLoginDate.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				maxLastLogin = (Date) event.getProperty().getValue();
			}
		});
		verticalLayoutLeft.addComponent(tfMaxLastLoginDate);



		final TextField tfMinPoints = new TextField("Min Points");
		tfMinPoints.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				minPoints = Integer.parseInt((String) event.getProperty().getValue());
			}
		});
		verticalLayoutRight.addComponent(tfMinPoints);


		final TextField tfMaxPoints = new TextField("Max Points");
		tfMaxPoints.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				maxPoints = Integer.parseInt((String) event.getProperty().getValue());
			}
		});
		verticalLayoutRight.addComponent(tfMaxPoints);

		final TextField tfMinReviews = new TextField("Min Reviews");
		tfMinReviews.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				minReviews = Integer.parseInt((String) event.getProperty().getValue());
			}
		});
		verticalLayoutRight.addComponent(tfMinReviews);

		final TextField tfMaxReviews = new TextField("Max Reviews");
		tfMaxReviews.addListener(new Property.ValueChangeListener()
		{
			public void valueChange(Property.ValueChangeEvent event)
			{
				maxReviews = Integer.parseInt((String) event.getProperty().getValue());
			}
		});
		verticalLayoutRight.addComponent(tfMaxReviews);


		final Button runQuery = new Button("Run Query");
		runQuery.addListener(new Button.ClickListener()
		{

			public void buttonClick(Button.ClickEvent event)
			{
				application.getMainWindow().showNotification("Info: " + minRegDate.toString());
			}
		});

		verticalLayoutRight.addComponent(runQuery);
		
		panel.addComponent(baseLayout);
		setCompositionRoot(panel);
	}

}
