package com.trivago.mail.pigeon.web.data.process;


import com.trivago.mail.pigeon.bean.Campaign;
import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.configuration.Settings;
import com.trivago.mail.pigeon.json.MailTransport;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.StringWriter;
import java.util.Set;

public class TemplateProcessor
{
	private VelocityEngine velocity;

	public TemplateProcessor()
	{
		velocity = new VelocityEngine();
	}

    public MailTransport processMail(Mail mail, Recipient recipient, Sender sender, Campaign campaign)
    {
        return processMail(mail, recipient, sender, campaign, false);
    }

	public MailTransport processMail(Mail mail, Recipient recipient, Sender sender, Campaign campaign, boolean autoReplaceCampaign)
	{
		MailTransport transport = new MailTransport();

		transport.setTo(recipient.getEmail());
		transport.setReplyTo(sender.getReplytoMail());
		transport.setFrom(sender.getFromMail());

		Context ctx = new VelocityContext();

		ctx.put("sender.uuid.id", sender.getId());
		ctx.put("sender.name", sender.getName());
		ctx.put("sender.email.from", sender.getFromMail());
		ctx.put("sender.email.replyto", sender.getReplytoMail());

		ctx.put("recipient.uuid.id", recipient.getId());
		ctx.put("recipient.uuid.external", recipient.getExternalId());
		
		ctx.put("recipient.email", recipient.getEmail());

		ctx.put("recipient.name.title", recipient.getTitle());
		ctx.put("recipient.name.first", recipient.getFirstname());
		ctx.put("recipient.name.last", recipient.getFirstname());

		ctx.put("recipient.gender", recipient.getGender().toString());
		ctx.put("recipient.birthday", recipient.getBirthday());

		ctx.put("recipient.language", recipient.getLanguage());
		ctx.put("recipient.location.city", recipient.getCity());
		ctx.put("recipient.location.country", recipient.getCountry());

		ctx.put("mail.date", mail.getSendDate());
		ctx.put("mail.uuid.id", mail.getId());

		ctx.put("campaign.id", campaign.getId());
		ctx.put("campaign.title", campaign.getTitle());
		ctx.put("campaign.params", campaign.getUrlParams());
		
		StringBuilder imageTag = new StringBuilder("<img src=\"");
		String trackingHost = Settings.create().getConfiguration().getString("tracking.url");
		imageTag.append(trackingHost).append("?user_id=").append(recipient.getId()).append("&newsletter_id=");
		imageTag.append(mail.getId()).append("\"/>");

		ctx.put("campaign.trackingpixel", imageTag.toString());

		String renderSubject = mail.getSubject();
		StringWriter outputSubject = new StringWriter();
		velocity.mergeTemplate("vm/macros/macroInclude.vm", "utf-8", ctx, outputSubject);
        velocity.evaluate( ctx, outputSubject, "subjectrender", renderSubject );


		String renderText = mail.getText();
        if (autoReplaceCampaign)
        {
            renderText = addCampaignToLinks(renderText, campaign.getUrlParams());
        }
		StringWriter outputText = new StringWriter();
		velocity.mergeTemplate("vm/macros/macroInclude.vm", "utf-8", ctx, outputText);
		velocity.evaluate( ctx, outputText, "textrender", renderText);

		String renderHtml = mail.getHtml();
        if (autoReplaceCampaign)
        {
            renderHtml = addCampaignToLinks(renderHtml, campaign.getUrlParams());
        }
		StringWriter outputHtml = new StringWriter();
		velocity.mergeTemplate("vm/macros/macroInclude.vm", "utf-8", ctx, outputHtml);
		velocity.evaluate(ctx, outputHtml, "htmlrender", renderHtml);


		transport.setSubject(outputSubject.toString());
		transport.setHtml(outputHtml.toString());
		transport.setText(outputText.toString());


		transport.setmId(String.valueOf(mail.getId()));
		transport.setuId(String.valueOf(recipient.getId()));

		transport.setSendDate(mail.getSendDate());

		return transport;
	}
    
    public String addCampaignToLinks(String content, String campaignParams)
    {
        Set<String> linkSet = LinkParser.parse(content);
        
        for (String link : linkSet)
        {
            StringBuilder newLink = new StringBuilder();
            
            if (link.contains("?") && link.contains("#"))
            {
                int indexOfHash = link.indexOf("#");
                String beforeHash = link.substring(0, indexOfHash);
                String restAfterHash = link.substring(indexOfHash);

                newLink.append(beforeHash).append("&amp;").append(campaignParams).append(restAfterHash);
            }
            else if (link.contains("?"))
            {
                newLink.append(link).append("&amp;").append(campaignParams);
            }
            else if (link.contains("#"))
            {
                int indexOfHash = link.indexOf("#");
                String beforeHash = link.substring(0, indexOfHash);
                String restAfterHash = link.substring(indexOfHash);

                newLink.append(beforeHash).append("?").append(campaignParams).append(restAfterHash);
            }
            else
            {
                newLink.append(link).append("?").append(campaignParams);
            }

            content = content.replace(link, newLink.toString());
        }

        return content;
    }
}
