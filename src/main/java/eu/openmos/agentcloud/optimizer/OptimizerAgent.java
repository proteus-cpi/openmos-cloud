package eu.openmos.agentcloud.optimizer;

import eu.openmos.agentcloud.agent.initiators.WorkstationInternalStageUpdateInitiator;
import eu.openmos.agentcloud.agent.initiators.WorkstationInternalUpdateInitiator;
import eu.openmos.agentcloud.utilities.OptimizationParameter;
import eu.openmos.agentcloud.toolagent.ToolAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.OrderInstance;
import eu.openmos.agentcloud.agent.responders.*;
import eu.openmos.agentcloud.productagent.NewLocationResponder;
import eu.openmos.agentcloud.productagent.ProductInstanceUpdateResponder;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.openmos.model.*;
import jade.core.AID;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

/**
 * The OptimizerAgent class is an abstract class that can be extended to implement customizes optimizers
 * This class provides an implementation for the OptimizerAgentInterface that contains the main methods for controlling a generic optimization algorithm.
 * The class also provides the main behaviours for registering and unregistering data from new agents and order being launched in the platform as well as missed heartbeat signals.
 * These behaviours handle the referred information by directing it to an optimization algorithm that needs to be set before the agent is launched in the platform by means of the appropriate methods or the constructor.
 * 
 * @author Luis Ribeiro<luis.ribeiro@liu.se>
 */
public abstract class OptimizerAgent extends ToolAgent implements OptimizerAgentInterface {
    Logger logger = LoggerFactory.getLogger(OptimizerAgent.class);

    /**
     * OPT_UN_INITIALIZED is a constant that defines the status of the Optimizer Agent as not initialized
     */
    protected static final int OPT_UN_INITIALIZED = 0;

    /**
     * OPT_INITIALIZED is a constant that defines the status of the Optimizer Agent as initialized
     */
    protected static final int OPT_INITIALIZED = 1;

    /**
     * OPT_READY is a constant that defines the status of the Optimizer Agent as ready to operate
     */
    protected static final int OPT_READY = 2;

    /**
     * OPT_STOP is a constant that defines the transient status of the Optimizer Agent as coming into a stop
     */
    protected static final int OPT_STOP = 3;

    /**
     * OPT_STOPPED is a constant that defines the status of the Optimizer Agent as stopped
     */
    protected static final int OPT_STOPPED = 4;

    /**
     * OPT_RESET is a constant that defines the transient status of the Optimizer Agent as coming into a reset
     */
    protected static final int OPT_RESET = 5;

    /**
     * OPT_RESETTED is a constant that defines the status of the Optimizer Agent as reseted
     */
    protected static final int OPT_RESETTED = 6;

    /**
     * OPT_REPARAMETRIZE is a constant that defines the transient status of the Optimizer Agent as being re-parametrized
     */
    protected static final int OPT_REPARAMETRIZE = 7;

    /**
     * OPT_REPARAMETRIZED is a constant that defines the status of the Optimizer Agent as re-parametrized
     */
    protected static final int OPT_REPARAMETRIZED = 8;

    /**
     * OPT_REPARAMETRIZED is a constant that defines the status of the Optimizer Agent as in failure
     */
    protected static final int OPT_FAILURE = 9;

    /**
     * OPT_REPARAMETRIZE is a constant that defines the transient status of the Optimizer Agent as performing an optimization
     */
    protected static final int OPT_OPTIMIZING = 10;

    /**
     * The variable status takes as value one of the state defining constants
     */
    protected int status;
    // protected OptimizationAlgorithm optimizationAlgorithm;

    /**
     *
     */
    protected PluggableOptimizerInterface optimizationAlgorithm;
          

    /**
     * The setOptimizationAlgorithm method enables an external class to set the custom optimization algorithm to be managed by the optimizer agent implementation
     *  
     * @param optimizationAlgorithm is the optimization algorithm implementation to be managed by this optimizer agent
     */
    public void setOptimizationAlgorithm(PluggableOptimizerInterface optimizationAlgorithm) {
        if (optimizationAlgorithm == null) {
            logger.warn("OptimizerAgent.setOptimizationAlgorithm -> optimizationAlgorithm is null!");
        }

        this.optimizationAlgorithm = optimizationAlgorithm;                   
        this.status = OPT_UN_INITIALIZED;
    }

    
    /**
     * The class constructor
     * 
     * @param optimizationAlgorithm is the optimization algorithm implementation to be managed by this optimizer agent
     */
    public OptimizerAgent(PluggableOptimizerInterface optimizationAlgorithm) {        
        if (optimizationAlgorithm == null) {
            logger.warn("OptimizerAgent.OptimizerAgent -> optimizationAlgorithm is null!");
        }else{
            this.optimizationAlgorithm = optimizationAlgorithm;
        }
    }  

    /**
     * The initializeOptimizer method calls the corresponding function  on the optimization algorithm and sets the optimizer agent status to {@link OptimizerAgent#OPT_INITIALIZED }
     * 
     * @return true if the initialization procedure succeeded, false otherwise
     */
    public boolean initializeOptimizer() {
        if (optimizationAlgorithm == null) {
            logger.warn("OptimizerAgent.initializeOptimizer -> optimizationAlgorithm is null!");
            return false;
        }
        if (optimizationAlgorithm.initializeOptimizer()) {
            status = OPT_INITIALIZED;
            return true;
        }
        return false;
    }

    /**
     * The stopOptimizer method calls the corresponding function on the optimization algorithm and sets the optimizer agent status to {@link OptimizerAgent#OPT_STOPPED }
     * 
     * @return true if the stop procedure succeeded, false otherwise
     */
    public boolean stopOptimizer() {
        if (optimizationAlgorithm == null) {
            logger.warn("OptimizerAgent.stopOptimizer -> optimizationAlgorithm is null!");
            return false;
        }
        if (optimizationAlgorithm.stopOptimizer()) {
            status = OPT_STOPPED;
            return true;
        }
        return false;
    }

    /**
     * The resetOptimizer method calls the corresponding function on the optimization algorithm and sets the optimizer agent status to {@link OptimizerAgent#OPT_RESETTED }
     * 
     * @return true if the reset procedure succeeded, false otherwise
     */
    public boolean resetOptimizer() {
        if (optimizationAlgorithm == null) {
            logger.warn("OptimizerAgent.resetOptimizer -> optimizationAlgorithm is null!");
            return false;
        }
        if (optimizationAlgorithm.resetOptimizer()) {
            status = OPT_RESETTED;
            return true;
        }
        return false;
    }

    /**
     * The reparametrizeOptimizer method calls the corresponding function on the optimization algorithm and sets the optimizer agent status to {@link OptimizerAgent#OPT_REPARAMETRIZED }
     * 
     * @param newParameters are the new parameters that should be considered by the optimization algorithm to perform the optimization (note that these are the parameters of the algorithm itself and not information coming from the system)
     * @return true if the re-parametrization procedure succeeded, false otherwise
     */
    public boolean reparametrizeOptimizer(List<OptimizationParameter> newParameters) {
        if (optimizationAlgorithm == null) {
            logger.warn("OptimizerAgent.reparametrizeOptimizer -> optimizationAlgorithm is null!");
            return false;
        }
        if (optimizationAlgorithm.reparametrizeOptimizer(newParameters)) {
            status = OPT_REPARAMETRIZED;
            return true;
        }
        return false;
    }

    /**
     * The isOptimizable method calls the corresponding function on the optimization algorithm
     * 
     * @return true if the system can be optimized or false if the system cannot be optimized at the moment.
     */
    public boolean isOptimizable() {
        if (optimizationAlgorithm == null) {
            logger.warn("OptimizerAgent.isOptimizable -> optimizationAlgorithm is null!");
            return false;
        }
        return optimizationAlgorithm.isOptimizable();
    }

    @Override
    protected void setup() {
        super.setup();
        
        logger.info("Optimizer Agent {} setting up... Launching behaviour to accept new CPA registration.", this.getName());
        addBehaviour(new RegisterCyberPhysicalAgentData(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_REGISTER_CPA_IN_OPTIMIZER))));
        addBehaviour(new ProcessMissingHeartBeat(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_MISSED_LB))));
        addBehaviour(new ProcessAgentPerformanceDataUpdate(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_UPDATE_CPA_PERFORMANCE_DATA))));
        addBehaviour(new RemoveCyberPhysicalAgentData(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.REMOVE_AGENT_FROM_OPTIMIZER),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ProcessNewlyDeployedOrder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEWLY_DEPLOYED_ORDER),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        
        /**
        * WP4 Cloud Platform Re-work related code.
        * 
        * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
        * @author Valerio Gentile <valerio.gentile@we-plus.eu>
        */
        addBehaviour(new ProcessNewRecipe(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_PROCESS_NEW_RECIPE_ON_THE_SYSTEM))));
        addBehaviour(new ProcessNewSkill(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_PROCESS_NEW_SKILL_ON_THE_SYSTEM))));
        addBehaviour(new ProcessFinishedProductResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_FINISHED_PRODUCT))));
        addBehaviour(new ProcessOrderRemovalResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_ORDER_REMOVAL))));
        addBehaviour(new ProcessUpdatedOrder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_UPDATED_ORDER),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        
//        addBehaviour(new UnexpectedProductResponder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_UNEXPECTED_PRODUCT),
//                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new UnexpectedProductResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_UNEXPECTED_PRODUCT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ProductLeavingWorkstationOrTransportResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
//        addBehaviour(new NewLocationResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_PHYSICAL_LOCATION),
//                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new NewLocationResponder(this, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_PHYSICAL_LOCATION),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new RecipeExecutionResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_RECIPE_EXECUTION),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new WorkstationUpdateResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_WORKSTATION_UPDATE),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));

        addBehaviour(new NewEquipmentObservationResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListEquipmentObservationsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATIONS),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        
        addBehaviour(new NewTriggeredSkillResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_TRIGGERED_SKILL),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new NewTriggeredRecipeResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_TRIGGERED_RECIPE),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListTriggeredSkillsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_TRIGGERED_SKILLS),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListTriggeredRecipesResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_TRIGGERED_RECIPES),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));

        addBehaviour(new NewEquipmentObservationRel2Responder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION_REL2),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListEquipmentObservationRel2sResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListEquipmentObservationRel2sResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListEquipmentObservationRel2sResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_SYSTEM),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new GetEquipmentObservationRel2Responder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_EQUIPMENT_OBSERVATION_REL2))));

        addBehaviour(new NewEquipmentAssessmentResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_EQUIPMENT_ASSESSMENT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListEquipmentAssessmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListEquipmentAssessmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListEquipmentAssessmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_SYSTEM),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new GetEquipmentAssessmentResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_EQUIPMENT_ASSESSMENT))));
        
        addBehaviour(new IsSystemRampUpChangeStagePossibleResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_IS_SYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new IsSubSystemRampUpChangeStagePossibleResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_IS_SUBSYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));

        addBehaviour(new NewPhysicalAdjustmentResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_PHYSICAL_ADJUSTMENT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListPhysicalAdjustmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListPhysicalAdjustmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new GetPhysicalAdjustmentResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_PHYSICAL_ADJUSTMENT))));

        addBehaviour(new NewProcessAssessmentResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_NEW_PROCESS_ASSESSMENT),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListProcessAssessmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_PROCESS_ASSESSMENTS),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListProcessAssessmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListProcessAssessmentsResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new GetProcessAssessmentResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_PROCESS_ASSESSMENT))));

        addBehaviour(new GetRecipesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_RECIPES))));
        addBehaviour(new GetRecipesBySubSystemIdResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_RECIPES_BY_SUBSYSTEM_ID))));        
        addBehaviour(new PutRecipesResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_PUT_RECIPES))));
        addBehaviour(new GetSubSystemsResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_SUBSYSTEMS))));
        addBehaviour(new GetExecutionTablesResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_EXECUTION_TABLES))));
        addBehaviour(new GetExecutionTableByIdResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_EXECUTION_TABLE_BY_ID))));
        addBehaviour(new GetExecutionTableBySubSystemIdResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_EXECUTION_TABLE_BY_SUBSYSTEM_ID))));

        addBehaviour(new OptimizerExecutionTableUpdateResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_UPDATE_EXECUTION_TABLE))));
        addBehaviour(new WorkstationRecipesUpdateResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_WORKSTATION_RECIPES_UPDATE),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));

        addBehaviour(new NewProductDefinitionResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_NEW_PRODUCT_DEFINITION))));
        addBehaviour(new ProcessStartedProductResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_STARTED_PRODUCT))));
        
        addBehaviour(new GetRecipeExecutionDataResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA))));
        addBehaviour(new GetRecipeExecutionDataResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE))));
        addBehaviour(new GetRecipeExecutionDataResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE))));
        addBehaviour(new GetRecipeExecutionDataResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE))));
                
        addBehaviour(new GetSkillResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_SKILL))));
        addBehaviour(new GetProductResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_PRODUCT))));

        addBehaviour(new GetOrderInstanceResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_GET_ORDER_INSTANCE))));
        addBehaviour(new ListOrderInstancesResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_ORDER_INSTANCES),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
        addBehaviour(new ListProductInstancesNotYetProducedResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchOntology(Constants.ONTO_LIST_PRODUCT_INSTANCES_NOT_YET_PRODUCED),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));

        addBehaviour(new NewSystemStageChangeResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_NEW_SYSTEM_STAGE_CHANGE))));
        addBehaviour(new NewSubSystemStageChangeResponder(this, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_NEW_SUBSYSTEM_STAGE_CHANGE))));

        addBehaviour(new ProductInstanceUpdateResponder(this, optimizationAlgorithm, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchOntology(Constants.ONTO_PRODUCT_INSTANCE_UPDATE))));
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        
        optimizationAlgorithm.stopOptimizer();
    }

    private final class ProcessAgentPerformanceDataUpdate extends AchieveREResponder {

        public ProcessAgentPerformanceDataUpdate(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            try {
                //            optimizationAlgorithm.newDataAvailable(request.getContent());
                optimizationAlgorithm.newDataAvailableForEquipment((RawEquipmentData)request.getContentObject());
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }

    }

    private final class ProcessMissingHeartBeat extends AchieveREResponder {

        public ProcessMissingHeartBeat(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            optimizationAlgorithm.newAgentWithMissingHB(request.getSender().getName());
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }
    }
    
    private final class ProcessNewlyDeployedOrder extends AchieveREResponder {

        public ProcessNewlyDeployedOrder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            // optimizationAlgorithm.newOrderInPlatform(Order.fromString(request.getContent()));
            boolean status = false;
            
            logger.debug(getClass().getName() + " - order into the optimizer: " + request.getContent());
            OrderInstance order;
            try {
                order = (OrderInstance)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            status = optimizationAlgorithm.newOrderInstanceInPlatform(order);

//            try
//            {
//                logger.debug(getClass().getName() + " - order into the optimizer: " + request.getContent());
//                Order order = Order.fromString(request.getContent());
//                status = optimizationAlgorithm.newOrderInPlatform(order);
//            }
//            catch (ParseException pe)
//            {
//                throw new RefuseException(pe.getMessage());
//            }
            
            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
            ACLMessage reply = request.createReply();
            
            if (status)
            {
                reply.setPerformative(ACLMessage.INFORM);
            }
            else
            {
                reply.setContent("OptimizerAgent - ProcessNewlyDeployedOrder - Database operation failed. Please check log file for further details.");
                reply.setPerformative(ACLMessage.FAILURE);                
            }
            return reply;
        }
    }    
        
    /**
     * Responder for unexpected product messages arriving via socket.
     * Moved into external class.
     */
//    private final class UnexpectedProductResponder extends AchieveREResponder {
//
//        public UnexpectedProductResponder(Agent a, MessageTemplate mt) {
//            super(a, mt);
//        }
//
//        @Override
//        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//            UnexpectedProductData unexpectedProductData;
//            try {
//                unexpectedProductData = UnexpectedProductData.fromString(request.getContent());
//            } catch (ParseException ex) {
//                throw new RefuseException(ex.getMessage());
//            }
//            optimizationAlgorithm.trackUnexpectedProductData(unexpectedProductData);
//            
//            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
//            ACLMessage reply = request.createReply();
//            reply.setPerformative(ACLMessage.INFORM);
//            return reply;
//        }
//    }

    private final class RegisterCyberPhysicalAgentData extends AchieveREResponder {

        public RegisterCyberPhysicalAgentData(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//            CyberPhysicalAgentDescription cpad;
//            try
//            {
//                cpad = CyberPhysicalAgentDescription.fromString(request.getContent());
//            }
//            catch (ParseException pe)
//            {
//                throw new RefuseException(pe.getMessage());
//            }
            SubSystem cpad;
            try {
                //////////////            cpad = SubSystem.deserialize(request.getContent().getBytes());
                cpad = (SubSystem)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            
            ACLMessage reply = request.createReply();                       
            if (cpad.isValid()) {               
                optimizationAlgorithm.newAgentInPlatform(cpad);
                reply.setContent("Agent Registered Successfully");
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Agent data is invalid");
                reply.setPerformative(ACLMessage.FAILURE);
                }
            return reply;
        }

    }
    
    private final class RemoveCyberPhysicalAgentData extends AchieveREResponder {

        public RemoveCyberPhysicalAgentData(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//            CyberPhysicalAgentDescription cpad;
//            try
//            {
//                cpad = CyberPhysicalAgentDescription.fromString(request.getContent());
//            }
//            catch (ParseException pe)
//            {
//                throw new RefuseException(pe.getMessage());
//            }
//
            ACLMessage reply = request.createReply();
                       
//            if (cpad.isValid()) {                
//                optimizationAlgorithm.agentRemovedFromPlatform(request.getSender().getName());
//                reply.setContent("Agent Registered Successfully");
//                reply.setPerformative(ACLMessage.INFORM);
//            } else {
//                reply.setContent("Agent data is invalid");
//                reply.setPerformative(ACLMessage.FAILURE);
//            }
            optimizationAlgorithm.agentRemovedFromPlatform(request.getSender().getName());
            reply.setContent("Agent deregistered successfully");
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }

    }

    /**
     * WP4 Cloud Platform Re-work related code.
     * 
     * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
     * @author Valerio Gentile <valerio.gentile@we-plus.eu>
     */
    
    private final class ProcessNewRecipe extends AchieveREResponder {

        public ProcessNewRecipe(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            Recipe recipe;
            try {
                recipe = (Recipe)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }

//            try
//            {
//                recipe = Recipe.fromString(request.getContent());
//            }
//            catch (ParseException pe)
//            {
//                throw new RefuseException(pe.getMessage());
//            }

            ACLMessage reply = request.createReply();
                       
            if(optimizationAlgorithm.processNewRecipe(recipe))  {      
                reply.setContent("Recipe acknowledged.");
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Recipe could not be stored.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class ProcessNewSkill extends AchieveREResponder {

        public ProcessNewSkill(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            Skill skill;
            try {
                skill = (Skill)request.getContentObject();
//            try
//            {
//                skill = Skill.fromString(request.getContent());
//            }
//            catch (ParseException pe)
//            {
//                throw new RefuseException(pe.getMessage());
//            }
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }

            ACLMessage reply = request.createReply();
                       
            if(optimizationAlgorithm.processNewSkill(skill))  {      
                reply.setContent("Skill acknowledged.");
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Skill could not be stored.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetRecipesResponder extends AchieveREResponder {

        public GetRecipesResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            List<Recipe> recipesList = optimizationAlgorithm.getRecipes();
            
            logger.debug("recipes selected from the optimizationAlgorithm = [" + recipesList + "]");

            // list is not serializable, so need conversion
            ArrayList<Recipe> recipes = new ArrayList(recipesList);
            
            ACLMessage reply = request.createReply();
                       
            if (recipes != null )  {      
                try {
                    reply.setContentObject(recipes);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Recipes could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetSkillResponder extends AchieveREResponder {

        public GetSkillResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String skillId = (String)request.getContent();
            Skill skill = optimizationAlgorithm.getSkill(skillId);
            
            logger.debug("skill selected from the optimizationAlgorithm = [" + skill + "]");

            ACLMessage reply = request.createReply();
                       
            if (skill != null )  {      
                try {
                    reply.setContentObject(skill);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Skill with id " + skillId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetEquipmentObservationRel2Responder extends AchieveREResponder {

        public GetEquipmentObservationRel2Responder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String equipmentObservationRel2Id = (String)request.getContent();
            EquipmentObservationRel2 equipmentObservationRel2 = optimizationAlgorithm.getEquipmentObservationRel2(equipmentObservationRel2Id);
            
            logger.debug("equipmentObservationRel2 selected from the optimizationAlgorithm = [" + equipmentObservationRel2 + "]");

            ACLMessage reply = request.createReply();
                       
            if (equipmentObservationRel2 != null )  {      
                try {
                    reply.setContentObject(equipmentObservationRel2);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("equipmentObservationRel2 with id " + equipmentObservationRel2Id + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetEquipmentAssessmentResponder extends AchieveREResponder {

        public GetEquipmentAssessmentResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String equipmentAssessmentId = (String)request.getContent();
            EquipmentAssessment equipmentAssessment = optimizationAlgorithm.getEquipmentAssessment(equipmentAssessmentId);
            
            logger.debug("equipmentAssessment selected from the optimizationAlgorithm = [" + equipmentAssessment + "]");

            ACLMessage reply = request.createReply();
                       
            if (equipmentAssessment != null )  {      
                try {
                    reply.setContentObject(equipmentAssessment);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("equipmentAssessment with id " + equipmentAssessmentId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetProcessAssessmentResponder extends AchieveREResponder {

        public GetProcessAssessmentResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String processAssessmentId = (String)request.getContent();
            ProcessAssessment processAssessment = optimizationAlgorithm.getProcessAssessment(processAssessmentId);
            
            logger.debug("processAssessment selected from the optimizationAlgorithm = [" + processAssessment + "]");

            ACLMessage reply = request.createReply();
                       
            if (processAssessment != null )  {      
                try {
                    reply.setContentObject(processAssessment);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("processAssessment with id " + processAssessmentId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetPhysicalAdjustmentResponder extends AchieveREResponder {

        public GetPhysicalAdjustmentResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String physicalAdjustmentId = (String)request.getContent();
            PhysicalAdjustment physicalAdjustment = optimizationAlgorithm.getPhysicalAdjustment(physicalAdjustmentId);
            
            logger.debug("physicalAdjustment selected from the optimizationAlgorithm = [" + physicalAdjustment + "]");

            ACLMessage reply = request.createReply();
                       
            if (physicalAdjustment != null )  {      
                try {
                    reply.setContentObject(physicalAdjustment);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("physicalAdjustment with id " + physicalAdjustmentId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetProductResponder extends AchieveREResponder {

        public GetProductResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String productId = (String)request.getContent();
            Product product = optimizationAlgorithm.getProduct(productId);
            
            logger.debug("product selected from the optimizationAlgorithm = [" + product + "]");

            ACLMessage reply = request.createReply();
                       
            if (product != null )  {      
                try {
                    reply.setContentObject(product);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Product with id " + productId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetOrderInstanceResponder extends AchieveREResponder {

        public GetOrderInstanceResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String orderInstanceId = (String)request.getContent();
            OrderInstance orderInstance = optimizationAlgorithm.getOrderInstance(orderInstanceId);
            
            logger.debug("orderInstance selected from the optimizationAlgorithm = [" + orderInstance + "]");

            ACLMessage reply = request.createReply();
                       
            if (orderInstance != null )  {      
                try {
                    reply.setContentObject(orderInstance);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Order instance with id " + orderInstanceId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetRecipesBySubSystemIdResponder extends AchieveREResponder {

        public GetRecipesBySubSystemIdResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String subSystemId = (String)request.getContent();
            List<Recipe> recipesList = optimizationAlgorithm.getRecipesBySubSystemId(subSystemId);
            
            logger.debug("recipes by subsystemid selected from the optimizationAlgorithm = [" + recipesList + "]");

            ACLMessage reply = request.createReply();
                       
//            if (recipes != null )  {      
            if (recipesList != null )  {      
                // list is not serializable, so need conversion
                ArrayList<Recipe> recipes = new ArrayList(recipesList);

                try {
                    reply.setContentObject(recipes);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Recipes could not be retrieved. Maybe wrong subsystem id?");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetSubSystemsResponder extends AchieveREResponder {

        public GetSubSystemsResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            List<SubSystem> subSystemsList = optimizationAlgorithm.getSubSystems();
            
            logger.debug("subsystems selected from the optimizationAlgorithm = [" + subSystemsList + "]");

            // list is not serializable, so need conversion
            ArrayList<SubSystem> subSystems = new ArrayList(subSystemsList);
            
            ACLMessage reply = request.createReply();
                       
            if (subSystems != null )  {      
                try {
                    reply.setContentObject(subSystems);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("SubSystems could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetExecutionTablesResponder extends AchieveREResponder {

        public GetExecutionTablesResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            List<ExecutionTable> executionTablesList = optimizationAlgorithm.getExecutionTables();
            
            logger.debug("execution tables selected from the optimizationAlgorithm = [" + executionTablesList + "]");

            // list is not serializable, so need conversion
            ArrayList<ExecutionTable> executionTables = new ArrayList(executionTablesList);
            
            ACLMessage reply = request.createReply();
                       
            if (executionTables != null )  {      
                try {
                    reply.setContentObject(executionTables);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Execution tables could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetExecutionTableByIdResponder extends AchieveREResponder {

        public GetExecutionTableByIdResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String executionTableId = (String)request.getContent();

            ExecutionTable executionTable = optimizationAlgorithm.getExecutionTableById(executionTableId);
            
            logger.debug("execution table by id selected from the optimizationAlgorithm = [" + executionTable + "]");

            ACLMessage reply = request.createReply();
                       
            if (executionTable != null )  {      
                try {
                    reply.setContentObject(executionTable);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Execution table by id " + executionTableId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class GetExecutionTableBySubSystemIdResponder extends AchieveREResponder {

        public GetExecutionTableBySubSystemIdResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            String subSystemId = (String)request.getContent();

            ExecutionTable executionTable = optimizationAlgorithm.getExecutionTableBySubSystemId(subSystemId);
            
            logger.debug("execution table by subsystem id selected from the optimizationAlgorithm = [" + executionTable + "]");

            ACLMessage reply = request.createReply();
                       
            if (executionTable != null )  {      
                try {
                    reply.setContentObject(executionTable);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Execution table by subsystem id " + subSystemId + " could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;
        }        
        
    }
    
    private final class ProcessFinishedProductResponder extends AchieveREResponder {

        public ProcessFinishedProductResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//            StringTokenizer tokenizer = new StringTokenizer(request.getContent(), "|");
            FinishedProductInfo finishedProductInfo = null;
            try {
                finishedProductInfo = (FinishedProductInfo) request.getContentObject();
            } catch (UnreadableException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            
            ACLMessage reply = request.createReply();
            
//            try {
//                if(optimizationAlgorithm.finishedProduct(tokenizer.nextToken(), tokenizer.nextToken(), new SimpleDateFormat(eu.openmos.model.utilities.SerializationConstants.DATE_REPRESENTATION).parse(tokenizer.nextToken())))  {
                if(optimizationAlgorithm.finishedProduct(finishedProductInfo))  {
                    reply.setContent("Finished product acknowledged.");
                    reply.setPerformative(ACLMessage.INFORM);
                } else {
                    reply.setContent("Finished product could not be stored.");
                    reply.setPerformative(ACLMessage.FAILURE);
                }
//            } catch (ParseException ex) {
//                logger.error(ex.getMessage());
//            }
            return reply;
        }        
        
    }
    
    private final class ProcessStartedProductResponder extends AchieveREResponder {

        public ProcessStartedProductResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            ProductInstance productInstance = null;
            try {
                productInstance = (ProductInstance) request.getContentObject();
            } catch (UnreadableException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            
            ACLMessage reply = request.createReply();
            
            StartedProductInfo startedProductInfo = new StartedProductInfo();
            startedProductInfo.setProductInstanceId(productInstance.getUniqueId());
            startedProductInfo.setStartedTime(new Date());
            startedProductInfo.setRegistered(new Date());
            
                if(optimizationAlgorithm.startedProduct(startedProductInfo))  {
                    reply.setContent("Started product acknowledged.");
                    reply.setPerformative(ACLMessage.INFORM);
                } else {
                    reply.setContent("Started product could not be stored.");
                    reply.setPerformative(ACLMessage.FAILURE);
                }
            return reply;
        }        
        
    }

    private final class NewProductDefinitionResponder extends AchieveREResponder {

        public NewProductDefinitionResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            Product product = null;
            try {
                product = (Product)request.getContentObject();
            } catch (UnreadableException ex) {
                logger.error(ex.getMessage());
            }
            
            ACLMessage reply = request.createReply();
            
                if(optimizationAlgorithm.newProductDefinition(product))  {
                    reply.setContent("New product definition acknowledged.");
                    reply.setPerformative(ACLMessage.INFORM);
                } else {
                    reply.setContent("New product definition  could not be stored.");
                    reply.setPerformative(ACLMessage.FAILURE);
                }

            return reply;
        }        
        
    }
    
    private final class NewSystemStageChangeResponder extends AchieveREResponder {

        public NewSystemStageChangeResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            SystemChangeStage systemChangeState = null;
            try {
                systemChangeState = (SystemChangeStage)request.getContentObject();
            } catch (UnreadableException ex) {
                logger.error(ex.getMessage());
            }
            
            ACLMessage reply = request.createReply();
            
                if(optimizationAlgorithm.newSystemStageChange(systemChangeState))  {
                    reply.setContent("New system status change acknowledged.");
                    reply.setPerformative(ACLMessage.INFORM);
                } else {
                    reply.setContent("New system status change could not be stored.");
                    reply.setPerformative(ACLMessage.FAILURE);
                }

            return reply;
        }        
        
    }
    
    private final class NewSubSystemStageChangeResponder extends AchieveREResponder {

        public NewSubSystemStageChangeResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            SubSystemChangeStage subSystemChangeStage = null;
            try {
                subSystemChangeStage = (SubSystemChangeStage)request.getContentObject();
            } catch (UnreadableException ex) {
                logger.error(ex.getMessage());
            }
            
            ACLMessage reply = request.createReply();
            
                if(optimizationAlgorithm.newSubSystemStageChange(subSystemChangeStage))  {

                    /********************/                    
                    
                    // VaG - 21/02/2018
                    // After updating database we update internal STATUS field of memory object.
                    AID agentToBeUpdated = new AID(subSystemChangeStage.getSubSystemId(), false);
                    ACLMessage updateMessage = WorkstationInternalStageUpdateInitiator.buildMessage(agentToBeUpdated, subSystemChangeStage);
                    myAgent.addBehaviour(new WorkstationInternalStageUpdateInitiator(myAgent, updateMessage));
            
                    logger.debug(getClass().getName() + " - handleRequest");
                    reply.setContent("New subsystem status change acknowledged.");
                    reply.setPerformative(ACLMessage.INFORM);
                    
                    /**********************/
                    
                } else {
                    reply.setContent("New subsystem status change could not be stored.");
                    reply.setPerformative(ACLMessage.FAILURE);
                }

            return reply;
        }        
        
    }
    
    private final class ProcessOrderRemovalResponder extends AchieveREResponder {

        public ProcessOrderRemovalResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            StringTokenizer tokenizer = new StringTokenizer(request.getContent(), "|");
            
            ACLMessage reply = request.createReply();
                       
            try {
                if(optimizationAlgorithm.removeOrderInstance(tokenizer.nextToken(), new SimpleDateFormat(eu.openmos.model.utilities.SerializationConstants.DATE_REPRESENTATION).parse(tokenizer.nextToken())))  {
                    reply.setContent("Order removal acknowledged.");
                    reply.setPerformative(ACLMessage.INFORM);
                } else {
                    reply.setContent("Order removal could not be stored.");
                    reply.setPerformative(ACLMessage.FAILURE);
                }
            } catch (ParseException ex) {
                logger.error(ex.getMessage());
            }
            return reply;
        }        
        
    }
    
    private final class ProcessUpdatedOrder extends AchieveREResponder {

        public ProcessUpdatedOrder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//                Order order = Order.fromString(request.getContent());
                OrderInstance order;
            try {
                order = (OrderInstance)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
                optimizationAlgorithm.updateOrderInstance(order);
            
            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }
    }

    private final class GetRecipeExecutionDataResponder extends AchieveREResponder 
    {

        public GetRecipeExecutionDataResponder(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            if (request.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA))
                return manage_ONTO_GET_RECIPEEXECUTIONDATA(request);
            else if (request.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE))
                return manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE(request);
            else if (request.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE))
                return manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE(request);
            else if (request.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE))
                return manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE(request);
            else 
                return null;    // must not happen! to check
            }
        }        

        private ACLMessage manage_ONTO_GET_RECIPEEXECUTIONDATA(ACLMessage request)
                 throws NotUnderstoodException, RefuseException
        {
            RecipeExecutionDataFilter myFilter = null;
            try {
                myFilter = (RecipeExecutionDataFilter) request.getContentObject();
            } catch (UnreadableException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            
            List<RecipeExecutionData> recipeExecutionDataList = null;

            if (myFilter == null)
                recipeExecutionDataList = optimizationAlgorithm.getRecipeExecutionData();
            else 
                recipeExecutionDataList = optimizationAlgorithm.getRecipeExecutionData(myFilter);

            logger.debug("recipe execution data selected from the optimizationAlgorithm = [" + recipeExecutionDataList + "]");

            // list is not serializable, so need conversion
            ArrayList<RecipeExecutionData> recipeExecutionData = new ArrayList(recipeExecutionDataList);

            ACLMessage reply = request.createReply();

            if (recipeExecutionData != null )  {      
                try {
                    reply.setContentObject(recipeExecutionData);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Recipe execution data could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            return reply;            
        }

        private ACLMessage manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE(ACLMessage request)
                 throws NotUnderstoodException, RefuseException
        {
            List<ProductInstance> productIstancesList = null;
            
            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE - begin");
            
            String recipeId = request.getContent();
            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE - recipeId = " + recipeId);
            if (recipeId == null)
                return null;

            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE - prima di query ");
            productIstancesList = optimizationAlgorithm.getProductInstancesPerRecipe(recipeId);

            logger.debug("recipe execution data selected from the optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE = [" + productIstancesList + "]");

            // list is not serializable, so need conversion
            ArrayList<ProductInstance> productInstancesArray = new ArrayList(productIstancesList);

            ACLMessage reply = request.createReply();

            if (productInstancesArray != null )  {      
                logger.debug("recipe execution data selected from the optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE productInstancesArray = [" + productInstancesArray + "]");
                try {
                    reply.setContentObject(productInstancesArray);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Recipe execution data (getProductInstancesPerRecipe could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            logger.debug("optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE reply = [" + reply + "]");
            return reply;            
        }
        
        private ACLMessage manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE(ACLMessage request)
                 throws NotUnderstoodException, RefuseException
        {
            List<Recipe> recipesList = null;
            
            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE - begin");
            
            String productInstanceId = request.getContent();
            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE - productInstanceId = " + productInstanceId);
            if (productInstanceId == null)
                return null;

            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE - prima di query ");
            recipesList = optimizationAlgorithm.getRecipesPerProductInstance(productInstanceId);

            logger.debug("recipe execution data selected from the optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE = [" + recipesList + "]");

            // list is not serializable, so need conversion
            ArrayList<Recipe> recipesArray = new ArrayList(recipesList);

            ACLMessage reply = request.createReply();

            if (recipesArray != null )  {      
                logger.debug("recipe execution data selected from the optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE recipesArray = [" + recipesArray + "]");
                try {
                    reply.setContentObject(recipesArray);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Recipe execution data (getRecipesPerProductInstances could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            logger.debug("optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE reply = [" + reply + "]");
            return reply;            
        }
        
        private ACLMessage manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE(ACLMessage request)
                 throws NotUnderstoodException, RefuseException
        {
            List<String> kpiNamesList = null;
            
            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE - begin");

            RecipeExecutionDataFilter recipeExecutionDataFilter = null;
            try {
                recipeExecutionDataFilter = (RecipeExecutionDataFilter) request.getContentObject();
            } catch (UnreadableException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            
//            String recipeId = request.getContent();
//            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE - recipeId = " + recipeId);
//            if (recipeId == null)
//                return null;

            logger.debug("manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE - prima di query ");
            kpiNamesList = optimizationAlgorithm.getKPISettingNamesPerRecipeExecutionData(recipeExecutionDataFilter);

            logger.debug("recipe execution data selected from the optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE = [" + kpiNamesList + "]");

            // list is not serializable, so need conversion
            ArrayList<String> kpiNamesArray = new ArrayList(kpiNamesList);

            ACLMessage reply = request.createReply();

            if (kpiNamesArray != null )  {      
                logger.debug("recipe execution data selected from the optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE kpiNamesArray = [" + kpiNamesArray + "]");
                try {
                    reply.setContentObject(kpiNamesArray);
                } catch (IOException ex) {
                    throw new RefuseException(ex.getLocalizedMessage());                
                }
                reply.setPerformative(ACLMessage.INFORM);
            } else {
                reply.setContent("Recipe execution data (getKPISettingNamesPerRecipe could not be retrieved.");
                reply.setPerformative(ACLMessage.FAILURE);
            }
            logger.debug("optimizationAlgorithm manage_ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE reply = [" + reply + "]");
            return reply;            
        }

    }
    
//}
