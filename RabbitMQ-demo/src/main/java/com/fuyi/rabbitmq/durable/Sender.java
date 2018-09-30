package com.fuyi.rabbitmq.durable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Sender {

	private static final String EXCHANGE_NAME = "hello-direct-durable";

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
		
		// 4. 指定发送消息为持久化， MessageProperties类定义了一系列常用BasicProperties的集合
		for(int i=0; i<100000; i++) {
			channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, ("hello-direct-durable-" + i ).getBytes());
		}
		
		// 5. 关闭连接
		channel.close();
		connection.close();
	}
}
