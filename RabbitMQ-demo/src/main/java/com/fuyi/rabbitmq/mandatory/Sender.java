package com.fuyi.rabbitmq.mandatory;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ReturnListener;

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
		
		// 当mandatory=true时，添加该监听器回调处理死信消息
		channel.addReturnListener(new ReturnListener() {
			
			@Override
			public void handleReturn(int replyCode,String replyText,String exchange,
					String routingKey,AMQP.BasicProperties properties,byte[] body) throws IOException {
				System.out.println("handleReturn");
				System.out.println("replyCode:"+replyCode);
				System.out.println("replyText:"+replyText);
				System.out.println("messagebody:"+new String(body));
			}
			
		});

		// 4. 发送消息
		boolean mandatory = true;
		channel.basicPublish(EXCHANGE_NAME, "hehe", mandatory, MessageProperties.PERSISTENT_TEXT_PLAIN, ("hello-direct-durable-mandatory").getBytes());
		
		
		System.in.read();
		
		// 5. 关闭连接
		channel.close();
		connection.close();
	}
}
