package eu.openmos.agentcloud.cloudinterface;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.model.OrderInstance;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import eu.openmos.model.*;

/**
 * Cloud Interface, the entry point of the agent-cloud for external systems 
 * (aka the Manifacturing Service Bus and optimizers).
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Pedro Lima Monteiro
 * @author Luis Ribeiro
 */
@WebService
public interface SystemConfigurator {
    
    /**
     * Asks the deployment agent to create a resource agent according with cyberPhysicalAgentDescription informations
     * and updates the cloud database.
     * Returns an object with the status of the operation.
     * 
     * @param cyberPhysicalAgentDescription   Object with the informations necessary to create the new resource agent.
     * @return ServiceCallStatus   Object with the result of the operation.
     */    
    @WebMethod(operationName = "createNewResourceAgent")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus createNewResourceAgent(
            @WebParam(name = "cyberPhysicalAgentDescription") SubSystem cyberPhysicalAgentDescription
    );

    /**
     * Asks the deployment agent to create a transport agent according with cyberPhysicalAgentDescription informations
     * and updates the cloud database.
     * Returns an object with the status of the operation.
     * 
     * @param cyberPhysicalAgentDescription   Object with the informations necessary to create the new transport agent.
     * @return ServiceCallStatus   Object with the result of the operation.
     */
    @WebMethod(operationName = "createNewTransportAgent")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus createNewTransportAgent(
            @WebParam(name = "cyberPhysicalAgentDescription") SubSystem cyberPhysicalAgentDescription
    );
    
    /**
     * Insert a new skill into the cloud database.
     * 
     * @param skill
     * @return ServiceCallStatus   Object with the result of the operation.
     */
    @WebMethod(operationName = "createNewSkill")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus createNewSkill(
            @WebParam(name = "skill") Skill skill
    );

    /**
     * Insert a new recipe into the cloud database
     * 
     * @param recipe
     * @return ServiceCallStatus   Object with the result of the operation.
     */
    @WebMethod(operationName = "createNewRecipe")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus createNewRecipe(
            @WebParam(name = "recipe") Recipe recipe
    );

    /**
     * Asks the agent to kill itself.
     * Returns an object with the status of the operation.
     * 
     * @param agentUniqueName   Unique name of the agent to be removed from the system.
     * @return ServiceCallStatus   Object with the result of the operation.
     */
    @WebMethod(operationName = "removeAgent")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus removeAgent(
            @WebParam(name = "agentUniqueName") String agentUniqueName
    );            
    
    /**     
     * Creates the requested order informations into the system.
     * Order lines (product instances) are marked as "TO_BE_PRODUCED".
     * No new agents are created at this call.
     * Returns an object with the status of the operation.
     * 
     * @param newOrder   Object with order details.
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "acceptNewOrderInstance")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus acceptNewOrderInstance(
            @WebParam(name = "newOrder") OrderInstance newOrder
    );

    /**
     * Marks the product instance as "PRODUCED" and removes the corresponding agent.
     * 
     * @param finishedProductInfo
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "finishedProduct")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus finishedProduct(
            @WebParam(name = "finishedProductInfo") FinishedProductInfo finishedProductInfo
    );

    /**
     * Update product instance status, value cold be TO_BE_PRODUCING or QUEUED.
     * 
     * @param productInstance
     * @return ServiceCallStatus  Object with operation result.
     */
    @WebMethod(operationName = "updateProduct")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus updateProduct(
            @WebParam(name = "productInstance") ProductInstance productInstance
    );
    
    /**
     * Marks the product instance as "PRODUCING" and creates the corresponding agent.
     * 
     * @param productInstance
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "startedProduct")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus startedProduct(
            @WebParam(name = "productInstance") ProductInstance productInstance
    );

    /**     
     * Order instance removal.
     * 
     * @param orderId   order unique id.
     * @param operationTimestamp   timestamp 
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "orderInstanceRemoval")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus orderInstanceRemoval(
            @WebParam(name = "orderId") String orderId, 
            @WebParam(name = "operationTimestamp") String operationTimestamp
    );

    /**     
     * Updates order instance information into the system.
     * Returns an object with the status of the operation.
     * 
     * @param order   Full object with order details.
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "orderInstanceUpdate")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus orderInstanceUpdate(
            @WebParam(name = "order") OrderInstance order
    );

    /**
     * Gets the full list of recipes from the cloud database.
     * 
     * @return list of recipes
     */
    @WebMethod(operationName = "getRecipes")
    @WebResult(name="recipes")
    public List<Recipe> getRecipes();

    /**
     * Gets full list of equipment observations from the cloud database.
     * 
     * @return list of equipment observations
     */
    @WebMethod(operationName = "getEquipmentObservationRel2s")
    @WebResult(name="equipmentObservationRel2s")
    public List<EquipmentObservationRel2> getEquipmentObservationRel2s();

    /**
     * Gets full list of equipment assessments from the cloud database.
     * 
     * @return list of equipment assessments
     */
    @WebMethod(operationName = "getEquipmentAssessments")
    @WebResult(name="equipmentAssessments")
    public List<EquipmentAssessment> getEquipmentAssessments();

    /**
     * Gets full list of physical adjustments from the cloud database.
     * 
     * @return list of physical adjustments
     */
    @WebMethod(operationName = "getPhysicalAdjustments")
    @WebResult(name="physicalAdjustments")
    public List<PhysicalAdjustment> getPhysicalAdjustments();

    /**
     * Gets full list of process assessments from the cloud database.
     * 
     * @return list of equipment assessments
     */
    @WebMethod(operationName = "getProcessAssessments")
    @WebResult(name="processAssessments")
    public List<ProcessAssessment> getProcessAssessments();

    /**
     * Gets full list of recipe execution data (data collected during order execution) from the cloud database.
     * 
     * @return list of recipe execution data
     */
    @WebMethod(operationName = "getRecipeExecutionData")
    @WebResult(name="recipeExecutionData")
    public List<RecipeExecutionData> getRecipeExecutionData();

    /**
     * Gets filtered list of recipe execution data (data collected during product execution) from the cloud database.
     * Filter can be applied to:
     * - recipe id
     * - product id
     * - kpi setting name
     * - begin interval
     * - end interval
     * 
     * @param recipeExecutionDataFilter
     * @return filtered list of recipe execution data
     */
    @WebMethod(operationName = "getFilteredRecipeExecutionData")
    @WebResult(name="recipeExecutionData")
    public List<RecipeExecutionData> getFilteredRecipeExecutionData(@WebParam(name = "recipeExecutionDataFilter") RecipeExecutionDataFilter recipeExecutionDataFilter);

    /**
     * Returns recipes list of a subsystem.
     * 
     * @param subSystemId
     * @return list of subsystem recipes
     */
    @WebMethod(operationName = "getRecipesBySubSystemId")
    @WebResult(name="recipes")
    public List<Recipe> getRecipesBySubSystemId(@WebParam(name = "subSystemId") String subSystemId);

    /**
     * Stores recipes into the cloud database. It inserts new recipes and updates existing recipes
     * and updates also data in running agents memory.
     * 
     * @param recipes
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "putRecipes")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus putRecipes(@WebParam(name = "recipes") Recipe[] recipes);

    /**
     * Gets subsystems list from the cloud database.
     * 
     * @return list of existing subsystems
     */
    @WebMethod(operationName = "getSubSystems")
    @WebResult(name="subSystems")
    public List<SubSystem> getSubSystems();

    /**
     * Gets full list of execution tables from the cloud database.
     * 
     * @return list of execution tables 
     */
    @WebMethod(operationName = "getExecutionTables")
    @WebResult(name="executionTables")
    public List<ExecutionTable> getExecutionTables();
    
    /**
     * Loads a specified execution table from the cloud database given its unique identifier.
     * 
     * @param executionTableId
     * @return execution table object
     */
    @WebMethod(operationName = "getExecutionTableById")
    @WebResult(name="executionTable")
    public ExecutionTable getExecutionTableById(@WebParam(name = "executionTableId") String executionTableId);
    
    /**
     * Loads the execution table of a given subsystem from the cloud database.
     * 
     * @param subSystemId
     * @return execution table object.
     */
    @WebMethod(operationName = "getExecutionTableBySubSystemId")
    @WebResult(name="executionTable")
    public ExecutionTable getExecutionTableBySubSystemId(@WebParam(name = "subSystemId") String subSystemId);
    
    /**
     * Updates a subsystem execution table data into the cloud database and
     * into running agent memory.
     * 
     * @param agentUniqueName the agent id, aka the subsystem id
     * @param executionTable execution table to update into the database
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "updateExecutionTable")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus updateExecutionTable(
            @WebParam(name = "agentUniqueName") String agentUniqueName,
            @WebParam(name = "executionTable") ExecutionTable executionTable);
    
    /**
     * Updates list of execution tables into the cloud database and
     * into running agent memory.
     * 
     * @param executionTables
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "updateExecutionTables")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus updateExecutionTables(
            @WebParam(name = "executionTables") List<ExecutionTable> executionTables);

    /**    
     * Stores a new product definition into the cloud database.
     * 
     * @param product The new product definition.
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "newProductDefinition")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus newProductDefinition(
            @WebParam(name = "productDefinition") Product product
    );

    /**
     * Stores a new recipe execution data object into the cloud database.
     * Created as an alternative when socket communication MSB-cloud is not possible.
     * 
     * @param recipeExecutionData
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "newRecipeExecutionData")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus newRecipeExecutionData(
            @WebParam(name = "recipeExecutionData") RecipeExecutionData recipeExecutionData
    );

    /**
     * Loads a skill from the cloud database given its unique identifier.
     * 
     * @param skillId
     * @return the skill object
     */
    @WebMethod(operationName = "getSkill")
    @WebResult(name="skill")
    public Skill getSkill(@WebParam(name = "skillId") String skillId);
    
    /**
     * Loads a product definition from the cloud database given the product unique identifier.
     * 
     * @param productId
     * @return product definition object
     */
    @WebMethod(operationName = "getProduct")
    @WebResult(name="product")
    public Product getProduct(@WebParam(name = "productId") String productId);
    
    /**
     * Loads an order instance data from the cloud database given its unique identifier.
     * 
     * @param orderInstanceId
     * @return the order instance object
     */
    @WebMethod(operationName = "getOrderInstance")
    @WebResult(name="orderInstance")
    public OrderInstance getOrderInstance(@WebParam(name = "orderInstanceId") String orderInstanceId);
    
    /**
     * Tracks system stage change.
     * 
     * @param newStage the new stage of the system
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "changeSystemStage")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus changeSystemStage(
            @WebParam(name = "newStage") String newStage);
    
    /**
     * Tracks subsystem stage change.
     * 
     * @param subSystemId unique id of the subsystem
     * @param newStage the new stage of the subsystem
     * @return ServiceCallStatus  Object with the result of the operation.
     */
    @WebMethod(operationName = "changeSubSystemStage")
    @WebResult(name="serviceCallStatus")
    public ServiceCallStatus changeSubSystemStage(
            @WebParam(name = "subSystemId") String subSystemId,
            @WebParam(name = "newStage") String newStage);
}
