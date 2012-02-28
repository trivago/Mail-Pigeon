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
package com.trivago;


import com.trivago.mail.pigeon.daemon.filter.DateFilter;
import com.trivago.mail.pigeon.json.MailTransport;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

public class TestDateFilter
{
	@Test
	public void testDateFilterPositive()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 5);
		Date d = cal.getTime();
		MailTransport mt = new MailTransport();
		mt.setSendDate(d);
		assertTrue("Message should have been filtered.", DateFilter.filterIfDateIsNotReached(mt));
	}

	@Test
	public void testDateFilterNegative()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -5);
		Date d = cal.getTime();
		MailTransport mt = new MailTransport();
		mt.setSendDate(d);
		assertFalse("Message should not have been filtered.", DateFilter.filterIfDateIsNotReached(mt));
	}
}
