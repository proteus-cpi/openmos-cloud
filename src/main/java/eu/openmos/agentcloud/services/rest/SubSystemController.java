package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.cloudinterfaceagent.data.SubSystemDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/subsystems")
public class SubSystemController {
    private final Logger logger = Logger.getLogger(SubSystemController.class.getName());
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{subSystemId}/isrampupchangestagepossible")
    public boolean isSubSystemRampUpChangeStagePossible(@PathParam("subSystemId") String subSystemId)
    {
        logger.debug("isSubSystemRampUpChangeStagePossible");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        SubSystemDataWrapper subSystemDataWrapper = new SubSystemDataWrapper();
        subSystemDataWrapper.setOntology(Constants.ONTO_IS_SUBSYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE);
        subSystemDataWrapper.setSubSystemId(subSystemId);
        jgc.checkIfSubSystemRampUpChangeStagePossible(subSystemDataWrapper);

        logger.debug("Rest service SubSystemController isSubSystemRampUpChangeStagePossible operation output -> " + subSystemDataWrapper.getMessage() );

        boolean b = subSystemDataWrapper.isSubSystemRampUpChangeStatePossible();
        
        logger.debug("isSubSystemRampUpChangeStagePossible: " + b);

        return b;
    }   
}