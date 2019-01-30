package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.services.rest.data.EquipmentAssessmentDataWrapper;
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
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/equipmentAssessments")
public class EquipmentAssessmentController {
    private final Logger logger = Logger.getLogger(EquipmentAssessmentController.class.getName());
/*    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EquipmentAssessment> getList()
    {
        logger.debug("equipment assessments getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper = new EquipmentAssessmentDataWrapper();
        equipmentAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS);
        jgc.listEquipmentAssessments(equipmentAssessmentDataWrapper);

        logger.debug("Rest service EquipmentAssessment list operation output -> " + equipmentAssessmentDataWrapper.getMessage() );

        List<EquipmentAssessment> eos = equipmentAssessmentDataWrapper.getEquipmentAssessmentsArrayList();
        
        logger.debug("equipment assessments list: " + eos);

        return eos;
    }
*/
    
/*    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentAssessmentId}")
    public EquipmentAssessment getDetail(@PathParam("equipmentAssessmentId") String equipmentAssessmentId) {
        logger.debug("equipmentAssessment getDetail - equipmentAssessmentId = " + equipmentAssessmentId);
        
        // TODO load from database
        
        return new EquipmentAssessment();
   }
*/

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EquipmentAssessment> getFullList()
    {
        logger.debug("equipment assessment getFullList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper = new EquipmentAssessmentDataWrapper();
        equipmentAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS);
        jgc.listEquipmentAssessments(equipmentAssessmentDataWrapper);

        logger.debug("Rest service EquipmentAssessment full list operation output -> " + equipmentAssessmentDataWrapper.getMessage() );

        List<EquipmentAssessment> eos = equipmentAssessmentDataWrapper.getEquipmentAssessmentsArrayList();
        
        logger.debug("equipment Assessment getFullList: " + eos);

        return eos;

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/equipment/{equipmentId}")
    public List<EquipmentAssessment> getListForEquipment(@PathParam("equipmentId") String equipmentId)
    {
        logger.debug("equipment Assessment list for equipment " + equipmentId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper = new EquipmentAssessmentDataWrapper();
        equipmentAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT);
        equipmentAssessmentDataWrapper.setEquipmentId(equipmentId);
        jgc.listEquipmentAssessments(equipmentAssessmentDataWrapper);

        logger.debug("Rest service Equipment Assessment list for equipment operation output -> " + equipmentAssessmentDataWrapper.getMessage() );

        List<EquipmentAssessment> eos = equipmentAssessmentDataWrapper.getEquipmentAssessmentsArrayList();
        
        logger.debug("equipment Assessment list for equipment: " + eos);

        return eos;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/system")
    public List<EquipmentAssessment> getListForSystem()
    {
        logger.debug("equipment Assessment list for system ");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper = new EquipmentAssessmentDataWrapper();
        equipmentAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_SYSTEM);
        jgc.listEquipmentAssessments(equipmentAssessmentDataWrapper);

        logger.debug("Rest service Equipment Assessment list for system operation output -> " + equipmentAssessmentDataWrapper.getMessage() );

        List<EquipmentAssessment> eos = equipmentAssessmentDataWrapper.getEquipmentAssessmentsArrayList();
        
        logger.debug("equipment Assessment list for system: " + eos);

        return eos;
    }
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentAssessmentId}")
    public EquipmentAssessment getDetail(@PathParam("equipmentAssessmentId") String equipmentAssessmentId) {
        logger.debug("equipmentAssessment getDetail - equipmentAssessmentId = " + equipmentAssessmentId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper = new EquipmentAssessmentDataWrapper();
        equipmentAssessmentDataWrapper.setOntology(Constants.ONTO_GET_EQUIPMENT_ASSESSMENT);
        equipmentAssessmentDataWrapper.setEquipmentAssessmentId(equipmentAssessmentId);
        jgc.listEquipmentAssessments(equipmentAssessmentDataWrapper);

        logger.debug("Rest service Equipment Assessment get operation output -> " + equipmentAssessmentDataWrapper.getMessage() );

        EquipmentAssessment eo = equipmentAssessmentDataWrapper.getEquipmentAssessment();
        
        logger.debug("equipment Assessment get: " + eo);

        return eo;
   }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EquipmentAssessment insert(EquipmentAssessment equipmentAssessment) {
        logger.debug("equipmentAssessment insertRow - row to insert = " + equipmentAssessment);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper = new EquipmentAssessmentDataWrapper();
        equipmentAssessmentDataWrapper.setEquipmentAssessment(equipmentAssessment);
        equipmentAssessmentDataWrapper.setOntology(Constants.ONTO_NEW_EQUIPMENT_ASSESSMENT);
        jgc.newEquipmentAssessment(equipmentAssessmentDataWrapper);

        logger.debug("Rest service EquipmentAssessment insert operation output -> " + equipmentAssessmentDataWrapper.getMessage() );

        return equipmentAssessment;
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
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/isrampupchangestagepossible")
    public boolean isRampUpChangeStagePossible()
    {
        logger.debug("isRampUpChangeStagePossible");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper = new EquipmentAssessmentDataWrapper();
        equipmentAssessmentDataWrapper.setOntology(Constants.ONTO_IS_SYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE);
        jgc.checkIfSystemRampUpChangeStagePossible(equipmentAssessmentDataWrapper);

        logger.debug("Rest service EquipmentAssessment isRampUpChangeStagePossible operation output -> " + equipmentAssessmentDataWrapper.getMessage() );

        boolean b = equipmentAssessmentDataWrapper.isRampUpChangeStagePossible();
        
        logger.debug("isRampUpChangeStagePossible: " + b);

        return b;

    }
    
}