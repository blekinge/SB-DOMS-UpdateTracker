package dk.statsbiblioteket.doms.updatetracker.improved.database;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 4/27/11
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "OBJECTS")
public class DomsObject {
    private long id;

    @NaturalId
    private String objectPid;
    @NaturalId
    private String entryPid;
    @NaturalId
    private String viewAngle;

    public DomsObject() {
    }

    public DomsObject(String objectPid, String entryPid, String viewAngle) {
        this.objectPid = objectPid;
        this.entryPid = entryPid;
        this.viewAngle = viewAngle;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObjectPid() {
        return objectPid;
    }

    public void setObjectPid(String objectPid) {
        this.objectPid = objectPid;
    }

    public String getEntryPid() {
        return entryPid;
    }

    public void setEntryPid(String entryPid) {
        this.entryPid = entryPid;
    }

    public String getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(String viewAngle) {
        this.viewAngle = viewAngle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomsObject)) {
            return false;
        }

        DomsObject that = (DomsObject) o;

        if (entryPid != null ? !entryPid.equals(that.entryPid) : that.entryPid != null) {
            return false;
        }
        if (objectPid != null ? !objectPid.equals(that.objectPid) : that.objectPid != null) {
            return false;
        }
        if (viewAngle != null ? !viewAngle.equals(that.viewAngle) : that.viewAngle != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = objectPid != null ? objectPid.hashCode() : 0;
        result = 31 * result + (entryPid != null ? entryPid.hashCode() : 0);
        result = 31 * result + (viewAngle != null ? viewAngle.hashCode() : 0);
        return result;
    }
}

