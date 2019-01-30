package eu.openmos.agentcloud.transportagent;

import eu.openmos.agentcloud.agent.initiators.ExecutionTableUpdateInitiator;
import eu.openmos.agentcloud.agent.initiators.NewLocationInitiator;
import eu.openmos.agentcloud.agent.initiators.ProductLeavingWorkstationOrTransportInitiator;
import eu.openmos.agentcloud.agent.initiators.RecipeExecutionInitiator;
import eu.openmos.agentcloud.agent.initiators.WorkstationUpdateInitiator;
import eu.openmos.agentcloud.agent.responders.CPAExecutionTableUpdateResponder;
import eu.openmos.agentcloud.agent.responders.SubSystemRecipeUpdateResponder;
import eu.openmos.agentcloud.agent.responders.SubSystemRecipesListUpdateResponder;
import eu.openmos.agentcloud.agent.responders.WorkstationInternalUpdateResponder;
import eu.openmos.model.Recipe;
import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.model.LogicalLocation;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.agentcloud.cyberphysicalagent.WebSocketMessageHandler;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.agentcloud.cyberphysicalagent.WebSocketReceiver;
import eu.openmos.model.ExecutionTable;
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
 */
public class TransportAgent extends CyberPhysicalAgent implements WebSocketMessageHandler {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(TransportAgent.class.getName());
    /**
     * Deployment web socket ID.
     */
    protected String websocketDeploymentId;
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
     * Constructor inherited from the Cyber Physical Agent class.
     * 
     * @param locationPhysical - Physical Location of the product.
     * @param locationLogical - Logical location of the product.
     * @param cpad - Description of the product.
     */
//    public TransportAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, CyberPhysicalAgentDescription cpad) {
    public TransportAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, SubSystem cpad) {
        super(locationPhysical, locationLogical, cpad);
    }
     
    /**
     * Setup method for the Transport Agent. Registers the agent in the DF.
     * Launches the behaviours that will allow the agent to accept new 
     * recipes from the optimizer and to register in the optimizer.
     * TO DO: Valerio needs to explain web sockets.
     */
    @Override
    protected void setup() {
        super.setup();
        logger.debug(getClass().getName() + " - Transport Agent " + getLocalName() + " launched.");

        DFInteraction.RegisterInDF(this, this.getLocalName(), Constants.DF_TRANSPORT);      
                
        activeRecipes.addAll(getCpad().getRecipes());
        deployedRecipes.addAll(getCpad().getRecipes());
        
        // addBehaviour(new NewTransportInitiator(this, NewTransportInitiator.BuildMessage(this, getCpad().toString())));
        addBehaviour(new NewTransportInitiator(this, NewTransportInitiator.buildMessage(this, getCpad())));

        addBehaviour(new DeployOptimizedTransportRecipesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_RECIPES))));
        addBehaviour(new DeploySuggestedTransportRecipesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_SUGGEST_RECIPES))));
        addBehaviour(new SOPOptimizedTransportRecipesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_DEPLOY_SUGGEST_RECIPES))));

        addBehaviour(new CPAExecutionTableUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_START_EXECUTION_TABLE_UPDATE))));
        addBehaviour(new SubSystemRecipesListUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_START_SUBSYSTEM_RECIPES_LIST_UPDATE))));
        addBehaviour(new SubSystemRecipeUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_START_SUBSYSTEM_RECIPE_UPDATE))));
        // VaG - 21/10/2016
        // MessageTrackerWSClient.track("RA", "constructor", "New transport agent " + this.getLocalName() + " coming up");

        addBehaviour(new WorkstationInternalUpdateResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
                MessageTemplate.MatchOntology(Constants.ONTO_WORKSTATION_INTERNAL_UPDATE))));
        
        logger.debug(getClass().getName() + " - about to start websocket communication for transport agent " + this.getLocalName());
        Vertx.vertx().deployVerticle(
            new WebSocketReceiver(this.getLocalName(), (WebSocketMessageHandler)this), res -> 
            {
                if (res.succeeded()) {
                    websocketDeploymentId = res.result();
                    logger.info(getClass().getName() + " - WebSocket deployment succedeed! Deployment id for transport agent " + this.getLocalName() + " is [" + websocketDeploymentId + "]");
                } else
                    logger.warn(getClass().getName() + " - WebSocket deployment for transport agent " + this.getLocalName() + " FAILED");
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
                logger.info(getClass().getName() + " - WebSocket undeployment succedeed! Deployment id for transport agent " + this.getLocalName() + " is [" + websocketDeploymentId + "]");
            } else {
                logger.warn(getClass().getName() + " - WebSocket undeployment for transport agent " + this.getLocalName() + " FAILED");
            }
        }
        );
        
        logger.debug(getClass().getName() + " - " + this.getLocalName() + " - takeDown ended");
    }

    // VALID FOR TRANSPORT AGENTS
    @Override
//    public void msgTypeLifeBeat(String msg) {
    public void msgTypeLifeBeat(RawEquipmentData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type lifebeat = [" + msg + "]");
        addBehaviour(new MissingTransportLifeBeatInitiator(this, MissingTransportLifeBeatInitiator.buildMessage(this)));
    }

    // VALID FOR TRANSPORT AGENTS
    @Override
//    public void msgTypeExtractedData(String msg) {
    public void msgTypeExtractedData(RawEquipmentData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type extracteddata = [" + msg + "]");
        addBehaviour(
                new ExtractedTransportDataInitiator(
                        this, ExtractedTransportDataInitiator.buildMessage(this, msg)
                )
        );
    }

    @Override
    // NO, UNSUPPORTED FOR TRANSPORT
    public void msgTypeNewLocation(String msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type new location = [" + msg + "]");
        addBehaviour(new NewLocationInitiator(this, NewLocationInitiator.buildMessage(this, msg)));
    }

    // VALID FOR TRANSPORT AGENTS
    @Override
//    public void msgTypeAppliedRecipes(String msg) {
    public void msgTypeAppliedRecipes(RecipeExecutionData msg) {
        String message = "Method msgTypeAppliedRecipes not suitable for transport agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    // VALID FOR TRANSPORT AGENTS
    @Override
//    public void msgTypeUnexpectedProduct(String msg) {
    public void msgTypeUnexpectedProduct(UnexpectedProductData msg) {
        String message = "Method msgTypeUnexpectedProduct not suitable for transport agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    @Override
    // VALID FOR TRANSPORT AGENTS
//    public void msgTypeProductArrived(String msg) {
    public void msgTypeProductArrived(UnexpectedProductData msg) {
        String message = "Method msgTypeProductArrived not suitable for transport agents";
        logger.error(message);
        // throw new UnsupportedOperationException(message);
    }

    /**
     * For resource and transport agents.
     * @param msg 
     */
    // VALID FOR TRANSPORT AGENTS
    @Override
//    public void msgTypeProductLeavingWorkstationOrTransport(String msg) {
    public void msgTypeProductLeavingWorkstationOrTransport(ProductLeavingWorkstationOrTransportData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type product leaving transport = [" + msg + "]");
        addBehaviour(new ProductLeavingWorkstationOrTransportInitiator(this, ProductLeavingWorkstationOrTransportInitiator.buildMessage(this, msg)));
    }

    // VALID FOR TRANSPORT AGENTS
    @Override
//    public void msgTypeRecipeExecutionData(String msg) {
    public void msgTypeRecipeExecutionData(RecipeExecutionData msg) {
        logger.debug(getClass().getName() + " - " + getLocalName() + " - msg of type recipe execution data = [" + msg + "]");
        addBehaviour(new RecipeExecutionInitiator(this, RecipeExecutionInitiator.buildMessage(this, msg)));
    }

    // VALID FOR TRANSPORT AGENTS
    @Override
//    public void msgTypeWorkstationUpdate(String msg) {
    public void msgTypeWorkstationUpdate(SubSystem msg) {
//        String message = "Method msgTypeWorkstationUpdate not suitable for transport agents";
//        logger.error(message);
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
