package eu.openmos.agentcloud.productagent;

import eu.openmos.agentcloud.agent.initiators.RecipeExecutionInitiator;
import eu.openmos.agentcloud.agent.responders.WorkstationInternalUpdateResponder;
import eu.openmos.model.Recipe;
import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.agentcloud.cyberphysicalagent.WebSocketMessageHandler;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.model.LogicalLocation;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.agentcloud.cyberphysicalagent.ProcessTakeDownRequestBehaviour;
import eu.openmos.agentcloud.cyberphysicalagent.WebSocketReceiver;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.ProductLeavingWorkstationOrTransportData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.SubSystem;
import eu.openmos.model.UnexpectedProductData;
import io.vertx.core.Vertx;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Agent that represents a product. It receives applied recipes from the resource
 * agents and new locations from the transport agent. 
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 *        
 */
public class ProductAgent extends CyberPhysicalAgent implements WebSocketMessageHandler {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(ProductAgent.class.getName()); 
    /**
     * List of already applied recipes since the product was created.
     */
    protected List<Recipe> appliedRecipes; 
    /**
     * List of physical locations the product has been to.
     */
    protected List<PhysicalLocation> physicalLocations;
    /**
     * List of logical locations the product has been to.
     */
    protected List<LogicalLocation> logicalLocations;
    /**
     * Deployment web socket ID.
     */
    protected String websocketDeploymentId;
    /**
     * 
     */
    protected ProductInstance productInstance;

    /**
     * Constructor inherited from the Cyber Physical Agent class.
     * 
     * @param locationPhysical - Physical Location of the product.
     * @param locationLogical - Logical location of the product.
     * @param cpad - Description of the product.
     */
//    public ProductAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, CyberPhysicalAgentDescription cpad) {
    public ProductAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, SubSystem cpad) {
        super(locationPhysical, locationLogical, cpad);
    } 

    /**
     * Constructor with only a product description as parameter.
     * 
     * @param description - product's description 
     */
    public ProductAgent() {}
    
    public ProductAgent(ProductInstance productInstance)
    {
        this.productInstance = productInstance;        
        logger.debug("product agent constructor -> " + productInstance);
    }

    public ProductInstance getProductInstance() {
        return productInstance;
    }

    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }
    
    

    /**
     * Setup method for the Product Agent. Registers the agent in the DF.
     * Launches the behaviours that will allow the agent to accept new locations
     * and newly applied recipes.
     */
    @Override
    protected void setup() {
        super.setup();
        logger.debug("Product Agent " + getLocalName() + " launched.");
        
        //DFInteraction.RegisterInDF(this, Constants.PRODUCT_SERVICE_NAME, Constants.DF_PRODUCT);
        DFInteraction.RegisterInDF(this, getLocalName(), Constants.DF_PRODUCT);
        
        addBehaviour(new NewLocationResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_LOCATION))));
        addBehaviour(new AppliedRecipeResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_APPLIED_RECIPE))));

        addBehaviour(new RemoveProductAgentResponder(this,
                MessageTemplate.and(MessageTemplate.MatchOntology(Constants.REMOVE_PRODUCT_AGENT), MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
                deletionDelayTime));
        
        addBehaviour(new ProductInstanceInternalUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_PRODUCT_INSTANCE_INTERNAL_UPDATE))));
        
        logger.debug(getClass().getName() + " - about to start websocket communication for product agent " + this.getLocalName());
        Vertx.vertx().deployVerticle(
            new WebSocketReceiver(this.getLocalName(), (WebSocketMessageHandler)this), res -> 
            {
                if (res.succeeded()) {
                    websocketDeploymentId = res.result();
                    logger.info(getClass().getName() + " - WebSocket deployment succedeed! Deployment id for product agent " + this.getLocalName() + " is [" + websocketDeploymentId + "]");
                } else
                    logger.warn(getClass().getName() + " - WebSocket deployment for product agent " + this.getLocalName() + " FAILED");
            }
        );     
    }

    /**
     * Default takedown method of the agent.
     */
    @Override
    protected void takeDown() {
        super.takeDown();
        logger.debug(getClass().getName() + " - " + this.getLocalName() + " - takeDown started");

        logger.debug(getClass().getName() + " - about to stop websocket stuff id = [" + websocketDeploymentId + "]");
        Vertx.vertx().undeploy(websocketDeploymentId, res -> 
        {
            if (res.succeeded()) {
                logger.info(getClass().getName() + " - WebSocket undeployment succedeed! Deployment id for product agent " + this.getLocalName() + " is [" + websocketDeploymentId + "]");
            } else {
                logger.warn(getClass().getName() + " - WebSocket undeployment for product agent " + this.getLocalName() + " FAILED");
            }
        }
        );
                
        logger.debug(getClass().getName() + " - " + this.getLocalName() + " - takeDown ended");
    }

    @Override
//    public void msgTypeLifeBeat(String msg) {
    public void msgTypeLifeBeat(RawEquipmentData msg) {
        String message = "Method msgTypeLifeBeat not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    // VALID FOR PRODUCT
    @Override
//    public void msgTypeExtractedData(String msg) {
    public void msgTypeExtractedData(RawEquipmentData msg) {
        String message = "Method msgTypeExtractedData not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    // VALID FOR PRODUCT
    @Override
    public void msgTypeNewLocation(String msg) {
        String message = "Method msgTypeNewLocation not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    @Override
//    public void msgTypeAppliedRecipes(String msg) {
    public void msgTypeAppliedRecipes(RecipeExecutionData msg) {
        String message = "Method msgTypeAppliedRecipes not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    @Override
//    public void msgTypeUnexpectedProduct(String msg) {
    public void msgTypeUnexpectedProduct(UnexpectedProductData msg) {
        String message = "Method msgTypeUnexpectedProduct not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    @Override
//    public void msgTypeProductArrived(String msg) {
    public void msgTypeProductArrived(UnexpectedProductData msg) {
        String message = "Method msgTypeProductArrived not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    @Override
//    public void msgTypeProductLeavingWorkstationOrTransport(String msg) {
    public void msgTypeProductLeavingWorkstationOrTransport(ProductLeavingWorkstationOrTransportData msg) {
        String message = "Method msgTypeProductLeavingWorkstationOrTransport not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    @Override
//    public void msgTypeRecipeExecutionData(String msg) {
    public void msgTypeRecipeExecutionData(RecipeExecutionData msg) {
//        String message = "Method msgTypeRecipeExecutionData not suitable for product agents";
//        logger.error(message);
        // throw new UnsupportedOperationException(message);
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type recipe execution data = [" + msg + "]");
        addBehaviour(new RecipeExecutionInitiator(this, RecipeExecutionInitiator.buildMessage(this, msg)));
    }

    @Override
//    public void msgTypeWorkstationUpdate(String msg) {
    public void msgTypeWorkstationUpdate(SubSystem msg) {
        String message = "Method msgTypeWorkstationUpdate not suitable for product agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    @Override
    public void msgTypeExecutionTableUpdate(ExecutionTable msg) {
        String message = "Method msgTypeExecutionTableUpdate not suitable for product agents";
        logger.error(message);
    }
}
