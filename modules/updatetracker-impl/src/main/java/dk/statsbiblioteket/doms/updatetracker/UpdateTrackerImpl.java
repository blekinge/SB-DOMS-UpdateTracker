package dk.statsbiblioteket.doms.updatetracker;

import dk.statsbiblioteket.doms.webservices.authentication.Credentials;
import dk.statsbiblioteket.doms.webservices.configuration.ConfigCollection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Update tracker webservice. Provides upper layers of DOMS with info on changes
 * to objects in Fedora. Used by DOMS Server aka. Central to provide Summa with
 * said info.
 */
public class UpdateTrackerImpl {


    private DateFormat fedoraFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private DateFormat alternativefedoraFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");
    private Credentials creds;

    public UpdateTrackerImpl(Credentials credentials) {
        //To change body of created methods use File | Settings | File Templates.
        creds = credentials;
    }

    /**
     * Lists the entry objects of views (records) in Fedora, in the given
     * collection, that have changed since the given time.
     *
     * @param collectionPid The PID of the collection in which we are looking
     *                      for changes.
     * @param viewAngle     ...TODO doc
     * @param beginTime     The time since which we are looking for changes.
     * @param state         ...TODO doc
     * @return returns java.util.List<dk.statsbiblioteket.doms.updatetracker
     *         .webservice.PidDatePidPid>
     */
    public List<PidAndOther> listObjectsChangedSince(
            String collectionPid,
            String viewAngle,
            long beginTime,
            String state,
            Integer offset,
            Integer limit)
            throws BackendInvalidCredsException, BackendMethodFailedException {

        return getModifiedObjects(collectionPid,
                                  viewAngle,
                                  beginTime,
                                  state,
                                  offset,
                                  limit,
                                  false);
    }

    public List<PidAndOther> getModifiedObjects(String collectionPid,
                                                String viewAngle,
                                                long beginTime,
                                                String state,
                                                Integer offset,
                                                Integer limit,
                                                boolean reverse)
            throws BackendMethodFailedException, BackendInvalidCredsException {
        List<PidAndOther> result = new ArrayList<PidAndOther>();

        List<String> allEntryObjectsInRadioTVCollection;
        Fedora fedora;
        String fedoralocation = ConfigCollection.getProperties().getProperty(
                "dk.statsbiblioteket.doms.updatetracker.fedoralocation");
        fedora = new Fedora(creds, fedoralocation);


        if (state == null) {
            state = "Published";
        }
        if (state.equals("Published")) {
            state = "<fedora-model:Active>";
        } else if (state.equals("InProgress")) {
            state = "<fedora-model:Inactive>";
        } else {
            state = "<fedora-model:Active>";
        }

        String query = "select $object $cm $date\n"
                       + "from <#ri>\n"
                       + "where\n"
                       + "$object <fedora-model:hasModel> $cm\n"
                       + "and\n"
                       + "$cm <http://ecm.sourceforge.net/relations/0/2/#isEntryForViewAngle> '"
                       + viewAngle + "'\n"
                       + "and\n"
                       + "$object <http://doms.statsbiblioteket.dk/relations/default/0/1/#isPartOfCollection> <info:fedora/"
                       + collectionPid + ">\n"
                       + "and\n"
                       + "$object <fedora-model:state> " + state + "\n"
                       + "and\n"
                       + "$object <fedora-view:lastModifiedDate> $date \n";


        if (beginTime != 0){
            String beginTimeDate
                    = fedoraFormat.format(new Date(beginTime));
            query = query + "and \n $date <mulgara:after> '"+beginTimeDate+"'^^<xml-schema:dateTime> in <#xsd> \n";
        }


        if (reverse){
            query = query + "order by $date desc";
        } else {
            query = query + "order by $date asc";
        }

        if (limit != 0) {
            query = query + "\n limit " + limit;
        }
        if (offset != 0) {
            query = query + "\n offset " + offset;
        }


        allEntryObjectsInRadioTVCollection
                = fedora.query(query);

        for (String line : allEntryObjectsInRadioTVCollection) {
            String[] splitted = line.split(",");
            String lastModifiedFedoraDate = splitted[2];
            long lastChangedTime;
            try {
                lastModifiedFedoraDate = normalizeFedoraDate(lastModifiedFedoraDate);
                lastChangedTime = fedoraFormat.parse(
                        lastModifiedFedoraDate).getTime();
            } catch (ParseException e) {
                throw new BackendMethodFailedException(
                        "Failed to parse date for object",
                        e);
            }

            if (lastChangedTime < beginTime) {
                continue;
            }

            PidAndOther objectThatChanged = new PidAndOther();
            String pid = splitted[0];
            String entryCMPid = splitted[1];
            objectThatChanged.setPid(pid);
            objectThatChanged.setCollectionPid(collectionPid);
            objectThatChanged.setEntryCMPid(entryCMPid);
            objectThatChanged.setLastChangedTime(lastChangedTime);

            result.add(objectThatChanged);
        }

        return result;
    }

    private String normalizeFedoraDate(String lastModifiedFedoraDate) {
        if (lastModifiedFedoraDate.matches(".*\\.d{3}Z$")){
            return lastModifiedFedoraDate;
        } else if (lastModifiedFedoraDate.matches(".*\\.\\d{2}Z$")){
            return lastModifiedFedoraDate.substring(0,lastModifiedFedoraDate.length()-1)+"0Z";
        }else if (lastModifiedFedoraDate.matches(".*\\.\\d{1}Z$")){
            return lastModifiedFedoraDate.substring(0,lastModifiedFedoraDate.length()-1)+"00Z";
        }else if (lastModifiedFedoraDate.matches(".*:\\d\\dZ$")){
            return lastModifiedFedoraDate.substring(0,lastModifiedFedoraDate.length()-1)+".000Z";
        }
        return lastModifiedFedoraDate;
    }

    /**
     * Return the last time a view/record conforming to the content model of the
     * given content model entry, and in the given collection, has been changed.
     *
     * @param collectionPid The PID of the collection in which we are looking
     *                      for the last change.
     * @param viewAngle     ...TODO doc
     * @return The date/time of the last change.
     */
    public long getLatestModificationTime(
            String collectionPid,
            String viewAngle,
            String state) throws BackendInvalidCredsException, BackendMethodFailedException

    {

        List<PidAndOther> lastChanged = getModifiedObjects(collectionPid,
                                                           viewAngle,
                                                           0,
                                                           state,
                                                           0,
                                                           1,
                                                           true);

        if (!lastChanged.isEmpty()){
            return lastChanged.get(0).getLastChangedTime();
        } else {
            throw new BackendMethodFailedException("Did not find any elements in the collection");
        }
    }


}
