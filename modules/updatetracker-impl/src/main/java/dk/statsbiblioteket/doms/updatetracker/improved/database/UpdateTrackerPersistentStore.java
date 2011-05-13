package dk.statsbiblioteket.doms.updatetracker.improved.database;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/3/11
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UpdateTrackerPersistentStore {

    void setUp() throws Exception;

    void clear();

    /**
     * Invoke to register a new object, that has been created
     * @param pid
     * @param date
     */
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
     * Object was changed, but remains in the inProgress state
     * @param pid
     * @param date
     */
    void objectChanged(String pid, Date date);


    /**
     * Find objects from the database. TODO collections viewangles restrict
     * @param since
     * @return
     */
    List<Entry> lookup(Date since, String viewAngle, int offset, int limit, boolean newestFirst);


    /**
     * Objects have changes in the relations, so we will have to update the structure of the views
     * @param pid
     * @param date
     */
    void objectRelationsChanged(String pid, Date date);
}
