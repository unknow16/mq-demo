package com.fuyi.rabbitmq.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicSender {
	
	private static final String EXCHANGE_NAME = "hello-topic";
	
	public static void main(String[] args) throws Exception {
		// 1. 创建连接
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.0.221");
		connectionFactory.setUsername("hzx_admin");
		connectionFactory.setPassword("123456");
		Connection connection = connectionFactory.newConnection();
		
		// 2. 通过连接创建通道
		Channel channel = connection.createChannel();
		
		// 3. 声明交换器
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		
		// 4. 发送消息
		channel.basicPublish(EXCHANGE_NAME, "fast.orange.*", null, "hello-fast.orange.*".getBytes());
		channel.basicPublish(EXCHANGE_NAME, "lazy.orange.a.b", null, "hello-lazy.orange.a.b".getBytes());
		 
        // 5. 关闭频道和连接  
        channel.close();
        connection.close();
	}
}
