package ins.sino.claimcar.middlestagequery.service;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 发送消息服务
 * @author j2eel
 *
 */
public class SendMsgToMqService {
	
	private static final String QUEUE_NAME = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_QUEUE");
	private static final String EXCHANGE_NAME = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_EXCHANGE");
	private static final String ROUTING_KEY = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_ROUTEKEY");
	
	private static final String HOST = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_HOST");
	private static final String PORT = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_PORT");
	private static final String VIRTUALHOST = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_VIRTUALHOST");
	private static final String USERNAME = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_USERNAME");
	private static final String PASSWORD = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_PASSWORD");

	
	private static Logger logger = LoggerFactory.getLogger(SendMsgToMqService.class);
	
	public void send(String data, String registNo) throws IOException, TimeoutException, Exception{	
		Connection conn = this.getConnect();
		//创建信道
		Channel channel = conn.createChannel();
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);
		//声明队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		//绑定队列和交换机
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		String msg = data;
		//上传消息
		//设置消息属性
		AMQP.BasicProperties proper = new AMQP.BasicProperties();	
		proper.builder().contentType("application/json");
		
		channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, proper, msg.getBytes("UTF-8"));
	
		logger.info("报案号： " + registNo + " 对应的消息已发送到rabbitMq =====> " + msg );
		channel.close();
		conn.close();
	}
	
	public Connection getConnect() throws IOException, TimeoutException, NullPointerException{
		
		String username = USERNAME;
		String password = PASSWORD;
		String virtualHost = VIRTUALHOST;
		String host = HOST;
		int port = Integer.parseInt(PORT);
		
		//创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setVirtualHost(virtualHost);
		factory.setUsername(username);
		factory.setPassword(password);
		
		return factory.newConnection();
	}


}
