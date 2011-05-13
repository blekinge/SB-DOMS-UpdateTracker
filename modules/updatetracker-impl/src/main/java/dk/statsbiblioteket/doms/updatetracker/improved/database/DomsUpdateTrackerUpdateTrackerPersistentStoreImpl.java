package dk.statsbiblioteket.doms.updatetracker.improved.database;

import dk.statsbiblioteket.doms.updatetracker.improved.fedora.Fedora;
import dk.statsbiblioteket.doms.updatetracker.improved.fedora.ViewInfo;
import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.NaturalIdentifier;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 4/27/11
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DomsUpdateTrackerUpdateTrackerPersistentStoreImpl implements UpdateTrackerPersistentStore {

    private SessionFactory sessionFactory;

    Fedora fedora;

    public DomsUpdateTrackerUpdateTrackerPersistentStoreImpl(Fedora fedora) {
        this.fedora = fedora;
    }

    public void setUp() throws Exception {
        // A SessionFactory is set up once for an application
        sessionFactory = new AnnotationConfiguration()
                .addAnnotatedClass(DomsObject.class)
                .addAnnotatedClass(Entry.class)
                .configure()
                .buildSessionFactory();
    }


    private void objectModified(String pid, Date date, String state) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            List results = session.createCriteria(DomsObject.class)
                    .add(Restrictions.naturalId().set("objectPid", pid))
                    .list();
            //Find all Entries that use this object
            for (Object result : results) {
                if (result instanceof DomsObject) {
                    DomsObject result1 = (DomsObject) result;
                    //Mark them as updated
                    updateEntry(session,result1.getEntryPid(), state,result1.getViewAngle(), date);
                }
            }

            // Find view Info for this object
            List<ViewInfo> viewInfoList = fedora.getViewInfo(pid,date);
            for (ViewInfo viewInfo : viewInfoList) {
                //If it is an entry object, set it in the ENTRIES table
                if (viewInfo.isEntry()) {
                    updateEntry(session, pid, state, viewInfo.getViewAngle(), date);
                    updateDomsObjects(session,pid,pid,viewInfo.getViewAngle());
                }
            }
            transaction.commit();

        } catch (HibernateException e) {
            //TODO log
            transaction.rollback();

        }


    }

    @Override
    public void objectCreated(String pid, Date date) {
        objectModified(pid,date,"I");
        objectRelationsChanged(pid,date);
    }

    @Override
    public void objectDeleted(String pid, Date date) {
        objectModified(pid,date,"D");
    }

    @Override
    public void objectPublished(String pid, Date date) {
        objectModified(pid,date,"A");
    }

    @Override
    public void objectChanged(String pid, Date date) {
        objectModified(pid,date,"I");
    }

    private void updateDomsObjects(Session session, String objectPid, String entryPid, String viewAngle) {
        List results = session.createCriteria(DomsObject.class).add(Restrictions.naturalId()
                                                                            .set("objectPid", objectPid)
                                                                            .set("entryPid", entryPid)
                                                                            .set("viewAngle", viewAngle))
                .list();
        if (results.size() == 0){
            session.save(new DomsObject(objectPid,entryPid,viewAngle));
        }
    }

    private void updateEntry(Session session, String entryPid, String state, String viewAngle, Date date) {
        NaturalIdentifier restrictions = Restrictions
                .naturalId();

        if (entryPid != null){
            restrictions = restrictions.set("entryPid", entryPid);
        }
        if (state != null){
            restrictions = restrictions.set("state", state);
        }
        if (viewAngle != null){
            restrictions = restrictions.set("viewAngle", viewAngle);
        }

        List results = session.createCriteria(Entry.class)
                .add(restrictions)
                .list();
        if (results.size() == 0){
            session.save(new Entry(entryPid,viewAngle,state,date));
        } else {
            for (Object result : results) {
                if (result instanceof Entry) {
                    Entry result1 = (Entry) result;
                    if (result1.getDateForChange().before(date)){
                        if (entryPid != null){
                            result1.setEntryPid(entryPid);
                        }
                        if (state != null){
                            result1.setState(state);
                        }
                        if (viewAngle != null){
                            result1.setViewAngle(viewAngle);
                        }
                        result1.setDateForChange(date);
                    }
                    session.save(result1);
                }
            }
        }
    }

    private void removeFromEntries(Session session, String entryPid, String state, String viewAngle) {
        List results = session.createCriteria(Entry.class)
                .add(Restrictions
                             .naturalId()
                             .set("entryPid", entryPid)
                             .set("state", state)
                             .set("viewAngle", viewAngle))
                .list();
        for (Object result : results) {
            if (result instanceof Entry) {
                Entry result1 = (Entry) result;
                session.delete(result1);
            }
        }

    }

    private void removeFromDomsObjects(Session session, String objectPid, String entryPid, String viewAngle) {
        List results = session.createCriteria(DomsObject.class).add(Restrictions.naturalId()
                                                                            .set("objectPid", objectPid)
                                                                            .set("entryPid", entryPid)
                                                                            .set("viewAngle", viewAngle))
                .list();
        for (Object result : results) {
            if (result instanceof DomsObject) {
                DomsObject result1 = (DomsObject) result;
                session.delete(result1);
            }
        }
    }





    public void objectRelationsChanged(String pid, Date date){
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();



        //This can change the structure of the views and we must therefore recaculate the views

        //if a current entry object use this object, we will need to recalculate the view of that object
        //if this is (now) an entry object, it should have it's view calcualted

        List<DomsObject> results = session.createCriteria(DomsObject.class)
                .add(Restrictions
                             .naturalId()
                             .set("objectPid", pid)
                )
                .list();

        //we now have a list of all the entries that include this object.


        for (DomsObject result : results) {

            ViewBundle bundle  = fedora.calcViewBundle(result.getEntryPid(), result.getViewAngle(),date);

            //First, remove all the objects in this bundle from the table
            //TODO should we really do this?
            removeFromDomsObjects(session, null, result.getEntryPid(), result.getViewAngle());


            //Add all the objects from the bundle to the objects Table.
            for (String objectPid : bundle.getContained()) {
                updateDomsObjects(session,objectPid, bundle.getEntry(), bundle.getViewAngle());
            }

            updateEntry(session, bundle.getEntry(),"I", bundle.getViewAngle(),date);

        }
    }




    @Override
    public List<Entry> lookup(Date since, String viewAngle, int offset, int limit, boolean newestFirst){
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {

            Criteria thing = session.createCriteria(Entry.class)
                    .add(Restrictions.ge("dateForChange", since))
                    .add(Restrictions.naturalId().set("viewAngle", viewAngle))
                    .setFirstResult(offset)
                    .setFetchSize(limit);
            if (newestFirst){
                thing.addOrder(Order.desc("dateForChange"));
            } else {
                thing.addOrder(Order.asc("dateForChange"));
            }
            List results = thing.list();


            List<Entry> entries = new ArrayList<Entry>(results.size());
            for (Object result : results) {
                if (result instanceof Entry) {
                    Entry result1 = (Entry) result;
                    entries.add(result1);
                }
            }
            return entries;

        } catch (HibernateException e) {
            //TODO log
            transaction.rollback();
            throw e;
        }
    }


    public void clear(){
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        List results = session.createCriteria(DomsObject.class).list();
        for (Object result : results) {
            session.delete(result);

        }
        results = session.createCriteria(Entry.class).list();
        for (Object result : results) {
            session.delete(result);

        }
        transaction.commit();


    }


    /*
        public void objectRelationsChanged(String pid, Date date){
            //objectChanged(pid,date); TODO

            //Find all entry objects that currently use the changed object
            List<DomsObject> domsobjects = findDomsObjects(pid);
            Set<String> entries = new HashSet<String>();//List of unique entry/viewangle pairs from the Objects table

            for (DomsObject domsobject : domsobjects) {
                entries.add(domsobject.getEntryPid()+"/"+domsobject.getViewAngle());
            }

            //Now, remove all references to these entries objects from the OBJECTS table

            for (String entry : entries) {
                String entrypid = entry.substring(0,entry.indexOf("/"));
                String viewangle = entry.substring(entry.indexOf("/") + 1);
                removeFromDomsObjects(null,entrypid,viewangle);


            }

            //Then, recreate all the OBJECTS entries
            for (String entry : entries) {
                String entrypid = entry.substring(0,entry.indexOf("/"));
                String viewangle = entry.substring(entry.indexOf("/") + 1);
                ViewBundle bundle = fedora.getViewBundle(entrypid,viewangle,date);
                List<String> contained = bundle.getContainedObjects();
                for (String containedpid : contained) {
                    addToDomsObjects(containedpid,entrypid,viewangle);
                }
                //And update the entries in the ENTRY database
                updateChangedTimes(entrypid,viewangle,bundle.getState(),date);
            }

        }

    */



}
