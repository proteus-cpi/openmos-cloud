package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.services.rest.data.HMIPhysicalAdjustment;
import eu.openmos.agentcloud.services.rest.data.PhysicalAdjustmentDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.*;
import eu.openmos.model.utilities.SerializationConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 * REST controller for equipment physical adjustment HMI views.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/physicalAdjustments")
public class PhysicalAdjustmentController {
    private final Logger logger = Logger.getLogger(PhysicalAdjustmentController.class.getName());
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
  
/*     
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PhysicalAdjustment insert(PhysicalAdjustment physicalAdjustment) {
        logger.debug("physicalAdjustment insertRow - row to insert = " + physicalAdjustment);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper = new PhysicalAdjustmentDataWrapper();
        physicalAdjustmentDataWrapper.setEquipmentAdjustment(physicalAdjustment);
        physicalAdjustmentDataWrapper.setOntology(Constants.ONTO_NEW_PHYSICAL_ADJUSTMENT);
        jgc.newPhysicalAdjustment(physicalAdjustmentDataWrapper);

        logger.debug("Rest service PhysicalAdjustment insert operation output -> " + physicalAdjustmentDataWrapper.getMessage() );

        return physicalAdjustment;
   }   
*/    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhysicalAdjustment> getFullList()
    {
        logger.debug("physical adjustments getFullList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper = new PhysicalAdjustmentDataWrapper();
        physicalAdjustmentDataWrapper.setOntology(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS);
        jgc.listPhysicalAdjustments(physicalAdjustmentDataWrapper);

        logger.debug("Rest service PhysicalAdjustment full list operation output -> " + physicalAdjustmentDataWrapper.getMessage() );

        List<PhysicalAdjustment> eos = physicalAdjustmentDataWrapper.getPhysicalAdjustmentsArrayList();
        
        logger.debug("PhysicalAdjustment getFullList: " + eos);

        return eos;

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/equipment/{equipmentId}")
    public List<PhysicalAdjustment> getListForEquipment(@PathParam("equipmentId") String equipmentId)
    {
        logger.debug("PhysicalAdjustment list for equipment " + equipmentId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper = new PhysicalAdjustmentDataWrapper();
        physicalAdjustmentDataWrapper.setOntology(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT);
        physicalAdjustmentDataWrapper.setEquipmentId(equipmentId);
        jgc.listPhysicalAdjustments(physicalAdjustmentDataWrapper);

        logger.debug("Rest service PhysicalAdjustment list for equipment operation output -> " + physicalAdjustmentDataWrapper.getMessage() );

        List<PhysicalAdjustment> eos = physicalAdjustmentDataWrapper.getPhysicalAdjustmentsArrayList();
        
        logger.debug("PhysicalAdjustment list for equipment: " + eos);

        return eos;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{physicalAdjustmentId}")
    public PhysicalAdjustment getDetail(@PathParam("physicalAdjustmentId") String physicalAdjustmentId) {
        logger.debug("physicalAdjustment getDetail - physicalAdjustmentId = " + physicalAdjustmentId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper = new PhysicalAdjustmentDataWrapper();
        physicalAdjustmentDataWrapper.setOntology(Constants.ONTO_GET_PHYSICAL_ADJUSTMENT);
        physicalAdjustmentDataWrapper.setPhysicalAdjustmentId(physicalAdjustmentId);
        jgc.listPhysicalAdjustments(physicalAdjustmentDataWrapper);

        logger.debug("Rest service physicalAdjustment get operation output -> " + physicalAdjustmentDataWrapper.getMessage() );

        PhysicalAdjustment eo = physicalAdjustmentDataWrapper.getPhysicalAdjustment();
        
        logger.debug("physicalAdjustment get: " + eo);

        return eo;
   }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PhysicalAdjustment insert(HMIPhysicalAdjustment hmiPhysicalAdjustment) {
        logger.debug("physicalAdjustment insertRow - row to insert = " + hmiPhysicalAdjustment);
        
        //  CONVERSION FROM HMI OBJECT  TO REAL MODEL OBJECT
        Date today = new Date();
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        
        PhysicalAdjustment physicalAdjustment = new PhysicalAdjustment();
        physicalAdjustment.setDescription("Generated");
        physicalAdjustment.setEquipmentId(hmiPhysicalAdjustment.getEquipmentId());
        physicalAdjustment.setEquipmentType(hmiPhysicalAdjustment.getEquipmentType());
        physicalAdjustment.setName("Generated");
        physicalAdjustment.setPhysicalAdjustmentType("Physical");
        physicalAdjustment.setRegistered(today);
        physicalAdjustment.setSystemStage(hmiPhysicalAdjustment.getSystemStage());
        physicalAdjustment.setUniqueId("" + new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(today) + "_" + randomNumber);
        physicalAdjustment.setUserName(hmiPhysicalAdjustment.getUserName());
        physicalAdjustment.setUserText("Generated");
        
        PhysicalAdjustmentParameter pap = new PhysicalAdjustmentParameter();
        pap.setUniqueId(hmiPhysicalAdjustment.getPhysicalAdjustmentParameterId());
        
        PhysicalAdjustmentParameterSetting paps = new PhysicalAdjustmentParameterSetting();
        paps.setDescription("Generated");
        paps.setName("Generated");
        paps.setPhysicalAdjustmentParameter(pap);
        paps.setRegistered(today);
        
        int randomNumber2 = ThreadLocalRandom.current().nextInt(11, 20 + 1);
        paps.setUniqueId(physicalAdjustment.getUniqueId() + "_" + randomNumber2);
        paps.setValue(hmiPhysicalAdjustment.getNewValue());
                
        physicalAdjustment.setPhysicalAdjustmentParameterSetting(paps);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper = new PhysicalAdjustmentDataWrapper();
        physicalAdjustmentDataWrapper.setPhysicalAdjustment(physicalAdjustment);
        physicalAdjustmentDataWrapper.setOntology(Constants.ONTO_NEW_PHYSICAL_ADJUSTMENT);
        jgc.newPhysicalAdjustment(physicalAdjustmentDataWrapper);

        logger.debug("Rest service PhysicalAdjustment insert operation output -> " + physicalAdjustmentDataWrapper.getMessage() );

        return physicalAdjustment;
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