package dk.statsbiblioteket.doms.updatetracker.webservice;

import dk.statsbiblioteket.doms.updatetracker.improved.Meat;
import dk.statsbiblioteket.doms.updatetracker.improved.database.Entry;
import dk.statsbiblioteket.doms.webservices.authentication.Credentials;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.lang.String;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Update tracker webservice. Provides upper layers of DOMS with info on changes
 * to objects in Fedora. Used by DOMS Server aka. Central to provide Summa with
 * said info.
 */
@WebService(endpointInterface
                    = "dk.statsbiblioteket.doms.updatetracker.webservice"
                      + ".UpdateTrackerWebservice")
public class UpdateTrackerWebserviceImpl implements UpdateTrackerWebservice {

    @Resource
    WebServiceContext context;

    private DateFormat fedoraFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private DateFormat alternativefedoraFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        try {
            Meat.startup();
            //TODO
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public UpdateTrackerWebserviceImpl() throws MethodFailedException {
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
     * @throws MethodFailedException
     * @throws InvalidCredentialsException
     */
    public List<PidDatePidPid> listObjectsChangedSince(
            @WebParam(name = "collectionPid", targetNamespace = "")
            String collectionPid,
            @WebParam(name = "viewAngle", targetNamespace = "")
            String viewAngle,
            @WebParam(name = "beginTime", targetNamespace = "")
            long beginTime,
            @WebParam(name = "state", targetNamespace = "")
            String state,
            @WebParam(name = "offset", targetNamespace = "") Integer offset,
            @WebParam(name = "limit", targetNamespace = "") Integer limit)


            throws InvalidCredentialsException, MethodFailedException

    {

        List<Entry> entries = Meat.getStore().lookup(new Date(beginTime), viewAngle, offset, limit, false);
        return convert(entries);

}

    private List<PidDatePidPid> convert(List<Entry> entries) {
        List<PidDatePidPid> list2 = new ArrayList<PidDatePidPid>(entries.size());
        for (Entry entry : entries) {
            list2.add(convert(entry));
        }
        return list2;
    }

    private PidDatePidPid convert(Entry thing) {
        PidDatePidPid thang = new PidDatePidPid();
        thang.setLastChangedTime(thing.getDateForChange().getTime());
        thang.setPid(thing.getEntryPid());
        return thang;
    }


    /**
     * Return the last time a view/record conforming to the content model of the
     * given content model entry, and in the given collection, has been changed.
     *
     * @param collectionPid The PID of the collection in which we are looking
     *                      for the last change.
     * @param viewAngle     ...TODO doc
     * @return The date/time of the last change.
     * @throws InvalidCredentialsException
     * @throws MethodFailedException
     */
    public long getLatestModificationTime(
            @WebParam(name = "collectionPid", targetNamespace = "")
            java.lang.String collectionPid,
            @WebParam(name = "viewAngle", targetNamespace = "")
            java.lang.String viewAngle,
            @WebParam(name = "state", targetNamespace = "")
            java.lang.String state)
            throws InvalidCredentialsException, MethodFailedException
    {

        List<Entry> entries = Meat.getStore().lookup(new Date(0), viewAngle, 0, 1,true);
        if (entries.size() != 1){
            return 0;
        } else {
            return entries.get(0).getDateForChange().getTime();
        }
    }

    /**
     * TODO doc
     *
     * @return TODO doc
     */
    private Credentials getCredentials() {
        HttpServletRequest request = (HttpServletRequest) context
                .getMessageContext()
                .get(MessageContext.SERVLET_REQUEST);
        Credentials creds = (Credentials) request.getAttribute("Credentials");
        if (creds == null) {
//            log.warn("Attempted call at Central without credentials");
            creds = new Credentials("", "");
        }
        return creds;
    }

}
