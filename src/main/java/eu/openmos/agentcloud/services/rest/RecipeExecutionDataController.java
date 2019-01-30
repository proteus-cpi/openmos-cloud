package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.cloudinterfaceagent.data.RecipeExecutionDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.*;
import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/reds")
public class RecipeExecutionDataController {
    private final Logger logger = Logger.getLogger(RecipeExecutionDataController.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RecipeExecutionData> getList()
    {
        logger.debug("recipe execution data getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA);
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Rest service RecipeExecutionData list operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<RecipeExecutionData> eos = recipeExecutionDataWrapper.getRecipeExecutionDataArrayList();
        
        logger.debug("recipe execution data list: " + eos);

        return eos;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/query")
    public List<RecipeExecutionData> getList(@Context UriInfo redFilter)
    {
        logger.debug("recipe execution data getList");
        logger.debug("recipe execution data getList - filter = " + redFilter);
        
        String from = redFilter.getQueryParameters().getFirst("from");
        String to = redFilter.getQueryParameters().getFirst("to");
        String recipe = redFilter.getQueryParameters().getFirst("recipe");
        String product = redFilter.getQueryParameters().getFirst("product");
        String name = redFilter.getQueryParameters().getFirst("name");

        logger.debug("recipe execution data getList - filter from = " + from);
        logger.debug("recipe execution data getList - filter to = " + to);
        logger.debug("recipe execution data getList - filter recipe = " + recipe);
        logger.debug("recipe execution data getList - filter product  = " + product);
        logger.debug("recipe execution data getList - filter name = " + name);     
        
        boolean bFilters = false;
        RecipeExecutionDataFilter recipeExecutionDataFilter = new RecipeExecutionDataFilter();
        if (from != null)
        {
            recipeExecutionDataFilter.setStartInterval(from);
            bFilters = true;
        }
        if (to != null)
        {
            recipeExecutionDataFilter.setStopInterval(to);
            bFilters = true;
        }
        if (product != null)
        {
            recipeExecutionDataFilter.setProductInstanceId(product);
            bFilters = true;
        }
        if (recipe != null)
        {
            recipeExecutionDataFilter.setRecipeId(recipe);
            bFilters = true;
        }
        if (name != null)
        {
            recipeExecutionDataFilter.setKpiSettingName(name);
            bFilters = true;
        }
                    
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA);
        
        if (bFilters)
            recipeExecutionDataWrapper.setRecipeExecutionDataFilter(recipeExecutionDataFilter);
        
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Rest service RecipeExecutionData list operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<RecipeExecutionData> eos = recipeExecutionDataWrapper.getRecipeExecutionDataArrayList();
        
        logger.debug("recipe execution data list: " + eos);

        return eos;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/productinstancesperrecipe/{recipeId}")
    public List<ProductInstance> getProductInstancesPerRecipe(@PathParam("recipeId") String recipeId)
    {
        logger.debug("recipe execution data getProductInstancesPerRecipe");
        logger.debug("recipe execution data getProductInstancesPerRecipe - recipeId = " + recipeId);
        if (recipeId == null)
            return null;
        
        boolean bFilters = false;
        RecipeExecutionDataFilter recipeExecutionDataFilter = new RecipeExecutionDataFilter();
        if (recipeId != null)
        {
            recipeExecutionDataFilter.setRecipeId(recipeId);
            bFilters = true;
        }
                    
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE);
        
        if (bFilters)
            recipeExecutionDataWrapper.setRecipeExecutionDataFilter(recipeExecutionDataFilter);
        
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Rest service getProductsPerRecipe list operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<ProductInstance> eos = recipeExecutionDataWrapper.getProductInstancesPerRecipe();
        
        logger.debug("recipe execution data list: " + eos);

        return eos;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/recipesperproductinstance/{productInstanceId}")
    public List<Recipe> getRecipesPerProductInstance(@PathParam("productInstanceId") String productInstanceId)
    {
        logger.debug("recipe execution data getRecipesPerProductInstance");
        logger.debug("recipe execution data getRecipesPerProductInstance - productInstanceId = " + productInstanceId);
        if (productInstanceId == null)
            return null;
        
        boolean bFilters = false;
        RecipeExecutionDataFilter recipeExecutionDataFilter = new RecipeExecutionDataFilter();
        if (productInstanceId != null)
        {
            recipeExecutionDataFilter.setProductInstanceId(productInstanceId);
            bFilters = true;
        }
                    
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE);
        
        if (bFilters)
            recipeExecutionDataWrapper.setRecipeExecutionDataFilter(recipeExecutionDataFilter);
        
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Rest service getRecipesPerProductInstance list operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<Recipe> eos = recipeExecutionDataWrapper.getRecipesPerProductInstance();
        
        logger.debug("recipe execution data list: " + eos);

        return eos;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/kpisettingnamesperrecipe/{recipeId}")
    public List<String> getKPISettingNamesPerRecipe(@PathParam("recipeId") String recipeId)
    {
        logger.debug("recipe execution data getKPISettingNamesPerRecipe");
        logger.debug("recipe execution data getKPISettingNamesPerRecipe - recipeId = " + recipeId);
        if (recipeId == null)
            return null;
        
        boolean bFilters = false;
        RecipeExecutionDataFilter recipeExecutionDataFilter = new RecipeExecutionDataFilter();
        if (recipeId != null)
        {
            recipeExecutionDataFilter.setRecipeId(recipeId);
            bFilters = true;
        }
                    
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE);
        
        if (bFilters)
            recipeExecutionDataWrapper.setRecipeExecutionDataFilter(recipeExecutionDataFilter);
        
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Rest service getKPISettingNamesPerRecipe list operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<String> eos = recipeExecutionDataWrapper.getKPISettingNamesPerRecipe();
        
        logger.debug("recipe execution data list: " + eos);

        return eos;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/kpisettingnamesperproductandrecipe/{productInstanceId}/{recipeId}")
    public List<String> getKPISettingNamesPerProductAndRecipe(@PathParam("productInstanceId") String productInstanceId, @PathParam("recipeId") String recipeId)
    {
        logger.debug("recipe execution data getKPISettingNamesPerProductAndRecipe");
        logger.debug("recipe execution data getKPISettingNamesPerProductAndRecipe - productInstanceId = " + productInstanceId);
        logger.debug("recipe execution data getKPISettingNamesPerProductAndRecipe - recipeId = " + recipeId);
        if (productInstanceId == null)
            return null;
        if (recipeId == null)
            return null;
        
        boolean bFilters = false;
        RecipeExecutionDataFilter recipeExecutionDataFilter = new RecipeExecutionDataFilter();
        if (recipeId != null)
        {
            recipeExecutionDataFilter.setRecipeId(recipeId);
            bFilters = true;
        }
        if (productInstanceId != null)
        {
            recipeExecutionDataFilter.setProductInstanceId(productInstanceId);
            bFilters = true;
        }
                    
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE);
        
        if (bFilters)
            recipeExecutionDataWrapper.setRecipeExecutionDataFilter(recipeExecutionDataFilter);
        
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Rest service getKPISettingNamesPerProductAndRecipe list operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<String> eos = recipeExecutionDataWrapper.getKPISettingNamesPerRecipe();
        
        logger.debug("recipe execution data list (getKPISettingNamesPerProductAndRecipe): " + eos);

        return eos;
    }
}