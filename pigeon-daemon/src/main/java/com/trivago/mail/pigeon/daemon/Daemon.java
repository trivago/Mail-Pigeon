package com.trivago.mail.pigeon.daemon;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class Daemon
{
	private static final String channelName = "messages";

    private static final String userName = "admin";
    private static final String password = "admin";
    private static final String virtualHost = "/";
    private static final String hostName = "127.0.0.1";
    private static final Integer portNumber = 5672;

    /**
     * Main Daemon method containing the event loop.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException
	{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(hostName);
        factory.setPort(portNumber);

        Connection conn = null;
        Channel channel = null;

        try {
            conn = factory.newConnection();
            channel = conn.createChannel();

            boolean autoAck = false;
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(channelName, autoAck, consumer);
            while (true) {
                QueueingConsumer.Delivery delivery;
                try {
                    delivery = consumer.nextDelivery();
                } catch (InterruptedException ie) {
                    continue;
                }
                System.out.println(delivery.getEnvelope().getDeliveryTag() + " :: Body:" + new String(delivery.getBody()));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }

        } finally {
            if (channel != null) {
                channel.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
