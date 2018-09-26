package com.fuyi.rabbitmq.routekey;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReveiveInfoLog {

	private static final String LOG_LEVEL = "info";
	private static final String EXCHANGE_NAME = "direct_logs";

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
		
		// 4. 创建一个非持久的、唯一的、自动删除的队列
		String queueName = channel.queueDeclare().getQueue();
				
		// 5. 绑定queue到exchange
		// 参数1 queue ：队列名
        // 参数2 exchange ：交换器名
        // 参数3 routingKey ：路由键名
		channel.queueBind(queueName, EXCHANGE_NAME, LOG_LEVEL);
		
		// 6. 创建队列消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        
        System.out.println("Log Level: " + LOG_LEVEL);
        channel.basicConsume(queueName, true, consumer);
	}
}
