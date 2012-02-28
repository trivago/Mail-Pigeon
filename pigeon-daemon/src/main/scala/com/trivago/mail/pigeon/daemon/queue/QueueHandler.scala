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