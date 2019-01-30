package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.services.rest.data.ProcessAssessmentDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.*;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/processAssessments")
public class ProcessAssessmentController {
    private final Logger logger = Logger.getLogger(ProcessAssessmentController.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProcessAssessment> getFullList()
    {
        logger.debug("process assessment getFullList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ProcessAssessmentDataWrapper processAssessmentDataWrapper = new ProcessAssessmentDataWrapper();
        processAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_PROCESS_ASSESSMENTS);
        jgc.listProcessAssessments(processAssessmentDataWrapper);

        logger.debug("Rest service ProcessAssessment full list operation output -> " + processAssessmentDataWrapper.getMessage() );

        List<ProcessAssessment> eos = processAssessmentDataWrapper.getProcessAssessmentsArrayList();
        
        logger.debug("process Assessment getFullList: " + eos);

        return eos;

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/recipe/{recipeId}")
    public List<ProcessAssessment> getListForRecipe(@PathParam("recipeId") String recipeId)
    {
        logger.debug("process Assessment list for recipe " + recipeId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ProcessAssessmentDataWrapper processAssessmentDataWrapper = new ProcessAssessmentDataWrapper();
        processAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE);
        processAssessmentDataWrapper.setRecipeId(recipeId);
        jgc.listProcessAssessments(processAssessmentDataWrapper);

        logger.debug("Rest service Process Assessment list for process operation output -> " + processAssessmentDataWrapper.getMessage() );

        List<ProcessAssessment> eos = processAssessmentDataWrapper.getProcessAssessmentsArrayList();
        
        logger.debug("process Assessment list for recipe: " + eos);

        return eos;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/skill/{skillId}")
    public List<ProcessAssessment> getListForSkill(@PathParam("skillId") String skillId)
    {
        logger.debug("process Assessment list for skill " + skillId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ProcessAssessmentDataWrapper processAssessmentDataWrapper = new ProcessAssessmentDataWrapper();
        processAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL);
        processAssessmentDataWrapper.setSkillId(skillId);
        jgc.listProcessAssessments(processAssessmentDataWrapper);

        logger.debug("Rest service Process Assessment list for process operation output -> " + processAssessmentDataWrapper.getMessage() );

        List<ProcessAssessment> eos = processAssessmentDataWrapper.getProcessAssessmentsArrayList();
        
        logger.debug("process Assessment list for skill: " + eos);

        return eos;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{processAssessmentId}")
    public ProcessAssessment getDetail(@PathParam("processAssessmentId") String processAssessmentId) {
        logger.debug("processAssessment getDetail - processAssessmentId = " + processAssessmentId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ProcessAssessmentDataWrapper processAssessmentDataWrapper = new ProcessAssessmentDataWrapper();
        processAssessmentDataWrapper.setOntology(Constants.ONTO_GET_PROCESS_ASSESSMENT);
        processAssessmentDataWrapper.setProcessAssessmentId(processAssessmentId);
        jgc.listProcessAssessments(processAssessmentDataWrapper);

        logger.debug("Rest service Process Assessment get operation output -> " + processAssessmentDataWrapper.getMessage() );

        ProcessAssessment eo = processAssessmentDataWrapper.getProcessAssessment();
        
        logger.debug("process Assessment get: " + eo);

        return eo;
   }    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ProcessAssessment insert(ProcessAssessment processAssessment) {
        logger.debug("processAssessment insertRow - row to insert = " + processAssessment);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ProcessAssessmentDataWrapper processAssessmentDataWrapper = new ProcessAssessmentDataWrapper();
        processAssessmentDataWrapper.setProcessAssessment(processAssessment);
        processAssessmentDataWrapper.setOntology(Constants.ONTO_NEW_PROCESS_ASSESSMENT);
        jgc.newProcessAssessment(processAssessmentDataWrapper);

        logger.debug("Rest service ProcessAssessment insert operation output -> " + processAssessmentDataWrapper.getMessage() );

        return processAssessment;
   }   

/*    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentAssessmentId}")
    public EquipmentAssessment update(@PathParam("equipmentAssessmentId") String equipmentAssessmentId, EquipmentAssessment equipmentAssessment) {
        logger.debug("equipmentAssessment update - equipmentAssessmentId = " + equipmentAssessmentId);
        logger.debug("equipmentAssessment update = " + equipmentAssessment.toString());
        
        // TODO store into database
        
        return equipmentAssessment;
   }      
*/
}