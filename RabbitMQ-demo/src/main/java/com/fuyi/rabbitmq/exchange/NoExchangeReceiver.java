package com.fuyi.rabbitmq.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class NoExchangeReceiver {

	private static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		// 1. 创建连接
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.0.221");
		connectionFactory.setUsername("hzx_admin");
		connectionFactory.setPassword("123456");
		Connection connection = connectionFactory.newConnection();
		
		// 2. 通过连接创建通道
		Channel channel = connection.createChannel();
		
		// 3. 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		// 4. 创建队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);
        
        // 5. 获取数据
        for(;;) {
        	Delivery nextDelivery = consumer.nextDelivery();
        	byte[] body = nextDelivery.getBody();
        	System.out.println("[Receiver] "  + new String(body));
        }
	}
}
