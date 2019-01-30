package eu.openmos.agentcloud.services.rest.data;

import eu.openmos.model.Base;
import eu.openmos.model.ProcessAssessment;
import java.io.Serializable;
import java.util.ArrayList;

public class ProcessAssessmentDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757713L;      
    
    private String recipeId;
    private String skillId;
    private String processAssessmentId;    
    
    private ProcessAssessment processAssessment;    
    private ArrayList<ProcessAssessment> processAssessmentsArrayList;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public ProcessAssessment getProcessAssessment() {
        return processAssessment;
    }

    public void setProcessAssessment(ProcessAssessment processAssessment) {
        this.processAssessment = processAssessment;
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
    
    public ArrayList<ProcessAssessment> getProcessAssessmentsArrayList() {
        return processAssessmentsArrayList;
    }

    public void setProcessAssessmentsArrayList(ArrayList<ProcessAssessment> processAssessmentsArrayList) {
        this.processAssessmentsArrayList = processAssessmentsArrayList;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getProcessAssessmentId() {
        return processAssessmentId;
    }

    public void setProcessAssessmentId(String processAssessmentId) {
        this.processAssessmentId = processAssessmentId;
    }
    
}
