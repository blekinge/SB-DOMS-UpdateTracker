package dk.statsbiblioteket.doms.updatetracker.webservice.surveillance;

import dk.statsbiblioteket.doms.domsutil.surveyable.Severity;
import dk.statsbiblioteket.doms.domsutil.surveyable.Status;
import dk.statsbiblioteket.doms.domsutil.surveyable.StatusMessage;
import dk.statsbiblioteket.doms.domsutil.surveyable.Surveyable;
import dk.statsbiblioteket.doms.webservices.configuration.ConfigCollection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: Nov 23, 2010
 * Time: 4:09:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RealTimeService implements Surveyable{
    /** Logger for this class. */
    private Log log = LogFactory.getLog(getClass());

    /** The time when the getStatus method of this class is called. */
    private long timeOfGetStatusCall;

    /** The name of the system being surveyed by through this class. */
    private static final String SURVEYEE_NAME = "DomsUpdateTracker";

    /** Will be called by the webservice framework after the call of the
     * constructor. Reads parameters from web.xml.
     * This method serves as fault barrier. All exceptions are caught and
     * logged.
     */
    @PostConstruct
    // Will be called after the call of the constructor
    private void initialize() {
        log.trace("Entered method initialize()");
        try {
            ConfigCollection.getProperties();
        } catch (Exception e) {
            log.error("Exception caught by fault barrier", e);
        }
    }


    /** Returns only the current real time info.
     *
     * @param time This given date is ignored.
     * @return A status containing list of status messages.
     */
    public Status getStatusSince(long time) {
        log.trace("Entered method getStatusSince(" + time + ")");
        return getStatus();
    }


    /** Returns real time info about the current state of the highlevel
     * bitstorage webservice.
     * This method serves as fault barrier. All exceptions are caught and turned
     * into a status message.
     *
     * @return A status containing list of status messages.
     */
    public Status getStatus() {
        log.trace("Entered method getStatus()");
        Status status;

        timeOfGetStatusCall = System.currentTimeMillis();

        try {
            status = checkUpdateTrackerForCurrentState();
        } catch (Exception e) {
            log.error("Exception caught by fault barrier", e);
            // Create status covering exception
            status = makeStatus(Severity.RED,
                                "Exception caught by fault barrier: "
                                        + e.getMessage());
        }

        return status;
    }


    /** Tries to connect to highlevel-bitstorage and calls the status method
     * there.
     *
     * @return A status containing list of status messages.
     *
     * @throws UpdateTrackerSoapException on trouble calling status
     * message.
     */
    private Status checkUpdateTrackerForCurrentState()
            throws UpdateTrackerSoapException {
        log.trace("Entered method checkUpdateTrackerForCurrentState()");

        return makeStatus(Severity.GREEN,"UpdateTracker is running");
    }


    /** Constructs a status containing a status message with the input severity,
     * input message, and the current system time.
     *
     * @param severity The severity to be given to the returned status
     * @param message The message to be entered in the returned status
     * @return A status containing a message with the given severity and message
     * text
     */
    private Status makeStatus(Severity severity, String message) {
        log.trace("Entered method makeStatus('" + severity + "', '" + message
                  + "')");
        StatusMessage statusMessage = new StatusMessage();
        Status status = new Status();

        statusMessage.setMessage(message);
        statusMessage.setSeverity(severity);
        statusMessage.setTime(timeOfGetStatusCall);

        status.setName(SURVEYEE_NAME);
        status.getMessages().add(statusMessage);
        return status;
    }


}
