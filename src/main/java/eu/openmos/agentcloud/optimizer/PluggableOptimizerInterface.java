package eu.openmos.agentcloud.optimizer;

import eu.openmos.model.OrderInstance;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.model.ProductLeavingWorkstationOrTransportData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.UnexpectedProductData;
import eu.openmos.agentcloud.utilities.OptimizationParameter;
import java.util.Date;
import java.util.List;
import eu.openmos.model.*;

/**
 * The PluggableOptimizerInterface interface defines the minimum set of function that a custom optimization algorithm needs to support to be compatible with the optimizer agent
 * 
 * @author Luis Ribeiro<luis.ribeiro@liu.se>
 */
public interface PluggableOptimizerInterface {     

    /**
     * The initializeOptimizer method initializes the custom optimizer in the implementation class
     * 
     * @return true if the process succeeded and false otherwise 
     */
    public boolean initializeOptimizer();    

    /**
     * The stopOptimizer method stops the custom optimizer in the implementation class
     * 
     * @return true if the process succeeded and false otherwise 
     */
    public boolean stopOptimizer();    

    /**
     * The resetOptimizer method resets the custom optimizer in the implementation class
     * 
     * @return true if the process succeeded and false otherwise 
     */
    public boolean resetOptimizer();    

    /**
     * The reparametrizeOptimizer method re-parametrizes the custom optimizer in the implementation class     
     * 
     * @param newParameters are the optimizers setup parameters 
     * @return true if the process succeeded and false otherwise 
     */
    @Deprecated
    public boolean reparametrizeOptimizer(List<OptimizationParameter> newParameters);    

    /**
     * The isOptimizable method checks of the system can be optimized
     * 
     * @return true if the system can be optimized and false otherwise
     */
    public boolean isOptimizable();    

    /**
     * The newDataAvailable method notifies the optimization algorithm implementation that new performance data is available and hands that data over to the algorithm
     * 
     * @param agentPerformanceData is a user customizable string of data (the system does no make any assumptions or checks on the contents of the data)
     */
//    public boolean newDataAvailable(String agentPerformanceData);    
    public boolean newDataAvailableForEquipment(RawEquipmentData red);    

    public boolean newDataAvailableForProduct(RawProductData rpd);    
    
    /**
     * The newDataAvailable method notifies the optimization algorithm implementation that an heartbeat signal has been missed for an agent
     * 
     * @param agentUniqueName is the id of the agent for which an heartbeat signal has been missed
     */
    public boolean newAgentWithMissingHB(String agentUniqueName);    

    /**
     * The newAgentInPlatform method notifies the optimization algorithm implementation that an new agent has joined the platform
     * 
     * @param cpad contains the information of the new agent that joined the platform
     */
//    public boolean newAgentInPlatform(CyberPhysicalAgentDescription cpad);    
    public boolean newAgentInPlatform(SubSystem cpad);    

    /**
     * The newOrderInPlatform method notifies the optimization algorithm implementation that an new order has been placed
     * 
     * @param order contains the information of the new order that has been placed
     */
    public boolean newOrderInstanceInPlatform(OrderInstance order);

    /**
     * The agentRemovedFromPlatform method notifies the optimization algorithm implementation that an agent has left the platform
     * 
     * @param agentUniqueName is the Id of the agent that left the platform
     */
    public boolean agentRemovedFromPlatform(String agentUniqueName);

    /**
     * The optimize method causes the optimizer to execute and attempt to optimize the system based on the user validate recipes
     * 
     * @return a List of user approved recipes that should be applied to the system to optimize it
     */
    public List<Recipe> optimize();    

    /**
     * The suggestOptimization method causes the optimizer to execute and attempt to optimize the system by creating new recipes
     * 
     * @return a List of new recipes requiring user approval that the optimizer implementation has computed to further optimize the system
     */
    public List<Recipe> suggestOptimization();    

    /**
     *
     * The failedDeploys method informs the optimizer implementation that recipes could not be deployed in some agents
     * 
     * @param agentsThatFailed is the list of agents that have failed the deployment
     * @return a new list of recipes to be deployed instead
     */
    public List<Recipe> failedDeploys(List<String> agentsThatFailed);    

    /**
     * The failedDeploysSuggest method informs the optimizer implementation that suggested recipes could not be deployed in some agents
     * 
     * @param agentsThatFailed is the list of agents that have failed the deployment
     * @return a new list of recipes to be deployed instead
     */
    public List<Recipe> failedDeploysSuggest(List<String> agentsThatFailed);    

    /**
     * The failedDeploysSOP method informs the optimizer implementation that recipes could not be made active in some agents
     * 
     * @param agentsThatFailed is the list of agents that have failed the activation of recipes
     * @return a new list of recipes to be deployed instead
     */
    public List<Recipe> failedDeploysSOP(List<String> agentsThatFailed);
    
    /**
    * WP4 Cloud Platform Re-work related code.
    * 
    * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
    * @author Valerio Gentile <valerio.gentile@we-plus.eu>
    */
     
    public boolean processNewRecipe(Recipe recipe); 
    
    public List<Recipe> getRecipes();
    public List<Recipe> getRecipesBySubSystemId(String subSystemId);
    
    public boolean putRecipes(Recipe[] recipes);
    
    public boolean processNewSkill(Skill skill); 
    
//    public boolean finishedProduct(String equipmentId, String productId, Date timestamp); 
    public boolean finishedProduct(FinishedProductInfo finishedProductInfo); 
    public boolean startedProduct(StartedProductInfo startedProductInfo);
    public boolean updateProduct(ProductInstance productInstance);
    
    public boolean removeOrderInstance(String orderId, Date timestamp); 
    
    public boolean updateOrderInstance(OrderInstance order); 
    
    public boolean trackUnexpectedProductData(UnexpectedProductData unexpectedProductData);

    public boolean trackProductLeavingWorkstationOrTransportData(ProductLeavingWorkstationOrTransportData productLeavingWorkstationOrTransportData);

    public boolean newPhysicalLocation(PhysicalLocation physicalLocation);

    public boolean newRecipeExecutionData(RecipeExecutionData recipeExecutionData);
    
//    public boolean updateWorkstation(CyberPhysicalAgentDescription newCpad);   
    public boolean updateWorkstation(SubSystem newCpad);   
    
    public boolean newEquipmentObservation(EquipmentObservation equipmentObservation);
    
    public List<EquipmentObservation> listEquipmentObservations();
    
    public boolean newTriggeredSkill(TriggeredSkill triggeredSkill);
    public boolean newTriggeredRecipe(TriggeredRecipe triggeredRecipe);
    public List<TriggeredSkill> listTriggeredSkills(String skillId);
    public List<TriggeredRecipe> listTriggeredRecipes(String recipeId);
    
    public boolean newEquipmentObservationRel2(EquipmentObservationRel2 equipmentObservationRel2);
    public EquipmentObservationRel2 getEquipmentObservationRel2(String equipmentObservationRel2Id);
    public List<EquipmentObservationRel2> listEquipmentObservationRel2s();
    public List<EquipmentObservationRel2> listEquipmentObservationRel2s(String equipmentId);
    public List<EquipmentObservationRel2> listSystemObservationRel2s();

    public ProcessAssessment getProcessAssessment(String processAssessmentId);
    public List<ProcessAssessment> listProcessAssessments();
    public List<ProcessAssessment> listProcessAssessments(String recipeId);
    public List<ProcessAssessment> listProcessAssessmentsForRecipe(String recipeId);
    public List<ProcessAssessment> listProcessAssessmentsForSkill(String skillId);
    public boolean newProcessAssessment(ProcessAssessment processAssessment);
        
    public EquipmentAssessment getEquipmentAssessment(String equipmentAssessmentId);
    public List<EquipmentAssessment> listEquipmentAssessments();
    public List<EquipmentAssessment> listEquipmentAssessments(String equipmentId);
    public List<EquipmentAssessment> listSystemAssessments();
    public boolean newEquipmentAssessment(EquipmentAssessment equipmentAssessment);    
    
    public PhysicalAdjustment getPhysicalAdjustment(String physicalAdjustmentId);
    public List<PhysicalAdjustment> listPhysicalAdjustments();
    public List<PhysicalAdjustment> listPhysicalAdjustments(String equipmentId);
    public boolean newPhysicalAdjustment(PhysicalAdjustment physicalAdjustment);
    
    
    public List<SubSystem> getSubSystems();
    
        public ExecutionTable getExecutionTableById(String executionTableId);
        
        public ExecutionTable getExecutionTableBySubSystemId(String subSystemId);
        
        public boolean updateExecutionTable(ExecutionTable executionTable);
        
        public List<ExecutionTable> getExecutionTables();

    public boolean updateWorkstationRecipes(SubSystemRecipes subSystemRecipes);

    public boolean newProductDefinition(Product product);

    public List<RecipeExecutionData> getRecipeExecutionData();
    public List<RecipeExecutionData> getRecipeExecutionData(RecipeExecutionDataFilter recipeExecutionDataFilter);

    public Product getProduct(String productId);

    public OrderInstance getOrderInstance(String orderInstanceId);

    public Skill getSkill(String skillId);

    public List<ProductInstance> getProductInstancesPerRecipe(String recipeId);
    public List<Recipe> getRecipesPerProductInstance(String productInstanceId);

    public List<String> getKPISettingNamesPerRecipeExecutionData(RecipeExecutionDataFilter recipeExecutionDataFilter);

    public List<OrderInstance> listOrderInstances();

    public List<ProductInstance> listProductInstancesNotYetProduced();

    public List<SystemChangeStage> listSystemStageChanges();
    public boolean newSystemStageChange(SystemChangeStage systemChangeStage);
    public SystemChangeStage getLastSystemStageChange();
    // public int countSystemAssessmentsAfterDate(String afterDate);        
    public boolean isSystemRampUpChangeStagePossible();

    public List<SubSystemChangeStage> listSubSystemStageChanges(String subSystemId);
    public boolean newSubSystemStageChange(SubSystemChangeStage subSystemChangeStage);
    public SubSystemChangeStage getLastSubSystemStageChange(String subSystemId);
    public boolean isSubSystemRampUpChangeStagePossible(String subSystemId);
}
