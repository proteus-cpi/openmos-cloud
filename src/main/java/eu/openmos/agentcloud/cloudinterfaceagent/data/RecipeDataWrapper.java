package eu.openmos.agentcloud.cloudinterfaceagent.data;

import eu.openmos.model.Base;
import eu.openmos.model.Recipe;
import java.io.Serializable;
import java.util.ArrayList;

public class RecipeDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757702L;      
    
    private Recipe[] recipes;    
    // private EquipmentObservation[] equipmentObservations;
    private ArrayList<Recipe> recipesArrayList;
    private String subSystemId;    

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public Recipe[] getRecipes() {
        return recipes;
    }

    public void setRecipes(Recipe[] recipes) {
        this.recipes = recipes;
    }

    public Recipe[] getRecipesBySubSystemId() {
        return recipes;
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
    
    public ArrayList<Recipe> getRecipesArrayList() {
        return recipesArrayList;
    }

    public void setRecipesArrayList(ArrayList<Recipe> recipesArrayList) {
        this.recipesArrayList = recipesArrayList;
    }

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String subSystemId) {
        this.subSystemId = subSystemId;
    }
}
