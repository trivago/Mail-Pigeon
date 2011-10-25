package com.trivago.mail.pigeon.mail;

import com.trivago.mail.pigeon.storage.ConnectionFactory;

import javax.mail.Message;
import javax.mail.MessagingException;


public class BounceFacade
{
	public boolean processBounce(Message msg)
	{
		try
		{
			final String[] header = msg.getHeader("Diagnostic-Code");

			boolean isHardBounce = false;
			boolean isSoftBounce = false;

			// This is really low level, this should be improved later ;)
			if (header[1].startsWith("55"))
			{
				isHardBounce = true;
			}
			else if (header[1].startsWith("45"))
			{
				isSoftBounce = true;
			}



			return isHardBounce || isSoftBounce;
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
		// default: return false as it is not marked as "seen" on the server
		return false;
	}
}
