package dk.statsbiblioteket.doms.updatetracker.improved.database;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 4/15/11
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UpdateTrackerPersistentStore {



    Token updateSimpleObject(String pid, Map<String, String> viewAnglesAndInjectObjects);

    Token removeObject(String pid);

    Token markEntryObjectPublished(String pid, Map<String, List<String>> viewAnglesToObjectLists);



}
