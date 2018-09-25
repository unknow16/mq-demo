package com.fuyi.rabbitmq.rr;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

	/**
	 * 消息队列名字
	 * 
	 * 声明队列是幂等的, 队列只会在它不存在时才会被创建，多次声明并不会重复创建。
	 */
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

		// 4. 发送消息
		for(int i = 5; i < 15; i++) {
			String msg = "Hello World :" + i ;
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			System.out.println(" [x] Sent '" + msg + "'");
		}
		
		// 5. 关闭通道和连接
		channel.close();
		connection.close();
	}
}
