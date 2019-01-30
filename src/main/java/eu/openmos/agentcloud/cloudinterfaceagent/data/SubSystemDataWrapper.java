package eu.openmos.agentcloud.cloudinterfaceagent.data;

import eu.openmos.model.Base;
import eu.openmos.model.SubSystem;
import java.io.Serializable;
import java.util.ArrayList;

public class SubSystemDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757703L;      
    
    private SubSystem[] subSystems;    
    private ArrayList<SubSystem> subSystemsArrayList;
    
    private String subSystemId;
    private boolean subSystemRampUpChangeStatePossible;
    

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public SubSystem[] getSubSystems() {
        return subSystems;
    }

    public void setSubSystems(SubSystem[] subSystems) {
        this.subSystems = subSystems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }
    
    public ArrayList<SubSystem> getSubSystemsArrayList() {
        return subSystemsArrayList;
    }

    public void setSubSystemsArrayList(ArrayList<SubSystem> subSystemsArrayList) {
        this.subSystemsArrayList = subSystemsArrayList;
    }

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String subSystemId) {
        this.subSystemId = subSystemId;
    }

    public boolean isSubSystemRampUpChangeStatePossible() {
        return subSystemRampUpChangeStatePossible;
    }

    public void setSubSystemRampUpChangeStatePossible(boolean subSystemRampUpChangeStatePossible) {
        this.subSystemRampUpChangeStatePossible = subSystemRampUpChangeStatePossible;
    }        
}
