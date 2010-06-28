package com.zanoccio.axirassa.pinger;

import java.io.IOException;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.qpid.AMQException;
import org.apache.qpid.client.AMQAnyDestination;
import org.apache.qpid.client.AMQConnection;
import org.apache.qpid.jms.ConnectionListener;
import org.apache.qpid.url.BindingURL;
import org.apache.qpid.url.BindingURLParser;
import org.apache.qpid.url.URLSyntaxException;

/**
 * The HTTPPinger provides verification of a single arbitrary URL.
 * 
 * @author wiktor
 */
public class HTTPPinger extends AbstractPinger {

	public void run() throws URLSyntaxException, AMQException, JMSException, NamingException, IOException {
		Properties properties = new Properties();
		properties.load(this.getClass().getResourceAsStream("HTTPPinger.properties"));
		Context context = new InitialContext(properties);
		
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionfactory");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination queue = (Destination) context.lookup("topicExchange");
		
		MessageProducer producer = session.createProducer(queue);
		MessageConsumer consumer = session.createConsumer(queue);
		
		TextMessage msg = session.createTextMessage("Hi There");
		producer.send(msg);
		
		msg = (TextMessage) consumer.receive();
		System.out.println("Received: "+msg.getText());
		
		connection.close();
		context.close();
	}
	
	
	
	public static void main(String[] args) throws URLSyntaxException, AMQException, JMSException, NamingException, IOException {
		HTTPPinger pinger = new HTTPPinger();
		pinger.run();
	}
}
