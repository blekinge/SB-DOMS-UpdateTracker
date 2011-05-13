package dk.statsbiblioteket.doms.updatetracker.improved;

import dk.statsbiblioteket.doms.updatetracker.UpdateTrackerImpl;
import dk.statsbiblioteket.doms.updatetracker.improved.database.DomsUpdateTrackerUpdateTrackerPersistentStoreImpl;
import dk.statsbiblioteket.doms.updatetracker.improved.database.UpdateTrackerPersistentStore;
import dk.statsbiblioteket.doms.updatetracker.improved.fedora.Fedora;
import dk.statsbiblioteket.doms.updatetracker.improved.fedoraJms.FedoraMessageListener;
import dk.statsbiblioteket.doms.webservices.authentication.Credentials;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 4/28/11
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Meat {


    private static String url = "tcp://localhost:61616";
    private static String subject = "fedora.apim.update";
    private static UpdateTrackerPersistentStore store;
    private static Fedora fedora;
    private static Credentials creds;
    private static String fedoraLocation = "http://localhost:7880/fedora";
    private static String ecmLocation = "http://localhost:7880/ecm-service";


    public static void startup() throws MalformedURLException, JMSException {

        //Start up the fedora connection
        fedora = new Fedora(creds,fedoraLocation,ecmLocation);

        //Start up the database
        store = new DomsUpdateTrackerUpdateTrackerPersistentStoreImpl(fedora);

        //initialise the jms connection to Fedora
        initialiseJMS(store);

       /* //Try not to get stuff garbage collected here.

        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }*/
    }

    public static UpdateTrackerPersistentStore getStore() {
        return store;
    }

    private static void initialiseJMS(UpdateTrackerPersistentStore store) throws JMSException {
        Connection connection = null;
        Destination destination = null;

        // Create the connection.
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();

        // Create the session
        Session session = connection.createSession(false,
                                                   Session.AUTO_ACKNOWLEDGE);

        destination = session.createTopic(subject);

        // Create the Consumer
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new FedoraMessageListener(store));
    }

}
