package com.trivago.mail.pigeon.json;

import java.util.Date;

public class MailTransport
{
	private String from;

	private String to;

	private String replyTo;

	private String subject;

	private String html;

	private String text;

	private String mId;

	private String uId;

	private Date sendDate;

	private boolean enforceSending = false;

	private boolean abortSending = false;

	public void enforceSending()
	{
		enforceSending = true;
	}

	public void abortSending()
	{
		abortSending = true;
	}

	public boolean shouldAbortSending()
	{
		return abortSending;
	}

	public boolean shouldEnforceSending()
	{
		return enforceSending;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getReplyTo()
	{
		return replyTo;
	}

	public void setReplyTo(String replyTo)
	{
		this.replyTo = replyTo;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getHtml()
	{
		return html;
	}

	public void setHtml(String html)
	{
		this.html = html;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getmId()
	{
		return mId;
	}

	public void setmId(String mId)
	{
		this.mId = mId;
	}

	public String getuId()
	{
		return uId;
	}

	public void setuId(String uId)
	{
		this.uId = uId;
	}

	public Date getSendDate()
	{
		return sendDate;
	}

	public void setSendDate(Date sendDate)
	{
		this.sendDate = sendDate;
	}
}
