package dk.statsbiblioteket.doms.updatetracker;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 4/13/11
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PidAndOther {

    protected java.lang.String pid;
    protected long lastChangedTime;
    protected java.lang.String collectionPid;
    protected java.lang.String entryCMPid;

    /**
     * Gets the value of the pid property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *
     */
    public java.lang.String getPid() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *
     */
    public void setPid(java.lang.String value) {
        this.pid = value;
    }

    /**
     * Gets the value of the lastChangedTime property.
     *
     */
    public long getLastChangedTime() {
        return lastChangedTime;
    }

    /**
     * Sets the value of the lastChangedTime property.
     *
     */
    public void setLastChangedTime(long value) {
        this.lastChangedTime = value;
    }

    /**
     * Gets the value of the collectionPid property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *
     */
    public java.lang.String getCollectionPid() {
        return collectionPid;
    }

    /**
     * Sets the value of the collectionPid property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *
     */
    public void setCollectionPid(java.lang.String value) {
        this.collectionPid = value;
    }

    /**
     * Gets the value of the entryCMPid property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *
     */
    public java.lang.String getEntryCMPid() {
        return entryCMPid;
    }

    /**
     * Sets the value of the entryCMPid property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *
     */
    public void setEntryCMPid(java.lang.String value) {
        this.entryCMPid = value;
    }



}
