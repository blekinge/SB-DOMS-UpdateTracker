package dk.statsbiblioteket.doms.updatetracker.improved.database.hibernate;

import dk.statsbiblioteket.doms.updatetracker.improved.fedora.Fedora;
import dk.statsbiblioteket.doms.updatetracker.improved.fedora.ViewInfo;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/3/11
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class FedoraMockup extends Fedora{

    Set<String> objects = new HashSet<String>();

    Map<String,Set<String>> bundles = new HashMap<String,Set<String>>();

    protected void addObject(String pid){
        objects.add(pid);
    }

    protected void addEntry(String entrypid, String... objects){
        bundles.put(entrypid,new HashSet<String>(Arrays.asList(objects)));
        addObject(entrypid);
        for (String object : objects) {
            addObject(object);
        }
    }

    @Override
    public List<ViewInfo> getViewInfo(String pid) {
        if (bundles.containsKey(pid)){
            ArrayList<ViewInfo> list = new ArrayList<ViewInfo>();
            ViewInfo view = new ViewInfo("SummaVisible",true,pid);
            list.add(view);
            return list;

        }
        else {
            return new ArrayList<ViewInfo>();
        }
    }

    @Override
    public ViewBundle calcViewBundle(String entryPid, String viewAngle, Date date) {
        if (bundles.containsKey(entryPid)){
            ViewBundle bundle = new ViewBundle();
            bundle.entry = entryPid;
            bundle.viewAngle = viewAngle;
            bundle.contained = new ArrayList<String>(bundles.get(entryPid));
            return bundle;
        }
        return null;
    }
}
