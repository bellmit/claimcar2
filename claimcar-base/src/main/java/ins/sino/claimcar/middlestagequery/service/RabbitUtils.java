package ins.sino.claimcar.middlestagequery.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitUtils {
	
	private static final String HOST = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_HOST");
	private static final String PORT = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_PORT");
	private static final String VIRTUALHOST = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_VIRTUALHOST");
	private static final String USERNAME = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_USERNAME");
	private static final String PASSWORD = SpringProperties.getProperty("spring.rabbitmq.RABBITMQ_PASSWORD");
	
	
	public Connection getConnect() throws IOException, TimeoutException, NullPointerException{
		
		String username = RabbitUtils.USERNAME;
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
