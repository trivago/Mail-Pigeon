package com.trivago.mail.pigeon.process;


import com.trivago.mail.pigeon.bean.Campaign;
import com.trivago.mail.pigeon.bean.Mail;
import com.trivago.mail.pigeon.bean.Recipient;
import com.trivago.mail.pigeon.bean.Sender;
import com.trivago.mail.pigeon.json.MailTransport;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.StringWriter;

public class TemplateProcessor
{
	private VelocityEngine velocity;

	public TemplateProcessor()
	{
		velocity = new VelocityEngine();
	}

	public MailTransport processMail(Mail mail, Recipient recipient, Sender sender, Campaign campaign)
	{
		MailTransport transport = new MailTransport();

		transport.setTo(recipient.getEmail());
		transport.setReplyTo(sender.getReplytoMail());
		transport.setFrom(sender.getFromMail());

		Context ctx = new VelocityContext();

		ctx.put("sender.id", sender.getId());
		ctx.put("sender.name", sender.getName());
		ctx.put("sender.email.from", sender.getFromMail());
		ctx.put("sender.email.replyto", sender.getReplytoMail());

		ctx.put("recipient.id", recipient.getId());
		ctx.put("recipient.name", recipient.getName());
		ctx.put("recipient.email", recipient.getEmail());

		ctx.put("mail.date", mail.getSendDate());
		ctx.put("mail.id", mail.getId());

		ctx.put("campaign.id", campaign.getId());

		String renderSubject = mail.getSubject();
		StringWriter outputSubject = new StringWriter();
		velocity.mergeTemplate("vm/macros/macroInclude.vm", "utf-8", ctx, outputSubject);
        velocity.evaluate( ctx, outputSubject, "subjectrender", renderSubject );

		String renderText = mail.getText();
		StringWriter outputText = new StringWriter();
		velocity.mergeTemplate("vm/macros/macroInclude.vm", "utf-8", ctx, outputText);
		velocity.evaluate( ctx, outputText, "textrender", renderText);

		String renderHtml = mail.getHtml();
		StringWriter outputHtml = new StringWriter();
		velocity.mergeTemplate("vm/macros/macroInclude.vm", "utf-8", ctx, outputHtml);
		velocity.evaluate(ctx, outputHtml, "htmlrender", renderHtml);


		transport.setSubject(outputSubject.toString());
		transport.setHtml(outputHtml.toString());
		transport.setText(outputText.toString());


		transport.setmId(String.valueOf(mail.getId()));
		transport.setuId(String.valueOf(recipient.getId()));

		return transport;
	}
}
