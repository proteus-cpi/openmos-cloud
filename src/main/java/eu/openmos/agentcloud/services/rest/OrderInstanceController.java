package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.cloudinterfaceagent.data.OrderInstanceDataWrapper;
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
@Path("/api/v1/orderinstances")
public class OrderInstanceController {
    private final Logger logger = Logger.getLogger(OrderInstanceController.class.getName());
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderInstance> getList()
    {
        logger.debug("order instances getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        OrderInstanceDataWrapper orderInstanceDataWrapper = new OrderInstanceDataWrapper();
        orderInstanceDataWrapper.setOntology(Constants.ONTO_LIST_ORDER_INSTANCES);
        jgc.listOrderInstances(orderInstanceDataWrapper);

        logger.debug("Rest service OrderInstance list operation output -> " + orderInstanceDataWrapper.getMessage() );

        List<OrderInstance> eos = orderInstanceDataWrapper.getOrderInstancesArrayList();
        
        logger.debug("order instances list: " + eos);

        return eos;

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/notyetproduced")
    public List<ProductInstance> listProductInstancesNotYetProduced()
    {
        logger.debug("product instances not yet produced getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        OrderInstanceDataWrapper orderInstanceDataWrapper = new OrderInstanceDataWrapper();
        orderInstanceDataWrapper.setOntology(Constants.ONTO_LIST_PRODUCT_INSTANCES_NOT_YET_PRODUCED);
        jgc.listProductInstancesNotYetProduced(orderInstanceDataWrapper);

        logger.debug("Rest service OrderInstance listProductInstancesNotYetProduced operation output -> " + orderInstanceDataWrapper.getMessage() );

        List<ProductInstance> eos = orderInstanceDataWrapper.getProductInstancesNotYetProducedArrayList();
        
        logger.debug("product instances not yet produced list: " + eos);

        return eos;

    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{orderInstanceId}")
    public OrderInstance getDetail(@PathParam("orderInstanceId") String orderInstanceId) {
        logger.debug("OrderInstance getDetail - orderInstanceId = " + orderInstanceId);
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        OrderInstanceDataWrapper orderInstanceDataWrapper = new OrderInstanceDataWrapper();
        orderInstanceDataWrapper.setOntology(Constants.ONTO_GET_ORDER_INSTANCE);
        orderInstanceDataWrapper.setOrderInstanceId(orderInstanceId);
        jgc.getOrderInstance(orderInstanceDataWrapper);

        logger.debug("Soap service getOrderInstance operation output -> " + orderInstanceDataWrapper.getMessage() );

        OrderInstance oi = orderInstanceDataWrapper.getOrderInstance();
        
        logger.debug("order instance: " + oi);

        return oi;
   }
}