package dk.statsbiblioteket.doms.updatetracker.improved.database.hibernate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;


import static junit.framework.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/3/11
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class HibernateTest {

    Hibernate db;
    FedoraMockup fcmock = new FedoraMockup();


    @Before
    public void setUp() throws Exception {

        db = new DomsUpdateTrackerHibernateImpl(fcmock);
        db.setUp();
    }

    @After
    public void tearDown() throws Exception {
        db.clear();
    }

    @Test
    public void testObjectCreated() throws Exception {
        Date now = new Date();
        fcmock.addEntry("doms:test1");
        db.objectCreated("doms:test1", now);
        List<Entry> list = db.lookup(now);
        assertEquals("To many objects, some should have been deleted",1, list.size());

        list = db.lookup(new Date());
        assertEquals("To many objects, some should have been deleted",0, list.size());

    }

    @Test
    public void testObjectDeleted() throws Exception {

        Date old = new Date();
        fcmock.addEntry("doms:test1");
        db.objectCreated("doms:test1", old);
        Date now = new Date();
        db.objectDeleted("doms:test1",now);



        List<Entry> list = db.lookup(old);
        assertEquals("To many objects", 2, list.size());
        list = db.lookup(now);
        assertEquals("To many objects", 1, list.size());

        list = db.lookup(new Date());
        assertEquals("To many objects", 0, list.size());

    }

    @Test
    public void testObjectRelationsChanged() throws Exception {
        Date frozen = new Date();
        fcmock.addEntry("doms:test1");
        db.objectCreated("doms:test1",frozen);
        db.objectCreated("doms:test2",frozen);
        fcmock.addEntry("doms:test1","doms:test2");

        List<Entry> list = db.lookup(frozen);
        assertEquals(1,list.size());
        assertEquals(list.get(0).getDateForChange().getTime(),frozen.getTime());

        Date flow = new Date();
        db.objectRelationsChanged("doms:test1",flow);

        list = db.lookup(frozen);
        assertEquals(1,list.size());
        assertEquals(list.get(0).getDateForChange().getTime(),flow.getTime());

        Date flow2 = new Date();

        db.objectChanged("doms:test2",flow2);

        list = db.lookup(frozen);
        assertEquals(1,list.size());
        assertEquals(list.get(0).getDateForChange().getTime(),flow2.getTime());

        Date flow3 = new Date();

        db.objectPublished("doms:test1",flow3);
        list = db.lookup(frozen);
                assertEquals(2,list.size());
                assertEquals(list.get(0).getDateForChange().getTime(),flow2.getTime());
        assertEquals(list.get(1).getDateForChange().getTime(),flow3.getTime());

    }

    @Test
    public void testObjectPublished() throws Exception {
        Date frozen = new Date();
        fcmock.addEntry("doms:test1");
        db.objectCreated("doms:test1",frozen);
        Thread.sleep(1000);
        db.objectPublished("doms:test1",new Date());
        List<Entry> list = db.lookup(frozen);
        assertEquals("Wrong number of objects",list.size(),2);
        assertEquals(list.get(0).getDateForChange().getTime(),frozen.getTime());
        assertEquals(list.get(0).getState(),"I");
        assertEquals(list.get(1).getState(),"A");
    }

}
