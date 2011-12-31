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
