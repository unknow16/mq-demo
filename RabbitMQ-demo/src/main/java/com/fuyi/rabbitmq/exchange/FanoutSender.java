package com.fuyi.rabbitmq.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class FanoutSender {
	
	private static final String EXCHANGE_NAME = "hello";
	
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
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		// 4. 发送消息
		for(int i=0; i<10; i++) {
			String message = "Hello " + i;
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");  
		}
		 
        // 5. 关闭频道和连接  
        channel.close();
        connection.close();
	}
}
