package com.fuyi.rabbitmq.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class ConfirmReceiver {

	private static final String EXCHANGE_NAME = "hello-direct-durable";
	private static final String QUEUE_NAME = "hello-direct-durable";
	
	public static void main(String[] args) throws Exception {
		// 1. 创建连接
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.0.221");
		connectionFactory.setUsername("hzx_admin");
		connectionFactory.setPassword("123456");
		Connection connection = connectionFactory.newConnection();
		
		// 2. 通过连接创建通道
		Channel channel = connection.createChannel();
		
		// 3. 声明持久化交换机
		boolean durable = true;
		channel.exchangeDeclare(EXCHANGE_NAME, "direct", durable);
		
		// 4. 声明持久化队列
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		
		// 5. 将队列绑定到交换机上
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
		
		// 6. 从队列获取消息
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);
		for(;;) {
			Delivery delivery = consumer.nextDelivery();
			// 获取路由键
			// String routingKey = delivery.getEnvelope().getRoutingKey();
			byte[] body = delivery.getBody();
			System.out.println("[Receiver] : " + new String(body));
		}
		
	}
}
