package dk.statsbiblioteket.doms.updatetracker.improved.database;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 4/27/11
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ENTRIES")
public class Entry {
    private long id;

    @NaturalId
    private String entryPid;
    @NaturalId
    private String viewAngle;
    @NaturalId
    private String state;

    private Date dateForChange;


    public Entry() {
    }

    public Entry(String entryPid, String viewAngle, String state, Date dateForChange) {
        this.entryPid = entryPid;
        this.viewAngle = viewAngle;
        this.state = state;
        this.dateForChange = dateForChange;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateForChange() {
        return dateForChange;
    }

    public void setDateForChange(Date dateForChange) {
        this.dateForChange = dateForChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entry)) {
            return false;
        }

        Entry entry = (Entry) o;

        if (dateForChange != null ? !dateForChange.equals(entry.dateForChange) : entry.dateForChange != null) {
            return false;
        }
        if (entryPid != null ? !entryPid.equals(entry.entryPid) : entry.entryPid != null) {
            return false;
        }
        if (state != null ? !state.equals(entry.state) : entry.state != null) {
            return false;
        }
        if (viewAngle != null ? !viewAngle.equals(entry.viewAngle) : entry.viewAngle != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = entryPid != null ? entryPid.hashCode() : 0;
        result = 31 * result + (viewAngle != null ? viewAngle.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (dateForChange != null ? dateForChange.hashCode() : 0);
        return result;
    }
}
