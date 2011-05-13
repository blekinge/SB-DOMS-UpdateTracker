/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package dk.statsbiblioteket.doms.updatetracker.improved.fedora;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A collection of utility methods for performing frequently required tasks.
 *
 * @author Ross Wayland
 */
public abstract class DateUtility {

    private static final Date ONE_BCE = new Date(-62167392000000L);

    private static final Date ONE_CE = new Date(-62135769600000L);

    /**
     * Converts a datetime string into and instance of java.util.Date using the
     * date format: yyyy-MM-ddTHH:mm:ss.SSSZ.
     *
     * @param dateTime
     *        A datetime string
     * @return Corresponding instance of java.util.Date (returns null if
     *         dateTime string argument is empty string or null)
     */
    public static Date convertStringToDate(String dateTime) {
        return parseDateLoose(dateTime);
    }

    /**
     * Converts an instance of java.util.Date into a String using the date
     * format: yyyy-MM-ddTHH:mm:ss.SSSZ.
     *
     * @param date
     *        Instance of java.util.Date.
     * @return ISO 8601 String representation (yyyy-MM-ddTHH:mm:ss.SSSZ) of the
     *         Date argument or null if the Date argument is null.
     */
    public static String convertDateToString(Date date) {
        return convertDateToString(date, true);
    }

    /**
     * Converts an instance of java.util.Date into an ISO 8601 String
     * representation. Uses the date format yyyy-MM-ddTHH:mm:ss.SSSZ or
     * yyyy-MM-ddTHH:mm:ssZ, depending on whether millisecond precision is
     * desired.
     *
     * @param date
     *        Instance of java.util.Date.
     * @param millis
     *        Whether or not the return value should include milliseconds.
     * @return ISO 8601 String representation of the Date argument or null if
     *         the Date argument is null.
     */
    public static String convertDateToString(Date date, boolean millis) {
        if (date == null) {
            return null;
        } else {
            DateFormat df;
            if (millis) {
                df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            } else {
                df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            }
            df.setTimeZone(TimeZone.getTimeZone("UTC"));

            if (date.before(ONE_CE)) {
                StringBuilder sb = new StringBuilder(df.format(date));
                sb.insert(0, "-");
                return sb.toString();
            } else {
                return df.format(date);
            }
        }
    }

    /**
     * Converts an instance of <code>Date</code> into the canonical lexical
     * representation of an XSD dateTime with the following exceptions: - Dates
     * before 1 CE (i.e. 1 AD) are handled according to ISO 8601:2000 Second
     * Edition: "0000" is the lexical representation of 1 BCE "-0001" is the
     * lexical representation of 2 BCE
     *
     * @param date
     *        Instance of java.util.Date.
     * @return the lexical form of the XSD dateTime value, e.g.
     *         "2006-11-13T09:40:55.001Z".
     * @see <a
     *      href="http://www.w3.org/TR/xmlschema-2/#date-canonical-representation">3.2.7.2
     *      Canonical representation</a>
     */
    public static String convertDateToXSDString(Date date) {
        if (date == null) {
            return null;
        }
        StringBuilder lexicalForm;
        String dateTime = convertDateToString(date, true);
        int len = dateTime.length() - 1;
        if (dateTime.indexOf('.', len - 4) != -1) {
            while (dateTime.charAt(len - 1) == '0') {
                len--; // fractional seconds may not with '0'.
            }
            if (dateTime.charAt(len - 1) == '.') {
                len--;
            }
            lexicalForm = new StringBuilder(dateTime.substring(0, len));
            lexicalForm.append('Z');
        } else {
            lexicalForm = new StringBuilder(dateTime);
        }

        if (date.before(ONE_CE)) {
            DateFormat df = new SimpleDateFormat("yyyy");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            StringBuilder year =
                    new StringBuilder(String.valueOf(Integer.parseInt(df
                            .format(date)) - 1));
            while (year.length() < 4) {
                year.insert(0, '0');
            }
            lexicalForm
                    .replace(0, lexicalForm.indexOf("-", 4), year.toString());
            if (date.before(ONE_BCE)) {
                lexicalForm.insert(0, "-");
            }
        }
        return lexicalForm.toString();
    }

    /**
     * Converts an instance of java.util.Date into a String using the date
     * format: yyyy-MM-ddZ.
     *
     * @param date
     *        Instance of java.util.Date.
     * @return Corresponding date string (returns null if Date argument is
     *         null).
     */
    public static String convertDateToDateString(Date date) {
        if (date == null) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            return df.format(date);
        }
    }

    /**
     * Converts an instance of java.util.Date into a String using the date
     * format: HH:mm:ss.SSSZ.
     *
     * @param date
     *        Instance of java.util.Date.
     * @return Corresponding time string (returns null if Date argument is
     *         null).
     */
    public static String convertDateToTimeString(Date date) {
        if (date == null) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            return df.format(date);
        }
    }

    /**
     * Convenience method for {@link #parseDateStrict(String)} which does not
     * throw an exception on error, but merely returns null.
     *
     * @param dateString
     *        the date string to parse
     * @return Date the date, if parse was successful; null otherwise
     */
    public static Date parseDateLoose(String dateString) {
        try {
            return parseDateStrict(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Convenience method for {@link #parseDateStrict(String)} with the
     * following difference: null or empty input returns null. Any other parse
     * errors are wrapped as an IllegalArgumentException.
     *
     * @param dateString the date string to parse
     * @return a Date representation of the dateString or null
     * @throws IllegalArgumentException if dateString is unable to be parsed.
     */
    public static Date parseDateOrNull(String dateString) throws IllegalArgumentException {
        if (dateString == null) {
            return null;
        } else if (dateString.isEmpty()) {
            return null;
        }
        try {
            return parseDateStrict(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Attempt to parse the given string of form: yyyy-MM-dd[THH:mm:ss[.SSS][Z]]
     * as a Date.
     *
     * @param dateString the date string to parse
     * @return a Date representation of the dateString
     * @throws ParseException if dateString is null, empty or is otherwise
     * unable to be parsed.
     */
    public static Date parseDateStrict(String dateString) throws ParseException {
        if (dateString == null) {
            throw new ParseException("Argument cannot be null.", 0);
        } else if (dateString.isEmpty()) {
            throw new ParseException("Argument cannot be empty.", 0);
        } else if (dateString.endsWith(".")) {
            throw new ParseException("dateString ends with invalid character.", dateString.length() - 1);
        }
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        int length = dateString.length();
        if (dateString.startsWith("-")) {
            length--;
        }
        if (dateString.endsWith("Z")) {
            if (length == 11) {
                formatter.applyPattern("yyyy-MM-dd'Z'");
            } else if (length == 20) {
                formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else if (length > 21 && length < 24) {
                // right-pad the milliseconds with 0s up to three places
                StringBuilder sb = new StringBuilder(dateString.substring(0, dateString.length() - 1));
                int dotIndex = sb.lastIndexOf(".");
                int endIndex = sb.length() - 1;
                int padding = 3 - (endIndex - dotIndex);
                for (int i = 0; i < padding; i++) {
                    sb.append("0");
                }
                sb.append("Z");
                dateString = sb.toString();
                formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            } else if (length == 24) {
                formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            }
        } else {
            if (length == 10) {
                formatter.applyPattern("yyyy-MM-dd");
            } else if (length == 19) {
                formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
            } else if (length > 20 && length < 23) {
                // right-pad millis with 0s
                StringBuilder sb = new StringBuilder(dateString);
                int dotIndex = sb.lastIndexOf(".");
                int endIndex = sb.length() - 1;
                int padding = 3 - (endIndex - dotIndex);
                for (int i = 0; i < padding; i++) {
                    sb.append("0");
                }
                dateString = sb.toString();
                formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            } else if (length == 23) {
                formatter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            } else if (dateString.endsWith("GMT") || dateString.endsWith("UTC")) {
                formatter.applyPattern("EEE, dd MMMM yyyyy HH:mm:ss z");
            }
        }
        return formatter.parse(dateString);
    }
}
