package ins.sino.claimcar.schedule.web.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitUtil {
	public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("10.248.107.61");
        //端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("testhost");
        factory.setUsername("admin");
        factory.setPassword("KFtest#20");
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
	
	public static void send(String message,String queueName,String exchangeName){
		 // 获取到连接以及mq通道
        Connection connection;
		try {
			connection = getConnection();
			 // 从连接中创建通道
	        Channel channel = connection.createChannel();
	        // 声明exchange
	        channel.exchangeDeclare(exchangeName, "topic");

	        // 声明（创建）队列
	        //channel.queueDeclare(queueName, false, false, false, null);
	        channel.basicPublish(exchangeName, queueName, null, message.getBytes("UTF-8"));
	        System.out.println(" [RabbitMQ] Sent '" + message + "'");
	        //关闭通道和连接
	        channel.close();
	        connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	public static void main(String[] args) {
		send("Hello What","queueName","exchangeName");
	}
}
