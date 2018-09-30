package com.fuyi.rabbitmq.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class TxSender {

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
		
		// 4. 发消息
		channel.txSelect(); // 开启事务
		try {
			channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, ("hello-direct-durable-tx").getBytes());
			
			channel.txCommit(); // 提交事务
			System.out.println("commit done.");
		} catch (Exception e) {
			channel.txRollback(); // 回滚事务
			e.printStackTrace();
		}
		
		// 5. 关闭连接
		channel.close();
		connection.close();
	}
}
