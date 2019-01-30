package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.cloudinterfaceagent.data.OrderInstanceDataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentAssessmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.TriggersDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.*;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/triggered")
public class TriggersController {
    private final Logger logger = Logger.getLogger(TriggersController.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/skills/{skillId}")
    public List<TriggeredSkill> getTriggeredSkillsList(@PathParam("skillId") String skillId)
    {
        logger.debug("getTriggeredSkillsList for skill " + skillId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        TriggersDataWrapper triggersDataWrapper = new TriggersDataWrapper();
        triggersDataWrapper.setOntology(Constants.ONTO_LIST_TRIGGERED_SKILLS);
        triggersDataWrapper.setTriggeredSkillId(skillId);
        jgc.listTriggeredSkills(triggersDataWrapper);

        logger.debug("getTriggeredSkillsList list operation output -> " + triggersDataWrapper.getMessage() );

        List<TriggeredSkill> eos = triggersDataWrapper.getTriggeredSkillsArrayList();
        
        logger.debug("triggered skills list: " + eos);

        return eos;
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/skills")
    public TriggeredSkill insert(TriggeredSkill triggeredSkill) {
        logger.debug("triggeredSkill insertRow - row to insert = " + triggeredSkill);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        TriggersDataWrapper triggersDataWrapper = new TriggersDataWrapper();
        triggersDataWrapper.setTriggeredSkill(triggeredSkill);
        triggersDataWrapper.setOntology(Constants.ONTO_NEW_TRIGGERED_SKILL);
        jgc.newTriggeredSkill(triggersDataWrapper);

        logger.debug("Rest service TriggeredSkill insert operation output -> " + triggersDataWrapper.getMessage() );

        return triggeredSkill;
   }   

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/recipes/{recipeId}")
    public List<TriggeredRecipe> getTriggeredRecipesList(@PathParam("recipeId") String recipeId)
    {
        logger.debug("getTriggeredRecipesList for recipe " + recipeId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        TriggersDataWrapper triggersDataWrapper = new TriggersDataWrapper();
        triggersDataWrapper.setOntology(Constants.ONTO_LIST_TRIGGERED_RECIPES);
        triggersDataWrapper.setTriggeredRecipeId(recipeId);
        jgc.listTriggeredRecipes(triggersDataWrapper);

        logger.debug("getTriggeredRecipesList list operation output -> " + triggersDataWrapper.getMessage() );

        List<TriggeredRecipe> eos = triggersDataWrapper.getTriggeredRecipesArrayList();
        
        logger.debug("triggered recipes list: " + eos);

        return eos;
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/recipes")
    public TriggeredRecipe insert(TriggeredRecipe triggeredRecipe) {
        logger.debug("triggeredRecipe insertRow - row to insert = " + triggeredRecipe);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        TriggersDataWrapper triggersDataWrapper = new TriggersDataWrapper();
        triggersDataWrapper.setTriggeredRecipe(triggeredRecipe);
        triggersDataWrapper.setOntology(Constants.ONTO_NEW_TRIGGERED_RECIPE);
        jgc.newTriggeredRecipe(triggersDataWrapper);

        logger.debug("Rest service TriggeredRecipe insert operation output -> " + triggersDataWrapper.getMessage() );

        return triggeredRecipe;
   }   
}