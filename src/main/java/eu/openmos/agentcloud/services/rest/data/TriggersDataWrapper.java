package eu.openmos.agentcloud.services.rest.data;

import eu.openmos.model.Base;
import eu.openmos.model.TriggeredRecipe;
import eu.openmos.model.TriggeredSkill;
import java.io.Serializable;
import java.util.ArrayList;

public class TriggersDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267760010L;      
    
    private String triggeredSkillId;
    private String triggeredRecipeId;
    private TriggeredRecipe triggeredRecipe;    
    private ArrayList<TriggeredRecipe> triggeredRecipesArrayList;
    private TriggeredSkill triggeredSkill;    
    private ArrayList<TriggeredSkill> triggeredSkillsArrayList;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

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

    public String getTriggeredSkillId() {
        return triggeredSkillId;
    }

    public void setTriggeredSkillId(String triggeredSkillId) {
        this.triggeredSkillId = triggeredSkillId;
    }

    public String getTriggeredRecipeId() {
        return triggeredRecipeId;
    }

    public void setTriggeredRecipeId(String triggeredRecipeId) {
        this.triggeredRecipeId = triggeredRecipeId;
    }

    
    public TriggeredRecipe getTriggeredRecipe() {
        return triggeredRecipe;
    }

    public void setTriggeredRecipe(TriggeredRecipe triggeredRecipe) {
        this.triggeredRecipe = triggeredRecipe;
    }

    public ArrayList<TriggeredRecipe> getTriggeredRecipesArrayList() {
        return triggeredRecipesArrayList;
    }

    public void setTriggeredRecipesArrayList(ArrayList<TriggeredRecipe> triggeredRecipesArrayList) {
        this.triggeredRecipesArrayList = triggeredRecipesArrayList;
    }

    public TriggeredSkill getTriggeredSkill() {
        return triggeredSkill;
    }

    public void setTriggeredSkill(TriggeredSkill triggeredSkill) {
        this.triggeredSkill = triggeredSkill;
    }

    public ArrayList<TriggeredSkill> getTriggeredSkillsArrayList() {
        return triggeredSkillsArrayList;
    }

    public void setTriggeredSkillsArrayList(ArrayList<TriggeredSkill> triggeredSkillsArrayList) {
        this.triggeredSkillsArrayList = triggeredSkillsArrayList;
    }

    
}
