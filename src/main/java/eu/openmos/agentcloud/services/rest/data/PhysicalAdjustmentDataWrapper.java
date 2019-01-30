package eu.openmos.agentcloud.services.rest.data;

import eu.openmos.model.Base;
import eu.openmos.model.PhysicalAdjustment;
import java.io.Serializable;
import java.util.ArrayList;

public class PhysicalAdjustmentDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757793L;      

    private String equipmentId;
    private String physicalAdjustmentId;
    
    private PhysicalAdjustment physicalAdjustment;    
    private ArrayList<PhysicalAdjustment> physicalAdjustmentsArrayList;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public PhysicalAdjustment getPhysicalAdjustment() {
        return physicalAdjustment;
    }

    public void setPhysicalAdjustment(PhysicalAdjustment physicalAdjustment) {
        this.physicalAdjustment = physicalAdjustment;
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
    
    public ArrayList<PhysicalAdjustment> getPhysicalAdjustmentsArrayList() {
        return physicalAdjustmentsArrayList;
    }

    public void setPhysicalAdjustmentsArrayList(ArrayList<PhysicalAdjustment> physicalAdjustmentsArrayList) {
        this.physicalAdjustmentsArrayList = physicalAdjustmentsArrayList;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getPhysicalAdjustmentId() {
        return physicalAdjustmentId;
    }

    public void setPhysicalAdjustmentId(String physicalAdjustmentId) {
        this.physicalAdjustmentId = physicalAdjustmentId;
    }
    
}
