package dk.statsbiblioteket.doms.updatetracker.webservice;

import dk.statsbiblioteket.doms.webservices.Credentials;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.net.MalformedURLException;

@WebService(endpointInterface
        = "dk.statsbiblioteket.doms.updatetracker.webservice"
          + ".UpdateTrackerWebservice")
public class UpdateTrackerWebserviceImpl implements UpdateTrackerWebservice{

    @Resource
    WebServiceContext context;


    private XMLGregorianCalendar lastChangedTime;

    public UpdateTrackerWebserviceImpl() throws MethodFailedException {


        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone(
                "Europe/Copenhagen"));
        calendar.set(GregorianCalendar.YEAR, 1999);
        calendar.set(GregorianCalendar.MONTH, 12);
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 31);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 23);
        calendar.set(GregorianCalendar.MINUTE, 59);
        calendar.set(GregorianCalendar.SECOND, 59);
        calendar.set(GregorianCalendar.MILLISECOND, 999);

        try {
            lastChangedTime
                    = DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    calendar);
        } catch (DatatypeConfigurationException e) {
            throw new MethodFailedException(
                    "Could not make new XMLGregorianCalendar", "");
        }
    }

    /**
     * TODO javadoc
     *
     * @param collectionPid
     * @param entryCMPid
     * @param beginTime
     * @return returns java.util.List<dk.statsbiblioteket.doms.updatetracker
     * .webservice.PidDatePidPid>
     *
     * @throws MethodFailedException
     * @throws InvalidCredentialsException
     *
     */
    public List<PidDatePidPid> listObjectsChangedSince(
            @WebParam(name = "collectionPid", targetNamespace = "")
            String collectionPid,
            @WebParam(name = "entryCMPid", targetNamespace = "")
            String entryCMPid,
            @WebParam(name = "viewAngle", targetNamespace = "")
            String viewAngle,
            @WebParam(name = "beginTime", targetNamespace = "")
            XMLGregorianCalendar beginTime)
            throws InvalidCredentialsException, MethodFailedException {

        // TODO Un-mockup this class please :-)

        List<PidDatePidPid> result = new ArrayList<PidDatePidPid>();



        if (beginTime.toGregorianCalendar().after(
                lastChangedTime.toGregorianCalendar())) {
            return result;
        }

        // TODO Mockup by calling the getAllEntryObjectsInCollection method in
        // ECM with collectionPID to get <PID, collectionPID, entryPID>.

        String pidOfCollection = "doms:RadioTV_Collection";
        List<String> allEntryObjectsInRadioTVCollection;
        ECM ecmConnector = null;
        try {
            ecmConnector = new ECM(getCredentials(), "http://alhena:7980/ecm");
        } catch (MalformedURLException e) {
            throw new MethodFailedException("Malformed URL", "", e);
        }

        try {
            allEntryObjectsInRadioTVCollection
                    = ecmConnector.getAllEntryObjectsInCollection(
                    pidOfCollection, "", "");
        } catch (BackendInvalidCredsException e) {
            throw new InvalidCredentialsException("Invalid credentials", "", e);
        } catch (BackendMethodFailedException e) {
            throw new MethodFailedException("Method failed", "", e);
        }

        for (String pid : allEntryObjectsInRadioTVCollection) {
            PidDatePidPid objectThatChanged = new PidDatePidPid();
            objectThatChanged.setPid(pid);
            objectThatChanged.setLastChangedTime(lastChangedTime);
            objectThatChanged.setCollectionPid(collectionPid);
            objectThatChanged.setEntryCMPid(entryCMPid);

            result.add(objectThatChanged);
        }

        return result;
    }

    public XMLGregorianCalendar getLatestModificationTime(
            @WebParam(name = "collectionPid", targetNamespace = "")
            String collectionPid,
            @WebParam(name = "entryCMPid", targetNamespace = "")
            String entryCMPid,
            @WebParam(name = "viewAngle", targetNamespace = "")
            String viewAngle)
            throws InvalidCredentialsException, MethodFailedException {
        return lastChangedTime;
    }

    /**
     *
     * @return
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
