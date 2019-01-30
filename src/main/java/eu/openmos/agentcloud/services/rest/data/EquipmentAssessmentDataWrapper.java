package eu.openmos.agentcloud.services.rest.data;

import eu.openmos.model.Base;
import eu.openmos.model.EquipmentAssessment;
import java.io.Serializable;
import java.util.ArrayList;

public class EquipmentAssessmentDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757713L;      
    
    private EquipmentAssessment equipmentAssessment;    
    private ArrayList<EquipmentAssessment> equipmentAssessmentsArrayList;
    private boolean rampUpChangeStagePossible;

    private String equipmentId;
    private String equipmentAssessmentId;
    
    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public EquipmentAssessment getEquipmentAssessment() {
        return equipmentAssessment;
    }

    public void setEquipmentAssessment(EquipmentAssessment equipmentAssessment) {
        this.equipmentAssessment = equipmentAssessment;
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
    
    public ArrayList<EquipmentAssessment> getEquipmentAssessmentsArrayList() {
        return equipmentAssessmentsArrayList;
    }

    public void setEquipmentAssessmentsArrayList(ArrayList<EquipmentAssessment> equipmentAssessmentsArrayList) {
        this.equipmentAssessmentsArrayList = equipmentAssessmentsArrayList;
    }

    public boolean isRampUpChangeStagePossible() {
        return rampUpChangeStagePossible;
    }

    public void setRampUpChangeStagePossible(boolean rampUpChangeStagePossible) {
        this.rampUpChangeStagePossible = rampUpChangeStagePossible;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentAssessmentId() {
        return equipmentAssessmentId;
    }

    public void setEquipmentAssessmentId(String equipmentAssessmentId) {
        this.equipmentAssessmentId = equipmentAssessmentId;
    }
    
    
    
}
