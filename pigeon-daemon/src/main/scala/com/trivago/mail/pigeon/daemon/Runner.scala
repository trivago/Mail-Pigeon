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
package com.trivago.mail.pigeon.daemon

import com.trivago.mail.pigeon.queue.ConnectionPool
import com.rabbitmq.client.QueueingConsumer
import com.trivago.mail.pigeon.mail.MailFacade
import com.trivago.mail.pigeon.json.MailTransport
import filter.DateFilter
import org.svenson.JSONParser
import queue.QueueHandler

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

            // This should evolve to a pluggable filter systen, but for now we just filter the
            // mails that are subject to be send in the future
			if (DateFilter.filterIfDateIsNotReached(mailTransport))
			{
				mailFacade.sendMail(mailTransport)
            	channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
                // we should send a new msg to the server to indicate, that we made a successful attempt to send
                // the message. this should lead to a new graph connection between the recipient and the newsletter.
                // this could also be used to count the sent newsletters in near-realtime
			}
			else
			{
				QueueHandler.requeueMessage(mailTransport, conn, "messages")
			}
        }
    }
}