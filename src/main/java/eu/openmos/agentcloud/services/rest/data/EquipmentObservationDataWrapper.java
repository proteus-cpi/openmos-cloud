package eu.openmos.agentcloud.services.rest.data;

import eu.openmos.model.Base;
import eu.openmos.model.EquipmentObservation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipmentObservationDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757701L;      
    
    private EquipmentObservation equipmentObservation;    
    // private EquipmentObservation[] equipmentObservations;
    private ArrayList<EquipmentObservation> equipmentObservationsArrayList;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public EquipmentObservation getEquipmentObservation() {
        return equipmentObservation;
    }

    public void setEquipmentObservation(EquipmentObservation equipmentObservation) {
        this.equipmentObservation = equipmentObservation;
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
    
//    public EquipmentObservation[]  getEquipmentObservations() {
//        return equipmentObservations;
//    }
//
//    public void setEquipmentObservations(EquipmentObservation[]  equipmentObservations) {
//        this.equipmentObservations = equipmentObservations;
//    }

    public ArrayList<EquipmentObservation> getEquipmentObservationsArrayList() {
        return equipmentObservationsArrayList;
    }

    public void setEquipmentObservationsArrayList(ArrayList<EquipmentObservation> equipmentObservationsArrayList) {
        this.equipmentObservationsArrayList = equipmentObservationsArrayList;
    }
    
    
    
}
