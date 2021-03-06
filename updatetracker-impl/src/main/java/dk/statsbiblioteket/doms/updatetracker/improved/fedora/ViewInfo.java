package dk.statsbiblioteket.doms.updatetracker.improved.fedora;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 4/28/11
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewInfo {

    private String viewAngle;

    private boolean entry;

    private String pid;

    private List<String> relations;

    private List<String> inverseRelations;

    public ViewInfo(String viewAngle, boolean entry, String pid) {
        this.viewAngle = viewAngle;
        this.entry = entry;
        this.pid = pid;
    }

    public List<String> getRelations() {
        return relations;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }

    public List<String> getInverseRelations() {
        return inverseRelations;
    }

    public void setInverseRelations(List<String> inverseRelations) {
        this.inverseRelations = inverseRelations;
    }

    public ViewInfo(String viewAngle, String pid) {
        this.viewAngle = viewAngle;
        this.pid = pid;
    }

    public String getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(String viewAngle) {
        this.viewAngle = viewAngle;
    }

    public boolean isEntry() {
        return entry;
    }

    public void setEntry(boolean entry) {
        this.entry = entry;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }




}
