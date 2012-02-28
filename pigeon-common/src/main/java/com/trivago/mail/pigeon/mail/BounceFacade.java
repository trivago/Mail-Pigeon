/**
 * Copyright (C) 2011-2012 trivago GmbH <mario.mueller@trivago.com>, <christian.krause@trivago.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trivago.mail.pigeon.mail;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * @author Mario Mueller mario.mueller@trivago.com
 */
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
