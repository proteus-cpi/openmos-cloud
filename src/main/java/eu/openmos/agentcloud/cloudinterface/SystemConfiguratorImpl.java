package eu.openmos.agentcloud.cloudinterface;

import eu.openmos.agentcloud.agent.initiators.ExecutionTableUpdateInitiator;
import eu.openmos.agentcloud.agent.initiators.RecipeExecutionInitiator;
import eu.openmos.agentcloud.agent.initiators.WorkstationUpdateInitiator;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.cloudinterfaceagent.CreateNewRecipeInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.CreateNewSkillInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.DeployNewAgentInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.DeployNewOrderInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.OrderRemovalInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.RemoveAgentInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.UpdateOrderInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.data.RecipeDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.agentcloud.cloudinterfaceagent.DeployNewProductInstanceInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.NewProductDefinitionInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.NewSubSystemStageChangeInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.NewSystemStageChangeInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.PutRecipesInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.StartExecutionTableUpdateInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.StartSubSystemRecipesListUpdateInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.StartSubSystemRecipeUpdateInitiator;
import eu.openmos.agentcloud.productagent.ProductInstanceInternalUpdateInitiator;
import eu.openmos.agentcloud.cloudinterfaceagent.data.ExecutionTableDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.OrderInstanceDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.ProductDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.RecipeExecutionDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.SkillDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.SubSystemDataWrapper;
import eu.openmos.agentcloud.productagent.ProductInstanceUpdateInitiator;
import eu.openmos.agentcloud.productagent.RemoveProductAgentInitiator;
import eu.openmos.agentcloud.services.rest.JadeGatewayConnector;
import eu.openmos.agentcloud.services.rest.data.EquipmentAssessmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentObservationRel2DataWrapper;
import eu.openmos.agentcloud.services.rest.data.PhysicalAdjustmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.ProcessAssessmentDataWrapper;
import eu.openmos.model.OrderInstance;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import java.util.List;
import java.util.ResourceBundle;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import eu.openmos.model.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Cloud Interface, the entry point of the agent-cloud for external systems (aka the Manifacturing Service Bus).
 * This concrete web-service class implements agent creation, agent removal and order submission.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
@WebService(endpointInterface = "eu.openmos.agentcloud.cloudinterface.SystemConfigurator", serviceName = "SystemConfigurator")
public class SystemConfiguratorImpl implements SystemConfigurator {

    private static final Logger logger = Logger.getLogger(SystemConfiguratorImpl.class.getName());
    private Agent myAgent = null;

    /**
     * Default constructor.
     */
    public SystemConfiguratorImpl() {
    }

    /**
     * Alternative constructor. Takes a reference of the cloud interface agent.
     * 
     * @param a  The cloud interface agent.
     */
    public SystemConfiguratorImpl(Agent a) {
        this.myAgent = a;
    }

    /**
     * Asks the deployment agent to create an agent according with cyberPhysicalAgentDescription informations.
     * Returns an object with the status of the operation.
     * 
     * @param cyberPhysicalAgentDescription   Object with the informations necessary to create the new agent.
     * @return AgentStatus   Object with the result of the operation.
     */
//    private ServiceCallStatus createNewAgent(CyberPhysicalAgentDescription cyberPhysicalAgentDescription, String agentType) {
    private ServiceCallStatus createNewAgent(SubSystem cyberPhysicalAgentDescription, String agentType) {
        logger.debug("on the cloud, after calling createNewResourceAgent: " + cyberPhysicalAgentDescription);
        String operationName = "createNewAgent";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";
        
        
        
        if (cyberPhysicalAgentDescription == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.CREATENEWAGENT_NEWAGENTINFO_PARAMETER_NULL);
        logger.debug(loggingInfo + "agentType = " + agentType);
        logger.debug(loggingInfo + cyberPhysicalAgentDescription.toString());
        
        // the service call forces the correct type agent
        cyberPhysicalAgentDescription.setSsType(agentType);
        if (cyberPhysicalAgentDescription.getSsType() == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.CREATENEWAGENT_NEWAGENTINFO_TYPE_NULL);

//        String agentClass = decodeAgentClass((String) cyberPhysicalAgentDescription.getType());
//        if (agentClass == null || agentClass.length() == 0)
//            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.CREATENEWAGENT_NEWAGENTINFO_TYPE_UNKNOWN);

        if (this.myAgent == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.CLOUDINTERFACEAGENT_NULL);

        DFAgentDescription[] deploymentAgentsList = DFInteraction.SearchInDF(Constants.DF_DEPLOYMENT, this.myAgent);
        if (deploymentAgentsList.length == 0)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.DEPLOYMENTAGENT_NULL);
        
        
        
//        myAgent.addBehaviour(
//                new DeployNewAgentInitiator(
//                        myAgent,
//                        DeployNewAgentInitiator.buildMessage(
//                                deploymentAgentsList[0].getName(),
//                                cyberPhysicalAgentDescription.toString()))
//        );


/*
String cpadString = null;
        try
        {
            byte[] cpadBytes = null;
            // cpadBytes = cyberPhysicalAgentDescription.serialize();
            cpadBytes = DataSerializer.serialize(cyberPhysicalAgentDescription);
            logger.debug("cpad serialized bytes length: " + cpadBytes.length);
            cpadString = new String(cpadBytes);
            logger.debug("cpad serialized: " + cpadString);
        }
        catch (IOException e)
        {
            logger.error(e);
        }
*/


//        myAgent.addBehaviour(
//                new DeployNewAgentInitiator(
//                        myAgent,
//                        DeployNewAgentInitiator.buildMessage(
//                                deploymentAgentsList[0].getName(),
//                                cpadString))
//        );
        myAgent.addBehaviour(
                new DeployNewAgentInitiator(
                        myAgent,
                        DeployNewAgentInitiator.buildMessage(
                                deploymentAgentsList[0].getName(),
                                cyberPhysicalAgentDescription))
        );

        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.CREATENEWAGENT_CALL_SUCCESS);
    }

    /**
     * Sends a message to an agent asking it to suicide.
     * Returns an object with the status of the operation.
     * 
     * @param agentUniqueName   Unique name of the agent to be removed from the system.
     * @return AgentStatus   Object with the result of the operation.
     */
    @Override
    public ServiceCallStatus removeAgent(String agentUniqueName) {
        String operationName = "removeAgent";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";

        logger.info(loggingInfo + agentUniqueName);

        if (agentUniqueName == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.REMOVEAGENT_EXISTINGAGENTINFO_PARAMETER_NULL);
        if (this.myAgent == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.CLOUDINTERFACEAGENT_NULL);

        AID agentToBeDeregistered = new AID(agentUniqueName, false);

        // VaG & Pedro - 06/12/2016
        // Moved into the takeDown() of the CyberPhhysicalAgent
/*        
        DFInteraction.DeregisterFromDF(myAgent, agenToBeDeregistered);
*/

        logger.debug(loggingInfo + "creating removal message...");
//        ACLMessage removalMessage = RemoveAgentInitiator.buildMessage(agentToBeDeregistered,"");        
        ACLMessage removalMessage = RemoveAgentInitiator.buildMessage(agentToBeDeregistered, agentUniqueName);
        logger.debug(loggingInfo + "removal message created: " + removalMessage.toString());

        // behaviour instantiation and adding to the agent        
        myAgent.addBehaviour(new RemoveAgentInitiator(myAgent, removalMessage));

        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.REMOVEAGENT_CALL_SUCCESS);
    }

    /**
     * Returns the java class fully qualified name matching with the requested agent type.
     * 
     * @param agentType   The agent type (resource or transport or product)
     * @return   The corresponding fully qualified java class name
     */    
    private String decodeAgentClass(String agentType) {       
        if (agentType.equals(Constants.DF_RESOURCE)) return Constants.RESOURCE_AGENT_CLASS;
        if (agentType.equals(Constants.DF_PRODUCT)) return Constants.PRODUCT_AGENT_CLASS;
        if (agentType.equals(Constants.DF_TRANSPORT)) return Constants.TRANSPORT_AGENT_CLASS;
        return null;
    }

    /**
     * Looks into the resultmessages.properties file and returns the description matching with the code.
     * The properties file is a resource bundle so it may be localized.
     * 
     * @param msgCode   The message code
     * @return   The corresponding message description.
     */
    private String getMessage(String msgCode) {
        ResourceBundle messages = ResourceBundle.getBundle("resultsmessages");
        String message = messages.getString(msgCode);
        return message;
    }

    /**
     * Returns an AgentStatus object with KO code and the given message.
     * 
     * @param msgCode   The message code
     * @return   The AgentStatus object filled with failure-related informations.
     */
    private ServiceCallStatus traceAndReturnError(String className, String operationName, String msgCode) {
        String msg = getMessage(msgCode);
        logger.error(className + " - " + operationName + " - " + msg);
        return new ServiceCallStatus(className, operationName, ServiceCallStatus.KO, msg);
    }

    /**
     * Returns an AgentStatus object with OK code.
     * 
     * @param className The java class name
     * @param operationName  The java method name
     * @param msgCode   The message code
     * @return   The AgentStatus object filled with success-related informations.
     */
    private ServiceCallStatus traceAndReturnSuccess(String className, String operationName, String msgCode) {
        String msg = getMessage(msgCode);
        logger.info(className + " - " + operationName + " - " + msg);
        return new ServiceCallStatus(className, operationName, ServiceCallStatus.OK, msg);
    }

    /**     
     * Sends a message to the deployment agent asking for creation of product agents matching the requested order informations.
     * Returns an object with the status of the operation.
     * 
     * @param newOrder   Object with order details.
     * @return OrderStatus  Object with the result of the operation.
     */
    @Override
    public ServiceCallStatus acceptNewOrderInstance(OrderInstance newOrder) {
        String operationName = "acceptNewOrderInstance";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";

        logger.info(loggingInfo + "operation started");

        DFAgentDescription[] deploymentAgentsList = DFInteraction.SearchInDF(Constants.DF_DEPLOYMENT, this.myAgent);
        if (deploymentAgentsList.length == 0)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.DEPLOYMENTAGENT_NULL);

        myAgent.addBehaviour(
                new DeployNewOrderInitiator(
                        myAgent,
                        DeployNewOrderInitiator.buildMessage(
                                deploymentAgentsList[0].getName(),
                                newOrder))
//                                newOrder.toString()))
        );
        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.ACCEPTNEWORDER_CALL_SUCCESS);
    }

    @Override
    public ServiceCallStatus orderInstanceRemoval(String orderId, String operationTimestamp) {
        String operationName = "orderRemoval";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";

        logger.info(loggingInfo + "operation started");
        
        String content = orderId.concat("|").concat(operationTimestamp);
        myAgent.addBehaviour(new OrderRemovalInitiator(myAgent, OrderRemovalInitiator.buildMessage(myAgent, content)));
        return traceAndReturnSuccess(getClass().getName(), "orderRemoval", ServiceCallStatus.ORDERREMOVAL_CALL_SUCCESS);
    }

    @Override
    public ServiceCallStatus orderInstanceUpdate(OrderInstance order) {
        String operationName = "orderUpdate";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";

        logger.info(loggingInfo + "operation started");

        DFAgentDescription[] deploymentAgentsList = DFInteraction.SearchInDF(Constants.DF_DEPLOYMENT, this.myAgent);
        if (deploymentAgentsList.length == 0)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.DEPLOYMENTAGENT_NULL);

        myAgent.addBehaviour(
                new UpdateOrderInitiator(
                        myAgent,
                        UpdateOrderInitiator.buildMessage(
                                deploymentAgentsList[0].getName(),
                                order))
//                                order.toString()))
        );
        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.UPDATEORDER_CALL_SUCCESS);
    }

    @Override
//    public ServiceCallStatus createNewResourceAgent(CyberPhysicalAgentDescription cyberPhysicalAgentDescription) {
    public ServiceCallStatus createNewResourceAgent(SubSystem cyberPhysicalAgentDescription) {
        return createNewAgent(cyberPhysicalAgentDescription, Constants.DF_RESOURCE);
    }

    @Override
//    public ServiceCallStatus createNewTransportAgent(CyberPhysicalAgentDescription cyberPhysicalAgentDescription) {
    public ServiceCallStatus createNewTransportAgent(SubSystem cyberPhysicalAgentDescription) {
        return createNewAgent(cyberPhysicalAgentDescription, Constants.DF_TRANSPORT);
    }

    @Override
    public ServiceCallStatus createNewSkill(Skill skill) {
//        myAgent.addBehaviour(new CreateNewSkillInitiator(myAgent, CreateNewSkillInitiator.buildMessage(myAgent, skill.toString())));
        myAgent.addBehaviour(new CreateNewSkillInitiator(myAgent, CreateNewSkillInitiator.buildMessage(myAgent, skill)));
        return traceAndReturnSuccess(getClass().getName(), "createNewSkill", ServiceCallStatus.CREATENEWSKILL_CALL_SUCCESS);
    }

    @Override
    public ServiceCallStatus createNewRecipe(Recipe recipe) {
//        myAgent.addBehaviour(new CreateNewRecipeInitiator(myAgent, CreateNewRecipeInitiator.buildMessage(myAgent, recipe.toString())));
        myAgent.addBehaviour(new CreateNewRecipeInitiator(myAgent, CreateNewRecipeInitiator.buildMessage(myAgent, recipe)));
        return traceAndReturnSuccess(getClass().getName(), "createNewRecipe", ServiceCallStatus.CREATENEWRECIPE_CALL_SUCCESS);
    }

    @Override
//    public ServiceCallStatus finishedProduct(String equipmentId, String productId, String operationTimestamp) {
    public ServiceCallStatus finishedProduct(FinishedProductInfo finishedProductInfo)
    {
        // String content = equipmentId.concat("|").concat(productId).concat("|").concat(operationTimestamp);
//        myAgent.addBehaviour(new FinishedProductInitiator(myAgent, FinishedProductInitiator.buildMessage(myAgent, finishedProductInfo)));
//        return traceAndReturnSuccess(getClass().getName(), "finishedProduct", ServiceCallStatus.FINISHEDPRODUCT_CALL_SUCCESS);
        String operationName = "finishedProduct";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";
        
        if (finishedProductInfo == null ||
                finishedProductInfo.getProductInstanceId() == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.FINISHEDPRODUCT_EXISTINGAGENTINFO_PARAMETER_NULL);

        String productInstanceId = finishedProductInfo.getProductInstanceId();
        logger.info(loggingInfo + productInstanceId);

        if (this.myAgent == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.CLOUDINTERFACEAGENT_NULL);

        AID agentToBeDeregistered = new AID(productInstanceId, false);

        // VaG & Pedro - 06/12/2016
        // Moved into the takeDown() of the CyberPhhysicalAgent
/*        
        DFInteraction.DeregisterFromDF(myAgent, agenToBeDeregistered);
*/

        logger.debug(loggingInfo + "creating product finished message...");
//        ACLMessage removalMessage = RemoveAgentInitiator.buildMessage(agentToBeDeregistered,"");        
        ACLMessage removalMessage = RemoveProductAgentInitiator.buildMessage(agentToBeDeregistered, finishedProductInfo);
        logger.debug(loggingInfo + "removal message created: " + removalMessage.toString());

        // behaviour instantiation and adding to the agent        
        myAgent.addBehaviour(new RemoveProductAgentInitiator(myAgent, removalMessage));

        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.FINISHEDPRODUCT_CALL_SUCCESS);
    }    

    @Override
    public ServiceCallStatus updateProduct(ProductInstance productInstance) {
       String operationName = "updateProduct";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";       

        logger.info(loggingInfo + "operation started");
        
        if (productInstance == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.UPDATEDPRODUCT_CALL_ERROR);
        if (productInstance.getUniqueId() == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.UPDATEDPRODUCT_CALL_ERROR);
            

//
        myAgent.addBehaviour(new ProductInstanceUpdateInitiator(
                        myAgent,
                        ProductInstanceUpdateInitiator.buildMessage(
                                myAgent,
                                productInstance))
        );
//        
        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.UPDATEDPRODUCT_CALL_SUCCESS);
    }
    

    @Override
    public ServiceCallStatus startedProduct(ProductInstance productInstance)
    {
//        myAgent.addBehaviour(new FinishedProductInitiator(myAgent, FinishedProductInitiator.buildMessage(myAgent, finishedProductInfo)));
//        return traceAndReturnSuccess(getClass().getName(), "finishedProduct", ServiceCallStatus.FINISHEDPRODUCT_CALL_SUCCESS);
        String operationName = "startedProduct";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";

        logger.info(loggingInfo + "operation started");

        DFAgentDescription[] deploymentAgentsList = DFInteraction.SearchInDF(Constants.DF_DEPLOYMENT, this.myAgent);
        if (deploymentAgentsList.length == 0)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.DEPLOYMENTAGENT_NULL);

        myAgent.addBehaviour(
                new DeployNewProductInstanceInitiator(
                        myAgent,
                        DeployNewProductInstanceInitiator.buildMessage(
                                deploymentAgentsList[0].getName(),
                                productInstance))
        );
        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.STARTEDPRODUCT_CALL_SUCCESS);
    }    

    @Override
    public List<Recipe> getRecipes() {
        logger.debug("recipes get");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeDataWrapper recipeDataWrapper = new RecipeDataWrapper();
        recipeDataWrapper.setOntology(Constants.ONTO_GET_RECIPES);
        jgc.getRecipes(recipeDataWrapper);

        logger.debug("Soap service getRecipes operation output -> " + recipeDataWrapper.getMessage() );

        List<Recipe> rs = recipeDataWrapper.getRecipesArrayList();
        
        logger.debug("recipes list: " + rs);

        return rs;
    }

    @Override
    public List<EquipmentObservationRel2> getEquipmentObservationRel2s() {
        logger.debug("equipment observations getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper = new EquipmentObservationRel2DataWrapper();
        equipmentObservationRel2DataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S);
        jgc.listEquipmentObservationRel2s(equipmentObservationRel2DataWrapper);

        logger.debug("Soap service EquipmentObservationRel2 list operation output -> " + equipmentObservationRel2DataWrapper.getMessage() );

        List<EquipmentObservationRel2> eos = equipmentObservationRel2DataWrapper.getEquipmentObservationRel2sArrayList();
        
        logger.debug("equipment observations rel2 list: " + eos);

        return eos;
    }

    @Override
    public List<EquipmentAssessment> getEquipmentAssessments() {
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

    @Override
    public List<SubSystem> getSubSystems() {
        logger.debug("subsystems get");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        SubSystemDataWrapper subSystemDataWrapper = new SubSystemDataWrapper();
        subSystemDataWrapper.setOntology(Constants.ONTO_GET_SUBSYSTEMS);
        jgc.getSubSystems(subSystemDataWrapper);

        logger.debug("Soap service getSubSystems operation output -> " + subSystemDataWrapper.getMessage() );

        List<SubSystem> rs = subSystemDataWrapper.getSubSystemsArrayList();
        
        logger.debug("subsystems list: " + rs);

        return rs;
    }

    @Override
    public List<ExecutionTable> getExecutionTables() {
        logger.debug("getExecutionTables");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ExecutionTableDataWrapper executionTableDataWrapper = new ExecutionTableDataWrapper();
        executionTableDataWrapper.setOntology(Constants.ONTO_GET_EXECUTION_TABLES);
        jgc.getExecutionTables(executionTableDataWrapper);

        logger.debug("Soap service getExecutionTables operation output -> " + executionTableDataWrapper.getMessage() );

        List<ExecutionTable> rs = executionTableDataWrapper.getExecutionTablesArrayList();
        
        logger.debug("getExecutionTables list: " + rs);

        return rs;
    }

    @Override
    public ExecutionTable getExecutionTableById(String executionTableId) {
        logger.debug("getExecutionTableById");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ExecutionTableDataWrapper executionTableDataWrapper = new ExecutionTableDataWrapper();
        executionTableDataWrapper.setOntology(Constants.ONTO_GET_EXECUTION_TABLE_BY_ID);
        executionTableDataWrapper.setExecutionTableId(executionTableId);
        jgc.getExecutionTableById(executionTableDataWrapper);

        logger.debug("Soap service getExecutionTableById operation output -> " + executionTableDataWrapper.getMessage() );

        ExecutionTable executionTable = executionTableDataWrapper.getExecutionTableById();
        
        logger.debug("getExecutionTableById: " + executionTable);

        return executionTable;
    }

    @Override
    public ExecutionTable getExecutionTableBySubSystemId(String subSystemId) {
        logger.debug("getExecutionTableBySubSystemId");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ExecutionTableDataWrapper executionTableDataWrapper = new ExecutionTableDataWrapper();
        executionTableDataWrapper.setOntology(Constants.ONTO_GET_EXECUTION_TABLE_BY_SUBSYSTEM_ID);
        executionTableDataWrapper.setSubSystemId(subSystemId);
        jgc.getExecutionTableBySubSystemId(executionTableDataWrapper);

        logger.debug("Soap service getExecutionTableBySubSystemId operation output -> " + executionTableDataWrapper.getMessage() );

        ExecutionTable executionTable = executionTableDataWrapper.getExecutionTableBySubSystemId();
        
        logger.debug("getExecutionTableBySubSystemId: " + executionTable);

        return executionTable;
    }
    @Override
    public List<Recipe> getRecipesBySubSystemId(String subSystemId) {
        logger.debug("getRecipesBySubSystemId");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeDataWrapper recipeDataWrapper = new RecipeDataWrapper();
        recipeDataWrapper.setOntology(Constants.ONTO_GET_RECIPES_BY_SUBSYSTEM_ID);
        recipeDataWrapper.setSubSystemId(subSystemId);
        jgc.getRecipesBySubSystemId(recipeDataWrapper);

        logger.debug("Soap service getRecipesBySubSystemId operation output -> " + recipeDataWrapper.getMessage() );

        List<Recipe> rs = recipeDataWrapper.getRecipesArrayList();
        
        logger.debug("recipes list: " + rs);

        return rs;
    }

    @Override
    public ServiceCallStatus updateExecutionTable(String agentUniqueName, ExecutionTable executionTable) {
        String operationName = "updateExecutionTable";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";

        logger.info(loggingInfo + agentUniqueName);

        if (agentUniqueName == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.UPDATEEXECUTIONTABLE_EXISTINGAGENTINFO_PARAMETER_NULL);
        if (this.myAgent == null)
            return traceAndReturnError(getClass().getName(), operationName, ServiceCallStatus.CLOUDINTERFACEAGENT_NULL);

        AID agentToBeUpdated = new AID(agentUniqueName, false);

        logger.debug(loggingInfo + "creating message...");
        ACLMessage startUpdateETMessage = StartExecutionTableUpdateInitiator.buildMessage(agentToBeUpdated, executionTable);
        logger.debug(loggingInfo + "start message created: " + startUpdateETMessage.toString());

        // behaviour instantiation and adding to the agent        
        myAgent.addBehaviour(new StartExecutionTableUpdateInitiator(myAgent, startUpdateETMessage));

        return traceAndReturnSuccess(getClass().getName(), operationName, ServiceCallStatus.UPDATEEXECUTIONTABLE_CALL_SUCCESS);
    }

    @Override
    public ServiceCallStatus putRecipes(Recipe[] recipes) {
        Map<String, List<Recipe>> recipesForSubSystemsMap = new HashMap<>();
        
        for (Recipe r : recipes)
        {
            // VaG - 07/12/2017
            // recipes from external optimizers have to be validated by human
            r.setValid(false);
            
            List<String> equipmentIds = r.getEquipmentIds();
            logger.debug("putRecipes - recipe " + r.getName() + ": " + equipmentIds);
            if (equipmentIds != null)
            {
                for (String id : equipmentIds)
                {
                    if (recipesForSubSystemsMap.containsKey(id))
                    {
                        List<Recipe> subSystemRecipes = recipesForSubSystemsMap.get(id);
                        subSystemRecipes.add(r);
                        recipesForSubSystemsMap.put(id, subSystemRecipes);                        
                    }
                    else
                    {
                        List<Recipe> subSystemRecipes = new LinkedList<>();
                        subSystemRecipes.add(r);
                        recipesForSubSystemsMap.put(id, subSystemRecipes);                        
                    }
                }
            }
        }
        logger.debug("recipesForSubSystemsMap: " + recipesForSubSystemsMap);
        
        for (String equipmentId : recipesForSubSystemsMap.keySet())
        {
            AID agentToBeUpdated = new AID(equipmentId, false);
            logger.debug("putRecipes - creating message for " + equipmentId);
            
            List<Recipe> recipesToUpdateList = recipesForSubSystemsMap.get(equipmentId);
            Recipe[] recipesToUpdate = recipesToUpdateList.toArray(new Recipe[recipesToUpdateList.size()]);

            ACLMessage startSubSystemRecipesListUpdateMessage = StartSubSystemRecipesListUpdateInitiator.buildMessage(agentToBeUpdated, recipesToUpdate);
            logger.debug("putRecipes - start message created: " + startSubSystemRecipesListUpdateMessage.toString());
            // behaviour instantiation and adding to the agent        
            myAgent.addBehaviour(new StartSubSystemRecipesListUpdateInitiator(myAgent, startSubSystemRecipesListUpdateMessage));                    
        }

        logger.debug("putRecipes - before updating recipes");
        
        // initiator for updating recipes into database
        myAgent.addBehaviour(new PutRecipesInitiator(myAgent, PutRecipesInitiator.buildMessage(myAgent, recipes)));
        return traceAndReturnSuccess(getClass().getName(), "putRecipes", ServiceCallStatus.PUTRECIPES_CALL_SUCCESS);
    }
    
    public ServiceCallStatus putRecipes_WORKS_BUT(Recipe[] recipes) {
        
        for (Recipe r : recipes)
        {
            List<String> equipmentIds = r.getEquipmentIds();
            logger.debug("putRecipes - recipe " + r.getName() + ": " + equipmentIds);
            if (equipmentIds != null)
            {
                for (String id : equipmentIds)
                {
                    AID agentToBeUpdated = new AID(id, false);

                    logger.debug("putRecipes - creating message for " + id);

                    ACLMessage startSubSystemRecipesUpdateMessage = StartSubSystemRecipeUpdateInitiator.buildMessage(agentToBeUpdated, r);
                    logger.debug("putRecipes - start message created: " + startSubSystemRecipesUpdateMessage.toString());
                    // behaviour instantiation and adding to the agent        
                    myAgent.addBehaviour(new StartSubSystemRecipeUpdateInitiator(myAgent, startSubSystemRecipesUpdateMessage));                    
                }
            }            
        }
        logger.debug("putRecipes - before updating recipes");
        
        // initiator for updating recipes into database
        myAgent.addBehaviour(new PutRecipesInitiator(myAgent, PutRecipesInitiator.buildMessage(myAgent, recipes)));
        return traceAndReturnSuccess(getClass().getName(), "putRecipes", ServiceCallStatus.PUTRECIPES_CALL_SUCCESS);
    }

/*    @Override */
    public ServiceCallStatus putRecipes_NOTWORKING(Recipe[] recipes) {
        
        Set commonSet = new HashSet();
        List<String> mergedEquipmentIds = new LinkedList<>();
        for (Recipe r : recipes)
        {
            List<String> equipmentIds = r.getEquipmentIds();
            logger.debug("putRecipes - recipe " + r.getName() + ": " + equipmentIds);
            if (equipmentIds != null)
                commonSet.addAll(equipmentIds);    
        }
        mergedEquipmentIds.addAll(commonSet);            
        logger.debug("putRecipes - mergedEquipmentIds list: " + mergedEquipmentIds);
        
        if (mergedEquipmentIds == null || mergedEquipmentIds.size() == 0)
        {
            logger.info("putRecipes stops 'cause mergedEquipmentIds list is empty");
            return traceAndReturnSuccess(getClass().getName(), "putRecipes", ServiceCallStatus.PUTRECIPES_CALL_SUCCESS);        
        }
        
        logger.debug("putRecipes - before asking subsystems list");
        List<SubSystem> subSystems = getSubSystems();
        logger.debug("putRecipes - after asking subsystems list");
        
        for (SubSystem ss : subSystems)
        {
            logger.debug("putRecipes - current subsystem is " + ss.getName());
            
            if (mergedEquipmentIds.contains(ss.getUniqueId()))
            {
                List<Recipe> subSystemRecipesList = new LinkedList<>();
                for (Recipe r1 : recipes)
                {
                    List<String> equipmentIds = r1.getEquipmentIds();
                    if (equipmentIds.contains(ss.getUniqueId()))
                        subSystemRecipesList.add(r1);
                }
                
                Recipe[] subSystemRecipes = subSystemRecipesList.toArray(new Recipe[subSystemRecipesList.size()]);
                AID agentToBeUpdated = new AID(ss.getName(), false);
                
                ss.setRecipes(subSystemRecipesList);

                logger.debug("putRecipes - creating message for " + ss.getName());
                logger.debug("putRecipes - recipes list for this subsystem: " + subSystemRecipes);
                
                ACLMessage startSubSystemRecipesListUpdateMessage = StartSubSystemRecipesListUpdateInitiator.buildMessage(agentToBeUpdated, subSystemRecipes);
                logger.debug("putRecipes - start message created: " + startSubSystemRecipesListUpdateMessage.toString());
                // behaviour instantiation and adding to the agent        
                myAgent.addBehaviour(new StartSubSystemRecipesListUpdateInitiator(myAgent, startSubSystemRecipesListUpdateMessage));
                
                // need to update subsystem into database
                myAgent.addBehaviour(new WorkstationUpdateInitiator(myAgent, WorkstationUpdateInitiator.buildMessage(myAgent, ss)));

            }                    
        }
        logger.debug("putRecipes - before updating recipes");
        
        // initiator for updating recipes into database
        myAgent.addBehaviour(new PutRecipesInitiator(myAgent, PutRecipesInitiator.buildMessage(myAgent, recipes)));
        return traceAndReturnSuccess(getClass().getName(), "putRecipes", ServiceCallStatus.PUTRECIPES_CALL_SUCCESS);
    }
    
    @Override
    public ServiceCallStatus newProductDefinition(Product product) {
        myAgent.addBehaviour(new NewProductDefinitionInitiator(myAgent, NewProductDefinitionInitiator.buildMessage(myAgent, product)));
        return traceAndReturnSuccess(getClass().getName(), "newProductDefinition", ServiceCallStatus.NEWPRODUCTDEFINITION_CALL_SUCCESS);
    }

    @Override
    public ServiceCallStatus newRecipeExecutionData(RecipeExecutionData recipeExecutionData) {
        myAgent.addBehaviour(new RecipeExecutionInitiator(myAgent, RecipeExecutionInitiator.buildMessage(myAgent, recipeExecutionData)));
        return traceAndReturnSuccess(getClass().getName(), "newRecipeExecutionData", ServiceCallStatus.NEWRECIPEEXECUTIONDATA_CALL_SUCCESS);
    }

    @Override
    public List<RecipeExecutionData> getRecipeExecutionData() {
        logger.debug("getRecipeExecutionData - begin");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA);
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Soap service getRecipeExecutionData operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<RecipeExecutionData> rs = recipeExecutionDataWrapper.getRecipeExecutionDataArrayList();
        
        logger.debug("recipe execution data list: " + rs);

        return rs;
    }

    @Override
    public List<RecipeExecutionData> getFilteredRecipeExecutionData(RecipeExecutionDataFilter recipeExecutionDataFilter ) {
        logger.debug("getFilteredRecipeExecutionData with filter - begin");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        RecipeExecutionDataWrapper recipeExecutionDataWrapper = new RecipeExecutionDataWrapper();
        recipeExecutionDataWrapper.setOntology(Constants.ONTO_GET_RECIPEEXECUTIONDATA);
        recipeExecutionDataWrapper.setRecipeExecutionDataFilter(recipeExecutionDataFilter);
        jgc.getRecipeExecutionData(recipeExecutionDataWrapper);

        logger.debug("Soap service getFilteredRecipeExecutionData operation output -> " + recipeExecutionDataWrapper.getMessage() );

        List<RecipeExecutionData> rs = recipeExecutionDataWrapper.getRecipeExecutionDataArrayList();
        
        logger.debug("recipe execution data list: " + rs);

        return rs;
    }

    @Override
    public Skill getSkill(String skillId) {
        logger.debug("getSkill - begin");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        SkillDataWrapper skillDataWrapper = new SkillDataWrapper();
        skillDataWrapper.setOntology(Constants.ONTO_GET_SKILL);
        skillDataWrapper.setSkillId(skillId);
        jgc.getSkill(skillDataWrapper);

        logger.debug("Soap service getSkill operation output -> " + skillDataWrapper.getMessage() );

        Skill s = skillDataWrapper.getSkill();
        
        logger.debug("skill: " + s);

        return s;
    }

    @Override
    public Product getProduct(String productId) {
        logger.debug("getProduct - begin");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ProductDataWrapper productDataWrapper = new ProductDataWrapper();
        productDataWrapper.setOntology(Constants.ONTO_GET_PRODUCT);
        productDataWrapper.setProductId(productId);
        jgc.getProduct(productDataWrapper);

        logger.debug("Soap service getProduct operation output -> " + productDataWrapper.getMessage() );

        Product p = productDataWrapper.getProduct();
        
        logger.debug("product: " + p);

        return p;
    }

    @Override
    public OrderInstance getOrderInstance(String orderInstanceId) {
        logger.debug("getOrderInstance - begin");
        
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

    @Override
    public ServiceCallStatus updateExecutionTables(List<ExecutionTable> executionTables) {
        if (executionTables == null)
            return traceAndReturnError(getClass().getName(), "updateExecutionTables", ServiceCallStatus.UPDATEEXECUTIONTABLE_CALL_FAILURE);
        
        for (ExecutionTable et : executionTables)
        {
            boolean status = updateExecutionTable(et);
            if (!status)
                return traceAndReturnError(getClass().getName(), "updateExecutionTables", ServiceCallStatus.UPDATEEXECUTIONTABLE_CALL_FAILURE);
        }                

        return traceAndReturnSuccess(getClass().getName(), "updateExecutionTables", ServiceCallStatus.UPDATEEXECUTIONTABLE_CALL_SUCCESS);        
    }

    private boolean updateExecutionTable(ExecutionTable executionTable)
    {
        String operationName = "updateExecutionTable";
        String loggingInfo = getClass().getName() + " - " + operationName + " - ";
        
        if (executionTable == null)
            return false;

        logger.debug(loggingInfo + "creating message...");
        ACLMessage startUpdateETMessage = ExecutionTableUpdateInitiator.buildMessage(myAgent, executionTable);
        logger.debug(loggingInfo + "start message created: " + startUpdateETMessage.toString());

        // behaviour instantiation and adding to the agent        
        myAgent.addBehaviour(new ExecutionTableUpdateInitiator(myAgent, startUpdateETMessage));

        return true;
    }

    @Override
    public List<ProcessAssessment> getProcessAssessments() {
        logger.debug("equipment assessments getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        ProcessAssessmentDataWrapper processAssessmentDataWrapper = new ProcessAssessmentDataWrapper();
        processAssessmentDataWrapper.setOntology(Constants.ONTO_LIST_PROCESS_ASSESSMENTS);
        jgc.listProcessAssessments(processAssessmentDataWrapper);

        logger.debug("Rest service processAssessment list operation output -> " + processAssessmentDataWrapper.getMessage() );

        List<ProcessAssessment> eos = processAssessmentDataWrapper.getProcessAssessmentsArrayList();
        
        logger.debug("process assessments list: " + eos);

        return eos;
    }

    @Override
    public ServiceCallStatus changeSystemStage(String newState) {
        SystemChangeStage scs = new SystemChangeStage();
        scs.setUniqueId((new Date()).toString());
        scs.setNewStage(newState);
        scs.setRegistered(new Date());
        
        myAgent.addBehaviour(new NewSystemStageChangeInitiator(myAgent, NewSystemStageChangeInitiator.buildMessage(myAgent, scs)));
        return traceAndReturnSuccess(getClass().getName(), "changeSystemStatus", ServiceCallStatus.CHANGE_SYSTEM_STATUS_CALL_SUCCESS);
    }

    @Override
    public ServiceCallStatus changeSubSystemStage(String subSystemId, String newState) {
        SubSystemChangeStage scs = new SubSystemChangeStage();
        scs.setUniqueId((new Date()).toString());
        scs.setSubSystemId(subSystemId);
        scs.setNewStage(newState);
        scs.setRegistered(new Date());
        
        myAgent.addBehaviour(new NewSubSystemStageChangeInitiator(myAgent, NewSubSystemStageChangeInitiator.buildMessage(myAgent, scs)));
        return traceAndReturnSuccess(getClass().getName(), "changeSubSystemStatus", ServiceCallStatus.CHANGE_SUBSYSTEM_STATUS_CALL_SUCCESS);
    }

    @Override
    public List<PhysicalAdjustment> getPhysicalAdjustments() {
        logger.debug("physical adjustments getList");
        
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();        
        
        PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper = new PhysicalAdjustmentDataWrapper();
        physicalAdjustmentDataWrapper.setOntology(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS);
        jgc.listPhysicalAdjustments(physicalAdjustmentDataWrapper);

        logger.debug("Rest service PhysicalAdjustment list operation output -> " + physicalAdjustmentDataWrapper.getMessage() );

        List<PhysicalAdjustment> eos = physicalAdjustmentDataWrapper.getPhysicalAdjustmentsArrayList();
        
        logger.debug("physical adjustments list: " + eos);

        return eos;
    }

    class RecipesForSubSystem
    {
        protected String subSystemId;
        protected List<Recipe> recipes;

        public String getSubSystemId() {
            return subSystemId;
        }

        public void setSubSystemId(String subSystemId) {
            this.subSystemId = subSystemId;
        }

        public List<Recipe> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<Recipe> recipes) {
            this.recipes = recipes;
        }
        
        
    }

}
