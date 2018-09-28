package com.fuyi.rabbitmq.exchange;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class HeadersSender {
	
	private static final String EXCHANGE_NAME = "hello-headers";
	
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
		channel.exchangeDeclare(EXCHANGE_NAME, "headers");
		
		
		// 4. 发送消息
		Map<String, Object> headers = new HashMap<>();
		headers.put("color", "red");
		headers.put("speed", "fast");
		Builder builder = new Builder();
		builder.headers(headers);
		channel.basicPublish(EXCHANGE_NAME, "", builder.build(), "hello-fast".getBytes());
		
		Map<String, Object> headers1 = new HashMap<>();
		headers1.put("color", "red");
		headers1.put("speed", "normal");
		Builder builder1 = new Builder();
		builder1.headers(headers1);
		channel.basicPublish(EXCHANGE_NAME, "", builder1.build(), "hello-normal".getBytes());
		 
        // 5. 关闭频道和连接  
        channel.close();
        connection.close();
	}
}
