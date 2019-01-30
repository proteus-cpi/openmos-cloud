package eu.openmos.agentcloud.cloudinterfaceagent.data;

import eu.openmos.model.Base;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.RecipeExecutionDataFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeExecutionDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757999L;      
    
    private RecipeExecutionData[] recipeExecutionData;    
    private ArrayList<RecipeExecutionData> recipeExecutionDataArrayList;
    private RecipeExecutionDataFilter recipeExecutionDataFilter;
    private ArrayList<ProductInstance> productInstancesPerRecipe;
    private ArrayList<Recipe> recipesPerProductInstance;
    private ArrayList<String> kpiSettingNamesPerRecipe;

    public ArrayList<String> getKPISettingNamesPerRecipe() {
        return kpiSettingNamesPerRecipe;
    }

    public void setKPISettingNamesPerRecipe(ArrayList<String> kpiSettingNamesPerRecipe) {
        this.kpiSettingNamesPerRecipe = kpiSettingNamesPerRecipe;
    }
    
    public ArrayList<ProductInstance> getProductInstancesPerRecipe() {
        return productInstancesPerRecipe;
    }

    public void setProductInstancesPerRecipe(ArrayList<ProductInstance> productInstancesPerRecipe) {
        this.productInstancesPerRecipe = productInstancesPerRecipe;
    }
    
    public ArrayList<Recipe> getRecipesPerProductInstance() {
        return recipesPerProductInstance;
    }

    public void setRecipesPerProductInstance(ArrayList<Recipe> recipesPerProductInstance) {
        this.recipesPerProductInstance = recipesPerProductInstance;
    }
    
    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public RecipeExecutionData[] getRecipeExecutionData() {
        return recipeExecutionData;
    }

    public void setRecipeExecutionData(RecipeExecutionData[] recipeExecutionData) {
        this.recipeExecutionData = recipeExecutionData;
    }

    public RecipeExecutionDataFilter getRecipeExecutionDataFilter() {
        return recipeExecutionDataFilter;
    }

    public void setRecipeExecutionDataFilter(RecipeExecutionDataFilter recipeExecutionDataFilter) {
        this.recipeExecutionDataFilter = recipeExecutionDataFilter;
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
    
    public ArrayList<RecipeExecutionData> getRecipeExecutionDataArrayList() {
        return recipeExecutionDataArrayList;
    }

    public void setRecipeExecutionDataArrayList(ArrayList<RecipeExecutionData> recipeExecutionDataArrayList) {
        this.recipeExecutionDataArrayList = recipeExecutionDataArrayList;
    }
}
