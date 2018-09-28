package com.fuyi.rabbitmq.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class DirectReceiver1 {

	private static final String EXCHANGE_NAME = "hello-direct";

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
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		// 4. 创建一个非持久的、唯一的且自动删除的队列  
		String queue = channel.queueDeclare().getQueue();
		// 5. 将队列绑定到交换器上
		channel.queueBind(queue, EXCHANGE_NAME, "error");
		
		// 6. 创建队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queue, true, consumer);
        
        // 5. 获取数据
        for(;;) {
        	Delivery nextDelivery = consumer.nextDelivery();
        	byte[] body = nextDelivery.getBody();
        	System.out.println("[Error Receiver] "  + new String(body));
        }
	}
}
