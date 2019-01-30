package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.services.rest.data.EquipmentObservationRel2DataWrapper;
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
@Path("/api/v1/equipmentobservationrel2s")
public class EquipmentObservationRel2Controller {
    private final Logger logger = Logger.getLogger(EquipmentObservationRel2Controller.class.getName());
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EquipmentObservationRel2> getFullList()
    {
        logger.debug("equipment observations getFullList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper = new EquipmentObservationRel2DataWrapper();
        equipmentObservationRel2DataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S);
        jgc.listEquipmentObservationRel2s(equipmentObservationRel2DataWrapper);

        logger.debug("Rest service EquipmentObservationRel2 full list operation output -> " + equipmentObservationRel2DataWrapper.getMessage() );

        List<EquipmentObservationRel2> eos = equipmentObservationRel2DataWrapper.getEquipmentObservationRel2sArrayList();
        
        logger.debug("equipment observations getFullList: " + eos);

        return eos;

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/equipment/{equipmentId}")
    public List<EquipmentObservationRel2> getListForEquipment(@PathParam("equipmentId") String equipmentId)
    {
        logger.debug("equipment observations list for equipment " + equipmentId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper = new EquipmentObservationRel2DataWrapper();
        equipmentObservationRel2DataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT);
        equipmentObservationRel2DataWrapper.setEquipmentId(equipmentId);
        jgc.listEquipmentObservationRel2s(equipmentObservationRel2DataWrapper);

        logger.debug("Rest service EquipmentObservationRel2 list for equipment operation output -> " + equipmentObservationRel2DataWrapper.getMessage() );

        List<EquipmentObservationRel2> eos = equipmentObservationRel2DataWrapper.getEquipmentObservationRel2sArrayList();
        
        logger.debug("equipment observations list for equipment: " + eos);

        return eos;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/system")
    public List<EquipmentObservationRel2> getListForSystem()
    {
        logger.debug("equipment observations list for system ");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper = new EquipmentObservationRel2DataWrapper();
        equipmentObservationRel2DataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_SYSTEM);
        jgc.listEquipmentObservationRel2s(equipmentObservationRel2DataWrapper);

        logger.debug("Rest service EquipmentObservationRel2 list for system operation output -> " + equipmentObservationRel2DataWrapper.getMessage() );

        List<EquipmentObservationRel2> eos = equipmentObservationRel2DataWrapper.getEquipmentObservationRel2sArrayList();
        
        logger.debug("equipment observations list for system: " + eos);

        return eos;
    }
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentObservationRel2Id}")
    public EquipmentObservationRel2 getDetail(@PathParam("equipmentObservationRel2Id") String equipmentObservationRel2Id) {
        logger.debug("equipmentObservationRel2 getDetail - equipmentObservationRel2Id = " + equipmentObservationRel2Id);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper = new EquipmentObservationRel2DataWrapper();
        equipmentObservationRel2DataWrapper.setOntology(Constants.ONTO_GET_EQUIPMENT_OBSERVATION_REL2);
        equipmentObservationRel2DataWrapper.setEquipmentObservationRel2Id(equipmentObservationRel2Id);
        jgc.listEquipmentObservationRel2s(equipmentObservationRel2DataWrapper);

        logger.debug("Rest service EquipmentObservationRel2 get operation output -> " + equipmentObservationRel2DataWrapper.getMessage() );

        EquipmentObservationRel2 eo = equipmentObservationRel2DataWrapper.getEquipmentObservationRel2();
        
        logger.debug("equipment observation get: " + eo);

        return eo;
   }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EquipmentObservationRel2 insert(EquipmentObservationRel2 equipmentObservationRel2) {
        logger.debug("equipmentObservationRel2 insertRow - row to insert = " + equipmentObservationRel2);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        // jgc.newEquipmentObservation(equipmentObservation);
        // Constants.ONTO_NEW_EQUIPMENT_OBSERVATION
        
        EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper = new EquipmentObservationRel2DataWrapper();
        equipmentObservationRel2DataWrapper.setEquipmentObservationRel2(equipmentObservationRel2);
        equipmentObservationRel2DataWrapper.setOntology(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION_REL2);
        jgc.newEquipmentObservationRel2(equipmentObservationRel2DataWrapper);

        logger.debug("Rest service EquipmentObservationRel2 insert operation output -> " + equipmentObservationRel2DataWrapper.getMessage() );

        return equipmentObservationRel2;
   }   

/*    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentObservationRel2Id}")
    public EquipmentObservationRel2 update(@PathParam("equipmentObservationRel2Id") String equipmentObservationRel2Id, EquipmentObservationRel2 equipmentObservationRel2) {
        logger.debug("equipmentObservationRel2 update - equipmentObservationRel2Id = " + equipmentObservationRel2Id);
        logger.debug("equipmentObservationRel2 update = " + equipmentObservationRel2.toString());
        
        // TODO store into database
        
        return equipmentObservationRel2;
   }      
*/
}