package com.trivago.mail.pigeon.daemon.queue

import com.trivago.mail.pigeon.json.MailTransport
import com.rabbitmq.client.Connection
import org.svenson.JSON

object QueueHandler {

    def requeueMessage(msg: MailTransport, conn: Connection, channelName: String) {
        val json: String = JSON.defaultJSON.forValue(msg)
        val channel = conn.createChannel
        channel.exchangeDeclare("mailpidgeon", "direct", true)
        channel.queueDeclare(channelName, true, false, false, null)
        channel.queueBind(channelName, "mailpidgeon", "mailpidgeon")
        val messageBodyBytes: Array[Byte] = json.getBytes
        channel.basicPublish("mailpidgeon", "mailpidgeon", null, messageBodyBytes)
    }
}