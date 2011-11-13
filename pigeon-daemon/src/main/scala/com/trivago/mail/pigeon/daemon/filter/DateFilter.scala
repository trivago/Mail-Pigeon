package com.trivago.mail.pigeon.daemon.filter

import com.trivago.mail.pigeon.json.MailTransport
import java.util.Calendar
import java.text.SimpleDateFormat

object DateFilter {

    def filterIfDateIsNotReached(msg: MailTransport): Boolean = {
        Calendar.getInstance.after(msg.getSendDate)
    }
}