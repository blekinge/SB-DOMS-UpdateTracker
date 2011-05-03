package dk.statsbiblioteket.doms.updatetracker.improved.database.hibernate;

import org.hibernate.cfg.AnnotationConfiguration;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/3/11
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Hibernate {

    void setUp() throws Exception;

    void clear();

    //DONE
    void objectCreated(String pid, Date date);

    /**
     * Object was changed to the deleted state. Mark any "Deleted" entries to reflect this
     * @param pid the pid of the object
     * @param date the date of the change
     */
    void objectDeleted(String pid, Date date);

    /**
     * Object was changed to the published state. Mark any "published" entries to this
     * @param pid
     * @param date
     */
    void objectPublished(String pid, Date date);

    /**
     * Object was changed to/in the inProgress state.
     * @param pid
     * @param date
     */
    void objectChanged(String pid, Date date);

    List<Entry> lookup(Date since);

    void objectRelationsChanged(String pid, Date date);
}
