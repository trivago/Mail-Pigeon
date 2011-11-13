package com.trivago.mail.pigeon.daemon

import com.trivago.mail.pigeon.queue.ConnectionPool
import com.rabbitmq.client.QueueingConsumer
import com.trivago.mail.pigeon.mail.MailFacade
import com.trivago.mail.pigeon.json.MailTransport
import org.svenson.JSONParser

object Runner
{
    /**
     * Main Daemon method containing the event loop.
     *
     * @param args command line args
     * @throws java.io.IOException
     */
    def main(args: Array[String]) {
        val conn = ConnectionPool.getConnection
        val channel = conn.createChannel
        val autoAck: Boolean = false
        val consumer: QueueingConsumer = new QueueingConsumer(channel)
        val mailFacade: MailFacade = new MailFacade


        channel.basicConsume("messages", autoAck, consumer)
        while (true)
        {
            var delivery: QueueingConsumer.Delivery = null
            try
            {
                delivery = consumer.nextDelivery
            }
            catch
            {
                case ie: InterruptedException =>
                { // Do nothing, this is intended
                }
            }
            val jsonContent: String = new String(delivery.getBody)
            val mailTransport: MailTransport = JSONParser.defaultJSONParser.parse(classOf[MailTransport], jsonContent)
            mailFacade.sendMail(mailTransport)
            channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
        }
    }
}