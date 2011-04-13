package dk.statsbiblioteket.doms.updatetracker.webservice;

import dk.statsbiblioteket.doms.updatetracker.BackendInvalidCredsException;
import dk.statsbiblioteket.doms.updatetracker.BackendMethodFailedException;
import dk.statsbiblioteket.doms.updatetracker.PidAndOther;
import dk.statsbiblioteket.doms.updatetracker.UpdateTrackerImpl;
import dk.statsbiblioteket.doms.webservices.authentication.Credentials;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.lang.String;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

        try {
            List<PidAndOther> list = new UpdateTrackerImpl(getCredentials()).getModifiedObjects(collectionPid,
                                                        viewAngle,
                                                        beginTime,
                                                        state,
                                                        offset,
                                                        limit,
                                                        false);

            return convert(list);
        } catch (BackendInvalidCredsException e) {
            throw new InvalidCredentialsException("Invalid credentials", "", e);
        } catch (BackendMethodFailedException e) {
            throw new MethodFailedException("Method failed", "", e);
        }

    }

    private List<PidDatePidPid> convert(List<PidAndOther> list) {
        List<PidDatePidPid> list2 = new ArrayList<PidDatePidPid>(list.size());
        for (PidAndOther pidAndOther : list) {
            list2.add(convert(pidAndOther));
        }
        return list2;

    }

    private PidDatePidPid convert(PidAndOther thing) {
        PidDatePidPid thang = new PidDatePidPid();
        thang.setCollectionPid(thing.getCollectionPid());
        thang.setEntryCMPid(thing.getEntryCMPid());
        thang.setLastChangedTime(thing.getLastChangedTime());
        thang.setPid(thing.getPid());
        return thang;
    }

    public List<PidDatePidPid> getModifiedObjects(String collectionPid,
                                                  String viewAngle,
                                                  long beginTime,
                                                  String state,
                                                  Integer offset,
                                                  Integer limit,
                                                  boolean reverse
    )
            throws InvalidCredentialsException, MethodFailedException {
        try {
            List<PidAndOther> list = new UpdateTrackerImpl(getCredentials())
                    .getModifiedObjects(
                            collectionPid,
                            viewAngle,
                            beginTime,
                            state,
                            offset,
                            limit,
                            reverse);

            return convert(list);
        } catch (BackendInvalidCredsException e) {
            throw new InvalidCredentialsException("Invalid credentials", "", e);
        } catch (BackendMethodFailedException e) {
            throw new MethodFailedException("Method failed", "", e);
        }

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

        try {
            return new UpdateTrackerImpl(getCredentials()).getLatestModificationTime(collectionPid,
                                                                                     viewAngle,
                                                                                     state);
        } catch (BackendInvalidCredsException e) {
            throw new InvalidCredentialsException("Invalid credentials", "", e);
        } catch (BackendMethodFailedException e) {
            throw new MethodFailedException("Method failed", "", e);
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
