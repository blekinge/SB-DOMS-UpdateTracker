package dk.statsbiblioteket.doms.updatetracker.improved.jms;

import junit.framework.TestCase;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

public class Jmstest extends TestCase {

    private static String user = ActiveMQConnection.DEFAULT_USER;
    private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "TOOL.SAGARA.eg1";

    @Test
    public void testMessageDelivery() throws Exception {
        Connection connection = null;
        Destination destination = null;

// Create the connection.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                user, password, url);
        connection = connectionFactory.createConnection();
        connection.start();

// Create the session
        Session session = connection.createSession(false,
                                                   Session.AUTO_ACKNOWLEDGE);
//
        destination = session.createQueue(subject);

// Create the producer.
        MessageConsumer consumer = session.createConsumer(destination);

        Message msg = consumer.receive();

        if (msg instanceof TextMessage) {
            TextMessage txtMsg = (TextMessage) msg;
            System.out.println("recived message: " + txtMsg.getText());
        }

        System.out.println("Done.");
    }


    @Before
    protected void setUp() throws Exception {
        super.setUp();
        Connection connection = null;
        Destination destination = null;

// Create the connection.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                user, password, url);
        connection = connectionFactory.createConnection();
        connection.start();

// Create the session
        Session session = connection.createSession(false,
                                                   Session.AUTO_ACKNOWLEDGE);
//
        destination = session.createQueue(subject);

// Create the producer.
        MessageProducer producer = session.createProducer(destination);

// Start sending messages
        String msgTo = " Hello world ActiveMQ from Sagara :: Maven";
        TextMessage message = session.createTextMessage(msgTo);
        System.out.println("Sending message: " + msgTo);

        producer.send(message);

        System.out.println("Done.");

        connection.close();

    }

}