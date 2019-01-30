package eu.openmos.agentcloud.resourceagent;

import eu.openmos.agentcloud.agent.initiators.ExecutionTableUpdateInitiator;
import eu.openmos.agentcloud.agent.initiators.ProductLeavingWorkstationOrTransportInitiator;
import eu.openmos.agentcloud.agent.initiators.RecipeExecutionInitiator;
import eu.openmos.agentcloud.agent.initiators.UnexpectedProductInitiator;
import eu.openmos.agentcloud.agent.initiators.WorkstationUpdateInitiator;
import eu.openmos.agentcloud.agent.responders.CPAExecutionTableUpdateResponder;
import eu.openmos.agentcloud.agent.responders.SubSystemRecipeUpdateResponder;
import eu.openmos.agentcloud.agent.responders.SubSystemRecipesListUpdateResponder;
import eu.openmos.agentcloud.agent.responders.WorkstationInternalUpdateResponder;
import eu.openmos.agentcloud.cyberphysicalagent.WebSocketReceiver;
import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.Recipe;
import eu.openmos.model.LogicalLocation;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.agentcloud.cyberphysicalagent.WebSocketMessageHandler;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.model.ProductLeavingWorkstationOrTransportData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.SubSystem;
import eu.openmos.model.UnexpectedProductData;
import io.vertx.core.Vertx;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 * Agent that represents a resource. It deploys several recipes into the  
 * Manufacturing Service Bus. It also notifies the optimizer of new data 
 * regarding the hardware. It notifies the product agent when a new recipe has
 * been applied.
 * TO DO: Valerio needs to explain messages types.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * 
 */
public class ResourceAgent extends CyberPhysicalAgent implements WebSocketMessageHandler {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(ResourceAgent.class.getName());
    /**
     * List of deployed recipes.
     */
    protected LinkedList<Recipe> deployedRecipes = new LinkedList<>();   
    /**
     * List of active recipes.
     */
    protected LinkedList<Recipe> activeRecipes = new LinkedList<>();  
    /**
     *  Missed life beat boolean.
     */
    protected boolean missedLifeBeat = false;
    /**
     * Deployment web socket ID.
     */
    protected String websocketDeploymentId;
    
    /**
     * Constructor inherited from the Cyber Physical Agent class.
     * 
     * @param locationPhysical - Physical Location of the product.
     * @param locationLogical - Logical location of the product.
     * @param cpad - Description of the product.
     */
//    public ResourceAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, CyberPhysicalAgentDescription cpad) {
    public ResourceAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, SubSystem cpad) {
        super(locationPhysical, locationLogical, cpad);
    }
     
    /**
     * Setup method for the Resource Agent. Registers the agent in the DF.
     * Launches the behaviours that will allow the agent to accept new 
     * recipes from the optimizer and to register in the optimizer.
     * TO DO: Valerio needs to explain web sockets.
     */
    @Override
    protected void setup() {
        super.setup();
        logger.debug(getClass().getName() + " - Resource Agent " + getLocalName() + " launched.");
        
        DFInteraction.RegisterInDF(this, Constants.RESOURCE_SERVICE_NAME, Constants.DF_RESOURCE);
        
        if (getCpad().getRecipes() != null)
        {
            activeRecipes.addAll(getCpad().getRecipes());
            deployedRecipes.addAll(getCpad().getRecipes());
        }
        
//        addBehaviour(new NewResourceInitiator(this, NewResourceInitiator.BuildMessage(this, getCpad().toString())));
        addBehaviour(new NewResourceInitiator(this, NewResourceInitiator.buildMessage(this, getCpad())));
        
        addBehaviour(new DeployOptimizedRecipesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_RECIPES))));
        addBehaviour(new DeploySuggestedRecipesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_SUGGEST_RECIPES))));
        addBehaviour(new SOPOptimizedRecipesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_SOP))));
               
        addBehaviour(new CPAExecutionTableUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_START_EXECUTION_TABLE_UPDATE))));
        addBehaviour(new SubSystemRecipesListUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_START_SUBSYSTEM_RECIPES_LIST_UPDATE))));
        addBehaviour(new SubSystemRecipeUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_START_SUBSYSTEM_RECIPE_UPDATE))));
        // VaG - 21/10/2016
        // MessageTrackerWSClient.track("RA", "constructor", "New resource agent " + this.getLocalName() + " coming up");

        addBehaviour(new WorkstationInternalUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_WORKSTATION_INTERNAL_UPDATE))));
        
        logger.debug(getClass().getName() + " - about to start websocket communication for resource agent " + this.getLocalName());
        Vertx.vertx().deployVerticle(
            new WebSocketReceiver(this.getLocalName(), (WebSocketMessageHandler)this), res -> 
            {
                if (res.succeeded()) {
                    websocketDeploymentId = res.result();
                    logger.info(getClass().getName() + " - WebSocket deployment succedeed! Deployment id for resource agent " + this.getLocalName() + " is [" + websocketDeploymentId + "]");
                } else
                    logger.warn(getClass().getName() + " - WebSocket deployment for resource agent " + this.getLocalName() + " FAILED");
            }
        );     
    }

    /**
     * Default takedown method of the agent.
     * TO DO: Valerio needs to explain sockets.
     */
    @Override
    protected void takeDown() {
        super.takeDown();
        logger.debug(getClass().getName() + " - " + this.getLocalName() + " - takeDown started");
        
        logger.debug(getClass().getName() + " - about to stop websocket stuff id = [" + websocketDeploymentId + "]");
        Vertx.vertx().undeploy(websocketDeploymentId, res -> 
        {
            if (res.succeeded()) {
                logger.info(getClass().getName() + " - WebSocket undeployment succedeed! Deployment id for resource agent " + this.getLocalName() + " is [" + websocketDeploymentId + "]");
            } else {
                logger.warn(getClass().getName() + " - WebSocket undeployment for resource agent " + this.getLocalName() + " FAILED");
            }
        }
        );
        
        logger.debug(getClass().getName() + " - " + this.getLocalName() + " - takeDown ended");
    }

    // VALID FOR RESOURCE AGENTS
    @Override
//    public void msgTypeLifeBeat(String msg) {
    public void msgTypeLifeBeat(RawEquipmentData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type lifebeat = [" + msg + "]");
        addBehaviour(new MissingLifeBeatInitiator(this, MissingLifeBeatInitiator.buildMessage(this)));
    }

    // VALID FOR RESOURCE AGENTS
    @Override
//    public void msgTypeExtractedData(String msg) {
    public void msgTypeExtractedData(RawEquipmentData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type extracteddata = [" + msg + "]");
        addBehaviour(new ExtractedDataInitiator(this, ExtractedDataInitiator.buildMessage(this, msg)));
    }

    /**
     * WP4 semantic model alignment
     * New product location (it covers the inotec tracker)
     * Message for product agents.
     */
    @Override
    public void msgTypeNewLocation(String msg) {
        String message = "Method msgTypeNewLocation not suitable for resource agents";
        logger.error(message);
    }

    // Product Agent to which recipes were applied needs to come here. -> Product AID
    // VALID FOR RESOURCE AGENTS
    @Override
//    public void msgTypeAppliedRecipes(String msg) {
    public void msgTypeAppliedRecipes(RecipeExecutionData msg) {
        String productAgentName = "this needs to come as part of msg!!!!!";
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type applied recipes = [" + msg + "]");
        addBehaviour(new AppliedRecipeInitiator(this, AppliedRecipeInitiator.buildMessage(this, msg, productAgentName)));
    }

    /**
     * WP4 semantic model alignment
     * Workstation got different product than what expected
     * Message for resource agents.
     */
    // VALID FOR RESOURCE AGENTS
    @Override
//    public void msgTypeUnexpectedProduct(String msg) {
    public void msgTypeUnexpectedProduct(UnexpectedProductData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type unexpected product = [" + msg + "]");
        addBehaviour(new UnexpectedProductInitiator(this, UnexpectedProductInitiator.buildMessage(this, msg)));
    }

    // VALID FOR RESOURCE AGENTS
    @Override
//    public void msgTypeProductArrived(String msg) {
    public void msgTypeProductArrived(UnexpectedProductData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type unexpected product = [" + msg + "]");
        addBehaviour(new UnexpectedProductInitiator(this, UnexpectedProductInitiator.buildMessage(this, msg)));
    }

    /**
     * For resource and transport agents.
     * @param msg 
     */
    // VALID FOR RESOURCE AGENTS
    @Override
//    public void msgTypeProductLeavingWorkstationOrTransport(String msg) {
    public void msgTypeProductLeavingWorkstationOrTransport(ProductLeavingWorkstationOrTransportData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type product leaving workstation = [" + msg + "]");
        addBehaviour(new ProductLeavingWorkstationOrTransportInitiator(this, ProductLeavingWorkstationOrTransportInitiator.buildMessage(this, msg)));
    }

    // VALID FOR RESOURCE AGENTS
    @Override
//    public void msgTypeRecipeExecutionData(String msg) {
    public void msgTypeRecipeExecutionData(RecipeExecutionData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type recipe execution data = [" + msg + "]");
        addBehaviour(new RecipeExecutionInitiator(this, RecipeExecutionInitiator.buildMessage(this, msg)));
    }

    // VALID FOR RESOURCE AGENTS
    @Override    
//    public void msgTypeWorkstationUpdate(String msg) {
    public void msgTypeWorkstationUpdate(SubSystem msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type workstation update = [" + msg + "]");
        
        addBehaviour(new WorkstationUpdateInitiator(this, WorkstationUpdateInitiator.buildMessage(this, msg)));

        // VaG - 11/12/2017
        // After updating database we update internal memory object.
        ((CyberPhysicalAgent) this).setCpad(msg);        
    }

    @Override
    public void msgTypeExecutionTableUpdate(ExecutionTable msg) {
        logger.debug("et before socket update: " + this.getCpad().getExecutionTable().toJSON2());        
        this.getCpad().setExecutionTable(msg);
        logger.debug("et after socket update: " + this.getCpad().getExecutionTable().toJSON2());
        
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type execution table update = [" + msg + "]");
        addBehaviour(new ExecutionTableUpdateInitiator(this, ExecutionTableUpdateInitiator.buildMessage(this, msg)));
    }
}
