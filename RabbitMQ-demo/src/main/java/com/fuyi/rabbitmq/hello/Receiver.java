package com.fuyi.rabbitmq.hello;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receiver {
	
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
		
		/**
		 * 3. 给通道指定队列
		 * 
		 * queueDeclare(String queue, boolean durable, boolean exclusive, 
		 * 				boolean autoDelete, Map<String, Object> arguments)
		 * queue： 队列名
		 * durable：是否持久化
		 * exclusive：仅创建者可以使用的私有队列，断开后自动删除
		 * autoDelete：当所有消费者连接断开后，是否自动删除队列
		 * arguments：额外参数
		 */
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for message. To exit press CTRL+C");
		
		// 4. 创建队列消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
				String msg = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + msg + "'");
			}
		};
		
		// 5. 给通道绑定消费者
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}

}
