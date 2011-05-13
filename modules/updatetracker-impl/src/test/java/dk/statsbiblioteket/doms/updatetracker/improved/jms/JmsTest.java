package dk.statsbiblioteket.doms.updatetracker.improved.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/6/11
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class JmsTest {

    String url = "tcp://localhost:61616";
        private static String subject = "fedora.apim.update";

/*
    @Test
    public void testFedoraListener() throws MessagingException {

        Properties props = new Properties();
        props.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.put("java.naming.provider.url",url);
        props.put("connection.factory.name","ConnectionFactory");
        props.put("topic.fedoraManagement","*");
        JmsMessagingClient client = new JmsMessagingClient(25 + "", new FedoraMessageListenerDemo(), props);
        client.start();
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
*/

    @Test
    public void testFedoraListener2() throws JMSException {

        Connection connection = null;
        Destination destination = null;

// Create the connection.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();

// Create the session
        Session session = connection.createSession(false,
                                                   Session.AUTO_ACKNOWLEDGE);
//
        destination = session.createTopic(subject);


// Create the producer.
        MessageConsumer consumer = session.createConsumer(destination);


        Message msg = consumer.receive();

        if (msg instanceof TextMessage) {
            TextMessage txtMsg = (TextMessage) msg;
            System.out.println("recived message: " + txtMsg.getText());
        }

        System.out.println("Done.");    }
}
