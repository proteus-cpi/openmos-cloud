package eu.openmos.agentcloud.optimizer.productionoptimizeragent;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.optimizer.productionoptimizer.ProductionOptimizerImpl;
import eu.openmos.agentcloud.utilities.OptimizationParameter;
import eu.openmos.agentcloud.optimizer.OptimizerAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;

// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.model.Recipe;
import eu.openmos.agentcloud.optimizer.OptimizerAgentInterface;
import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;//NOTE: This is an obsolete collection but it is JADE that is forcing its usage see ApplyAllRecipes inner class below
import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

/**
 * The ProductionOptimizerAgent extends the Optimizer class and provides a concrete implementation for a generic production optimizer.
 * This includes all the additional behaviours required to manage the deployment of recipes as well as an agent communication interface for activating the different optimization services
 * 
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 */
public class ProductionOptimizerAgent extends OptimizerAgent implements OptimizerAgentInterface {

    /**
     * NO_FAIL is a constant that defines that the Production Optimizer agent is working properly 
     */
    protected static final int NO_FAIL = -1;

    /**
     * FAIL_DEPLOY is a constant that defines that the Production Optimizer agent has detected a fail in the deployment procedure of user approved recipes
     */
    protected static final int FAIL_DEPLOY = 0;

    /**
     * FAIL_DEPLOY_SUGGEST is a constant that defines that the Production Optimizer agent has detected a fail in the deployment procedure of suggested recipes
     */
    protected static final int FAIL_DEPLOY_SUGGEST = 1;

    /**
     * FAIL_SOP is a constant that defines that the Production Optimizer agent has detected a fail in the activation of previously deployed recipes
     */
    protected static final int FAIL_SOP = 2;

    /**
     * The prodOptmizerStatus vairable holds the status of the production optimizer as one of the constants specified before
     */
    protected int prodOptmizerStatus;
    private String AGENT_PRODUCTIONOPTIMIZER_WS_VALUE;
    private static final String AGENT_PRODUCTIONOPTIMIZER_WS_PARAMETER = "openmos.agent.productionoptimizer.ws.endpoint";
    private static final Logger logger = Logger.getLogger(ProductionOptimizerAgent.class.getName());
    
    private final String optimizationServiceName;
    private List<OptimizationParameter> newParameters;
    private final List<String> failedDeploys = new LinkedList<>();
   
    /**
     * The constructor    
     * 
     * @param optimizationServiceName is the name of the optimization service (for future use)
     * @param optimizationAlgorithm is the optimization algorithm implementation to be used by the production optimizer
     */
    public ProductionOptimizerAgent(String optimizationServiceName, PluggableOptimizerInterface optimizationAlgorithm) {
        super(optimizationAlgorithm);
        this.prodOptmizerStatus = NO_FAIL;
        this.optimizationServiceName = optimizationServiceName;
    }

    @Override
    protected void setup() {
        super.setup();
        
        logger.info("Agent " + this.getName() + " is a Production Optimizer Agent and is setting up.");
        DFInteraction.RegisterInDF(this, optimizationServiceName, Constants.DF_PRODUCTION_OPTIMIZER);
        addBehaviour(new ProductionOptimizerEngine());

        AGENT_PRODUCTIONOPTIMIZER_WS_VALUE = ConfigurationLoader.getProperty(AGENT_PRODUCTIONOPTIMIZER_WS_PARAMETER);
        if (AGENT_PRODUCTIONOPTIMIZER_WS_VALUE == null) {
            String msg = getClass().getSimpleName() + " Missing configuration for " + AGENT_PRODUCTIONOPTIMIZER_WS_PARAMETER;
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        Endpoint.publish(AGENT_PRODUCTIONOPTIMIZER_WS_VALUE, new ProductionOptimizerImpl(this));
        logger.info("Published Agent Production Optimizer wsProductionOptimizer");
    }

    private final class ProductionOptimizerEngine extends CyclicBehaviour {

        @Override
        public void action() {
            switch (((ProductionOptimizerAgent) myAgent).status) {
                case OPT_UN_INITIALIZED:
                    if (!initializeOptimizer()) {
                        status = OPT_FAILURE;
                    }
                    break;
                case OPT_INITIALIZED:
                    myAgent.addBehaviour(new ProcessOptimizationRequests(myAgent, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                            MessageTemplate.MatchOntology(Constants.ONTO_PROC_OPT_REQS))));
                    status = OPT_READY;
                    break;
                case OPT_READY:
                    //NOTE if it is ready and the system is optimizable
                    //NOTE then optimize
                    //NOTE can only break out of this state if the ProcesseOptimizationRequest Behaviour receives a message that causes a state change or if the optimizer trigger a state change itself
                    break;
                case OPT_OPTIMIZING:
                    //NOTE can only break out of this state if the ProcesseOptimizationRequest Behaviour receives a message that causes a state change or if the optimizer trigger a state change itself
                    break;
                case OPT_STOP:
                    if (!stopOptimizer()) {
                        status = OPT_FAILURE;
                    }
                    break;
                case OPT_STOPPED:
                    //NOTE can only break out of this state if the ProcesseOptimizationRequest Behaviour receives a message that causes a state change or if the optimizer trigger a state change itself
                    break;
                case OPT_RESET:
                    if (!resetOptimizer()) {
                        status = OPT_FAILURE;
                    }

                    break;
                case OPT_RESETTED:
                    status = OPT_READY;
                    break;
                case OPT_REPARAMETRIZE:
                    if (!reparametrizeOptimizer(newParameters)) {
                        status = OPT_FAILURE;
                    }
                    break;
                case OPT_REPARAMETRIZED:
                    status = OPT_READY;
                    break;
                case OPT_FAILURE:
                    List<Recipe> recipes = null;
                    switch (prodOptmizerStatus) {
                        case FAIL_DEPLOY:                            
                            recipes = optimizationAlgorithm.failedDeploys(failedDeploys);                           
                            DeployRecipes(recipes, DeployRecipesRequest.DEPLOY);
                            break;
                        case FAIL_DEPLOY_SUGGEST:                            
                            recipes = optimizationAlgorithm.failedDeploysSuggest(failedDeploys);
                            DeployRecipes(recipes, DeployRecipesRequest.DEPLOY_SUGGEST);
                            break;
                        case FAIL_SOP:                            
                            recipes = optimizationAlgorithm.failedDeploysSOP(failedDeploys);
                            DeployRecipes(recipes, DeployRecipesRequest.DEPLOY);
                            break;
                    }                   
                    break;

            }
            block();
        }
        
        private void DeployRecipes(List<Recipe> recipes, int deployType) {
            ACLMessage deployRecipeMsg = new ACLMessage(ACLMessage.REQUEST);
            deployRecipeMsg.setOntology(Constants.ONTO_DEPLOY_RECIPES);
            DeployRecipesRequest deployContent = new DeployRecipesRequest(recipes, deployType);
            deployRecipeMsg.setContent(deployContent.toString());
            recipes.stream().forEach((r) -> {
                deployRecipeMsg.addReceiver(new AID(r.getUniqueAgentName(), false));
            });
            addBehaviour(new ApplyAllRecipes(myAgent, recipes, deployRecipeMsg));
        }

    }

    private final class ProcessOptimizationRequests extends AchieveREResponder {

        public ProcessOptimizationRequests(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            ProductionOptimizationRequest req = ProductionOptimizationRequest.fromString(request.getContent());
            ACLMessage reply = request.createReply();
            if (req.validate()) {
                switch (req.getType()) {
                    case ProductionOptimizationRequest.INITIALIZE:
                        if (((ProductionOptimizerAgent) myAgent).initializeOptimizer()) {
                            reply.setContent("Production Optimizer -> Initialized");
                            reply.setPerformative(ACLMessage.INFORM);
                        } else {
                            reply.setContent("Production Optimizer -> Initialization FAILED");
                            reply.setPerformative(ACLMessage.FAILURE);
                        }
                        break;
                    case ProductionOptimizationRequest.OPTIMIZE:
                        if (isOptimizable()) {                            
                            List<Recipe> recipes = (((ProductionOptimizerAgent) myAgent).optimizationAlgorithm).optimize();
                            reply.setContent("Production Optimizer -> Optimized");
                            reply.setPerformative(ACLMessage.AGREE);
                            ACLMessage deployRecipeMsg = new ACLMessage(ACLMessage.REQUEST);
                            deployRecipeMsg.setOntology(Constants.ONTO_DEPLOY_RECIPES);
                            DeployRecipesRequest deployContent = new DeployRecipesRequest(recipes, DeployRecipesRequest.DEPLOY);
                            deployRecipeMsg.setContent(deployContent.toString());
                            recipes.stream().forEach((r) -> {
                                deployRecipeMsg.addReceiver(new AID(r.getUniqueAgentName(), false));
                            });
                            registerPrepareResultNotification(new ApplyAllRecipes(myAgent, recipes, deployRecipeMsg));
                        }
                        break;
                    case ProductionOptimizationRequest.SUGGEST_OPTIMIZE:                        
                        List<Recipe> recipes = (((ProductionOptimizerAgent) myAgent).optimizationAlgorithm).suggestOptimization();
                        reply.setContent("Production Optimizer -> SuggestOptimized");
                        reply.setPerformative(ACLMessage.AGREE);
                        ACLMessage deployRecipeMsg = new ACLMessage(ACLMessage.REQUEST);
                        deployRecipeMsg.setOntology(Constants.ONTO_DEPLOY_SUGGEST_RECIPES);
                        DeployRecipesRequest deployContent = new DeployRecipesRequest(recipes, DeployRecipesRequest.DEPLOY_SUGGEST);
                        deployRecipeMsg.setContent(deployContent.toString());
                        recipes.stream().forEach((r) -> {
                            deployRecipeMsg.addReceiver(new AID(r.getUniqueAgentName(), false));
                        });
                        registerPrepareResultNotification(new ApplySuggestAllRecipes(myAgent, recipes, deployRecipeMsg));
                        break;
                    case ProductionOptimizationRequest.REPARAMETRIZE:
                        if (((ProductionOptimizerAgent) myAgent).reparametrizeOptimizer(req.getParameters())) {
                            reply.setContent("Production Optimizer -> Reparametrized");
                            reply.setPerformative(ACLMessage.INFORM);
                        } else {
                            reply.setContent("Production Optimizer -> Reparametrization FAILED");
                            reply.setPerformative(ACLMessage.FAILURE);
                        }
                        break;
                    case ProductionOptimizationRequest.RESET:
                        if (((ProductionOptimizerAgent) myAgent).resetOptimizer()) {
                            reply.setContent("Production Optimizer -> Reseted");
                            reply.setPerformative(ACLMessage.INFORM);
                        } else {
                            reply.setContent("Production Optimizer -> Reset FAILED");
                            reply.setPerformative(ACLMessage.FAILURE);
                        }
                        break;
                    case ProductionOptimizationRequest.STOP:
                        if (((ProductionOptimizerAgent) myAgent).stopOptimizer()) {
                            reply.setContent("Production Optimizer -> Stopped");
                            reply.setPerformative(ACLMessage.INFORM);
                        } else {
                            reply.setContent("Production Optimizer -> Stop FAILED");
                            reply.setPerformative(ACLMessage.FAILURE);
                        }
                        break;
                }
            }
            return reply;
        }

    }

    private final class ApplyAllRecipes extends AchieveREInitiator {

        private final List<ACLMessage> failures;
        private final List<ACLMessage> informs;
        private final List<Recipe> originalRecipes;

        public ApplyAllRecipes(Agent a, List<Recipe> originalRecipes, ACLMessage msg) {
            super(a, msg);
            this.failures = new LinkedList<>();
            this.informs = new LinkedList<>();
            this.originalRecipes = originalRecipes;

        }

        @Override
        protected void handleAllResultNotifications(Vector resultNotifications) {
            failures.clear();
            informs.clear();
            failedDeploys.clear();
            resultNotifications.stream().forEach((_item) -> {
                if (((ACLMessage) _item).getPerformative() == ACLMessage.FAILURE) {
                    failures.add(((ACLMessage) _item));
                    failedDeploys.add(((ACLMessage) _item).getSender().getName());
                } else if (((ACLMessage) _item).getPerformative() == ACLMessage.INFORM) {
                    informs.add(((ACLMessage) _item));
                }
            });

            ACLMessage initialRequest = (ACLMessage) ((AchieveREResponder) getParent()).getDataStore().get(((AchieveREResponder) getParent()).REQUEST_KEY);
            ACLMessage finalNotification = initialRequest.createReply();

            if (failures.isEmpty()) {
                ACLMessage sopMessage = new ACLMessage(ACLMessage.REQUEST);
                DeployRecipesRequest sopReq = new DeployRecipesRequest(originalRecipes, DeployRecipesRequest.SOP);
                sopMessage.setContent(sopReq.toString());
                sopMessage.setOntology(Constants.ONTO_DEPLOY_SOP);
                informs.stream().forEach((i) -> {
                    sopMessage.addReceiver(i.getSender());
                });
                addBehaviour(new AchieveREInitiator(myAgent, sopMessage) {

                    private final List<ACLMessage> sopFailures = new LinkedList<>();
                    private final List<ACLMessage> sopInforms = new LinkedList<>();

                    @Override
                    protected void handleAllResultNotifications(Vector resultNotifications) {
                        failedDeploys.clear();
                        resultNotifications.stream().forEach((_item) -> {
                            if (((ACLMessage) _item).getPerformative() == ACLMessage.FAILURE) {
                                sopFailures.add(((ACLMessage) _item));
                                failedDeploys.add(((ACLMessage) _item).getSender().getName());
                            } else if (((ACLMessage) _item).getPerformative() == ACLMessage.INFORM) {
                                sopInforms.add(((ACLMessage) _item));
                            }
                        });
                        if (!sopFailures.isEmpty()) {
                            //NOTE: Failure during the SOP
                            prodOptmizerStatus = FAIL_SOP;
                            status = OPT_FAILURE;
                        }
                    }

                });

                finalNotification.setPerformative(ACLMessage.INFORM);
                finalNotification.setContent("Optimization Successfull with SOP ongoing");

                ((AchieveREResponder) getParent()).getDataStore().put(((AchieveREResponder) getParent()).RESULT_NOTIFICATION_KEY, finalNotification);
                status = OPT_READY;
            } else {
                finalNotification.setPerformative(ACLMessage.FAILURE);
                finalNotification.setContent("Optimization UnSuccessfull since all the recipes could not be deployed");

                ((AchieveREResponder) getParent()).getDataStore().put(((AchieveREResponder) getParent()).RESULT_NOTIFICATION_KEY, finalNotification);
                //NOTE: Failure during deployment
                prodOptmizerStatus = FAIL_DEPLOY;
                status = OPT_FAILURE;
            }
        }

    }

    private final class ApplySuggestAllRecipes extends AchieveREInitiator {

        private final List<ACLMessage> failures;
        private final List<ACLMessage> informs;

        public ApplySuggestAllRecipes(Agent a, List<Recipe> originalRecipes, ACLMessage msg) {
            super(a, msg);
            this.failures = new LinkedList<>();
            this.informs = new LinkedList<>();
        }

        @Override
        protected void handleAllResultNotifications(Vector resultNotifications) {
            failures.clear();
            informs.clear();
            failedDeploys.clear();
            resultNotifications.stream().forEach((_item) -> {
                if (((ACLMessage) _item).getPerformative() == ACLMessage.FAILURE) {
                    failures.add(((ACLMessage) _item));
                    failedDeploys.add(((ACLMessage) _item).getSender().getName());
                } else if (((ACLMessage) _item).getPerformative() == ACLMessage.INFORM) {
                    informs.add(((ACLMessage) _item));
                }
            });

            ACLMessage initialRequest = (ACLMessage) ((AchieveREResponder) getParent()).getDataStore().get(((AchieveREResponder) getParent()).REQUEST_KEY);
            ACLMessage finalNotification = initialRequest.createReply();

            if (failures.isEmpty()) {
                finalNotification.setPerformative(ACLMessage.INFORM);
                finalNotification.setContent("Optimization Successfull with SOP ongoing");
                ((AchieveREResponder) getParent()).getDataStore().put(((AchieveREResponder) getParent()).RESULT_NOTIFICATION_KEY, finalNotification);
                status = OPT_READY;
            } else {
                finalNotification.setPerformative(ACLMessage.FAILURE);
                finalNotification.setContent("Optimization UnSuccessfull since all the recipes could not be deployed");
                ((AchieveREResponder) getParent()).getDataStore().put(((AchieveREResponder) getParent()).RESULT_NOTIFICATION_KEY, finalNotification);
                //NOTE: failure during deploument of suggestions
                prodOptmizerStatus = FAIL_DEPLOY_SUGGEST;
                status = OPT_FAILURE;
            }
        }

    }

}
