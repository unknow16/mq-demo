package com.fuyi.rabbitmq.ack;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class ConfirmSender {

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
		
		// 4. 发消息，确认机制通过confirm实现
		final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
		channel.addConfirmListener(new ConfirmListener() {
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("noack, deliveryTag: " + deliveryTag + ", multiple: " + multiple);
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("ack, deliveryTag: " + deliveryTag + ", multiple: " + multiple);
				if(multiple) {
					confirmSet.headSet(deliveryTag + 1).clear();
				} else {
					confirmSet.remove(deliveryTag);
				}
			}
		});
		
		long nextPublishSeqNo = channel.getNextPublishSeqNo();
		channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, ("hello-direct-durable-confirm").getBytes());
		confirmSet.add(nextPublishSeqNo);
		
		// 5. 关闭连接
		channel.close();
		connection.close();
	}
}
