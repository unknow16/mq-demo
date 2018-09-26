package com.fuyi.rabbitmq.routekey;

import java.util.Random;
import java.util.UUID;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {
	
	private static final String EXCHANGE_NAME = "direct_logs";
	private static final String[] LOG_LEVEL_AEE = {"error", "warn", "info"};
	
	public static void main(String[] args) throws Exception {
		// 1. 创建连接
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.0.221");
		connectionFactory.setUsername("hzx_admin");
		connectionFactory.setPassword("123456");
		Connection connection = connectionFactory.newConnection();
		
		// 2. 通过连接创建通道
		Channel channel = connection.createChannel();
		
		// 3. 声明Exchange
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		// 4. 发布消息
		for(int i=0; i<10; i++) {
			int rand = new Random().nextInt(3);
			String logLevel = LOG_LEVEL_AEE[rand];
			
			String msg = "MQ log: [" + logLevel +"] " + UUID.randomUUID().toString();
			channel.basicPublish(EXCHANGE_NAME, logLevel, null, msg.getBytes());
			
			System.out.println(" [x] Sent '" + msg + "'");  
		}
		
		// 5. 关闭频道和连接  
        channel.close();
        connection.close();
	}
}
