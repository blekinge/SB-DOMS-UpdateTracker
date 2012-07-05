package dk.statsbiblioteket.doms.updatetracker.improved.database;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/2/11
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewBundle {

    private String entry;

    private String viewAngle;

    private List<String> contained;


    public ViewBundle(String entry, String viewAngle, List<String> contained) {
        this.entry = entry;
        this.viewAngle = viewAngle;
        this.contained = contained;
    }

    public ViewBundle(String entry, String viewAngle) {
        this.entry = entry;
        this.viewAngle = viewAngle;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(String viewAngle) {
        this.viewAngle = viewAngle;
    }

    public List<String> getContained() {
        return contained;
    }

    public void setContained(List<String> contained) {
        this.contained = contained;
    }
}
