package com.trivago.mail.pigeon.web.components.mail;

import com.invient.vaadin.charts.InvientCharts;
import com.invient.vaadin.charts.InvientChartsConfig;
import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.storage.RelationTypes;
import com.vaadin.ui.CustomComponent;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.LinkedHashSet;

/**
 * Created by IntelliJ IDEA.
 * User: mmueller
 * Date: 28.10.11
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class MailOpenChart extends CustomComponent
{
	public MailOpenChart(Mail mail)
	{

		InvientChartsConfig chartConfig = new InvientChartsConfig();
        chartConfig.getGeneralChartConfig().setType(InvientCharts.SeriesType.PIE);

        chartConfig.getTitle().setText(
                "Opened mail statistics");

        chartConfig
                .getTooltip()
                .setFormatterJsFunc(
                        "function() {"
                                + " return '<b>'+ this.point.name +'</b>: '+ this.y +' mails'; "
                                + "}");

        InvientChartsConfig.PieConfig pie = new InvientChartsConfig.PieConfig();
        pie.setAllowPointSelect(true);
        pie.setCursor("pointer");
        pie.setDataLabel(new InvientChartsConfig.PieDataLabel(false));
        pie.setShowInLegend(true);
        chartConfig.addSeriesConfig(pie);

        InvientCharts chart = new InvientCharts(chartConfig);
        chart.addSeries(getChartData(mail.getId()));
		chart.setVisible(true);
		setCompositionRoot(chart);
	}

	private InvientCharts.XYSeries getChartData(long newsletterId)
	{

		Mail m = new Mail(newsletterId);
		InvientCharts.XYSeries series = new InvientCharts.XYSeries("Browser Share");
		LinkedHashSet<InvientCharts.DecimalPoint> points = new LinkedHashSet<InvientCharts.DecimalPoint>();
		InvientChartsConfig.PointConfig config = new InvientChartsConfig.PointConfig(true);
		int allRecipients = 0;
		int openMails = 0;
		int bounces = 0;

		Iterable<Relationship> recipients = m.getRecipients();
		for (Relationship relationship : recipients)
		{
			++allRecipients;
			Recipient r = new Recipient(relationship.getEndNode());

			Node mailNode = m.getDataNode();
			Node userNode = r.getDataNode();

			Iterable<Relationship> relationships = userNode.getRelationships(RelationTypes.OPENED);
			for (Relationship rel : relationships)
			{
				if(rel.getEndNode().equals(mailNode))
				{
					++openMails;
					break;
				}
			}

			Iterable<Relationship> bounceRel = mailNode.getRelationships(RelationTypes.BOUNCED_MAIL);
			for (Relationship rel : bounceRel)
			{
				++bounces;
			}
		}

		points.add(new InvientCharts.DecimalPoint(series, "Opened", openMails, config));
		points.add(new InvientCharts.DecimalPoint(series, "Unknown", (allRecipients-openMails-bounces)));
		points.add(new InvientCharts.DecimalPoint(series, "Bounces", bounces));
		series.setSeriesPoints(points);
		return series;
	}
}
