package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.services.rest.data.EquipmentObservationDataWrapper;
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
@Deprecated
@Path("/api/v1/equipmentobservations")
public class EquipmentObservationController {
    private final Logger logger = Logger.getLogger(EquipmentObservationController.class.getName());
    
/*    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EquipmentObservation> getList()
    {
        logger.debug("equipment observations getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentObservationDataWrapper equipmentObservationDataWrapper = new EquipmentObservationDataWrapper();
        equipmentObservationDataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATIONS);
        jgc.listEquipmentObservations(equipmentObservationDataWrapper);

        logger.debug("Rest service EquipmentObservation list operation output -> " + equipmentObservationDataWrapper.getMessage() );

        // EquipmentObservation[] eoa = equipmentObservationDataWrapper.getEquipmentObservations();
        
        // List<EquipmentObservation> eos = Arrays.stream(eoa).collect(Collectors.toList());        
        // List<EquipmentObservation> eos = Arrays.asList(eoa);
        List<EquipmentObservation> eos = equipmentObservationDataWrapper.getEquipmentObservationsArrayList();
        
        logger.debug("equipment observations list: " + eos);

        return eos;

    }
*/
    
/*    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentObservationId}")
    public EquipmentObservation getDetail(@PathParam("equipmentObservationId") String equipmentObservationId) {
        logger.debug("equipmentObservation getDetail - equipmentObservationId = " + equipmentObservationId);
        
        // TODO load from database
        
        return new EquipmentObservation();
   }
*/
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EquipmentObservation insert(EquipmentObservation equipmentObservation) {
        logger.debug("equipmentObservation insertRow - row to insert = " + equipmentObservation);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        // jgc.newEquipmentObservation(equipmentObservation);
        // Constants.ONTO_NEW_EQUIPMENT_OBSERVATION
        
        EquipmentObservationDataWrapper equipmentObservationDataWrapper = new EquipmentObservationDataWrapper();
        equipmentObservationDataWrapper.setEquipmentObservation(equipmentObservation);
        equipmentObservationDataWrapper.setOntology(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION);
        jgc.newEquipmentObservation(equipmentObservationDataWrapper);

        logger.debug("Rest service EquipmentObservation insert operation output -> " + equipmentObservationDataWrapper.getMessage() );

        return equipmentObservation;
   }   

/*    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentObservationId}")
    public EquipmentObservation update(@PathParam("equipmentObservationId") String equipmentObservationId, EquipmentObservation equipmentObservation) {
        logger.debug("equipmentObservation update - equipmentObservationId = " + equipmentObservationId);
        logger.debug("equipmentObservation update = " + equipmentObservation.toString());
        
        // TODO store into database
        
        return equipmentObservation;
   }      
*/
    
}