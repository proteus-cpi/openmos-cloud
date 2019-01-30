package eu.openmos.agentcloud.services.rest.data;

import eu.openmos.model.Base;
import eu.openmos.model.EquipmentObservationRel2;
import java.io.Serializable;
import java.util.ArrayList;

public class EquipmentObservationRel2DataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757711L;      
    
    private EquipmentObservationRel2 equipmentObservationRel2;    
    private ArrayList<EquipmentObservationRel2> equipmentObservationRel2sArrayList;
    
    private String equipmentId;
    private String equipmentObservationRel2Id;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public EquipmentObservationRel2 getEquipmentObservationRel2() {
        return equipmentObservationRel2;
    }

    public void setEquipmentObservationRel2(EquipmentObservationRel2 equipmentObservationRel2) {
        this.equipmentObservationRel2 = equipmentObservationRel2;
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
    
    public ArrayList<EquipmentObservationRel2> getEquipmentObservationRel2sArrayList() {
        return equipmentObservationRel2sArrayList;
    }

    public void setEquipmentObservationRel2sArrayList(ArrayList<EquipmentObservationRel2> equipmentObservationRel2sArrayList) {
        this.equipmentObservationRel2sArrayList = equipmentObservationRel2sArrayList;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentObservationRel2Id() {
        return equipmentObservationRel2Id;
    }

    public void setEquipmentObservationRel2Id(String equipmentObservationRel2Id) {
        this.equipmentObservationRel2Id = equipmentObservationRel2Id;
    }
    
    
    
}
