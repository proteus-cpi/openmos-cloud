package eu.openmos.agentcloud.optimizer.databasegateway;

import eu.openmos.model.OrderInstance;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.model.ProductLeavingWorkstationOrTransportData;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.model.RawProductData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.UnexpectedProductData;
import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.agentcloud.utilities.OptimizationParameter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import eu.openmos.model.*;
import eu.openmos.model.utilities.SerializationConstants;
import java.text.SimpleDateFormat;

/**
* WP4 Cloud Platform Re-work related code.
* 
* @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
* @author Valerio Gentile <valerio.gentile@we-plus.eu>
*/
public class DatabaseGateway implements PluggableOptimizerInterface {
    protected MongoDB db;
    protected MongoDBInterface client;
    private static final Logger logger = Logger.getLogger(DatabaseGateway.class.getName());

    @Override
    public boolean initializeOptimizer() {
        db = new MongoDB();
        db.launch();
        if(db.running) client = new MongoDBInterface(db.getMyHost().toString());
        return db.running;
    }

    @Override
    public boolean stopOptimizer() {
        db.shutdown();
        return !db.running;
    }

    @Override
    public boolean resetOptimizer() {
        client = new MongoDBInterface(db.getMyHost().toString());
        return true;
    }

    @Override
    public boolean isOptimizable() {
        return client.isOptimizable();
    }

    @Override
    public List<Recipe> optimize() {
        try {
            return client.optimize();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Recipe> suggestOptimization() {
        try {
            return client.optimize();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Recipe> failedDeploys(List<String> agentsThatFailed) {
        logger.warn("DATABASE GATEWAY - FAILED DEPLOYS: " + agentsThatFailed);
        try {
            return client.optimize();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Recipe> failedDeploysSuggest(List<String> agentsThatFailed) {
        logger.warn("DATABASE GATEWAY - FAILED DEPLOYS SUGGEST: " + agentsThatFailed);
        try {
            return client.optimize();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Recipe> failedDeploysSOP(List<String> agentsThatFailed) {
        logger.warn("DATABASE GATEWAY - FAILED DEPLOYS SOP: " + agentsThatFailed);
        try {
            return client.optimize();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
//    public boolean newDataAvailable(String agentPerformanceData) {
//        logger.debug("DATABASE GATEWAY - NEW DATA AVAILABLE: " + agentPerformanceData);
//        try {            
//            if(agentPerformanceData.contains(SerializationConstants.TOKEN_RAW_EQUIPMENT_DATA))
//                return client.newRawEquipmentData(RawEquipmentData.fromString(agentPerformanceData));
//            else
//                return client.newRawProductData(RawProductData.fromString(agentPerformanceData));
//        } catch (ParseException ex) {
//            logger.error(ex.getMessage());
//            return false;
//        }
//    }
    public boolean newDataAvailableForEquipment(RawEquipmentData red) {
        logger.debug("DATABASE GATEWAY - NEW DATA AVAILABLE FOR EQUIPMENT: " + red);
        return client.newRawEquipmentData(red);
//                return client.newRawProductData(RawProductData.fromString(agentPerformanceData));
    }
    public boolean newDataAvailableForProduct(RawProductData rpd) {
        logger.debug("DATABASE GATEWAY - NEW DATA AVAILABLE FOR PRODUCT: " + rpd);
        return client.newRawProductData(rpd);
//                return client.newRawProductData(RawProductData.fromString(agentPerformanceData));
    }

    @Override
    public boolean newAgentWithMissingHB(String agentUniqueName) {
        logger.debug("DATABASE GATEWAY - NEW AGENT WITH MISSING HB: " + agentUniqueName);
        return client.newAgentWithMissingHeartBeat(agentUniqueName.split("@")[0]);
    }

    @Override
    public boolean agentRemovedFromPlatform(String agentUniqueName) {
        logger.debug("DATABASE GATEWAY - REMOVE AGENT IN PLATFORM: " + agentUniqueName);
        return client.removeAgentInPlatform(agentUniqueName.split("@")[0]);
    }

    @Override
//    public boolean newAgentInPlatform(CyberPhysicalAgentDescription cpad) {
    public boolean newAgentInPlatform(SubSystem cpad) {
        logger.debug("DATABASE GATEWAY - NEW AGENT IN PLATFORM: " + cpad.toString());
        return client.newAgentInPlatform(cpad);
    }

    @Override
    public boolean updateProduct(ProductInstance productInstance) {
        logger.debug("DATABASE GATEWAY - UPDATE PRODUCT: " + productInstance);
        return client.updateProduct(productInstance);
    }
    
    @Override
//    public boolean finishedProduct(String equipmentId, String productId, Date timestamp) {
    public boolean finishedProduct(FinishedProductInfo finishedProductInfo) {
//        logger.debug("DATABASE GATEWAY - FINISHED PRODUCT: " + equipmentId + " - " + productId + " - " + timestamp.toString());
        logger.debug("DATABASE GATEWAY - FINISHED PRODUCT: " + finishedProductInfo);
//        return client.processFinishedProduct(equipmentId, productId, timestamp);
        return client.processFinishedProduct(finishedProductInfo);
    }
    
    @Override
    public boolean startedProduct(StartedProductInfo startedProductInfo) {
        logger.debug("DATABASE GATEWAY - STARTED PRODUCT: " + startedProductInfo);
        return client.processStartedProduct(startedProductInfo);
    }

    @Override
    public boolean newOrderInstanceInPlatform(OrderInstance order) {
        logger.debug("DATABASE GATEWAY - NEW ORDER IN PLATFORM: " + order.toString());
        try
        {
            return client.newOrderInstance(order);
        }
        catch (Exception e)
        {
            logger.error("DatabaseGateway - newOrderInPlatform - database operation error - please check the log file - " + e);
            return false;
        }
    }

    @Override
    public boolean processNewRecipe(Recipe recipe) {
        logger.debug("DATABASE GATEWAY - PROCESS NEW RECIPE: " + recipe.toString());
        return client.newRecipe(recipe);
    }

    @Override
    public boolean processNewSkill(Skill skill) {
        logger.debug("DATABASE GATEWAY - PROCESS NEW SKILL: " + skill.toString());
        return client.newSkill(skill);
    }

    @Override
    public boolean removeOrderInstance(String orderId, Date timestamp) {        
        logger.debug("DATABASE GATEWAY - REMOVE ORDER: " + orderId + " - " + timestamp.toString());
        return client.removeOrderInstance(orderId, timestamp);
    }  

    @Override
    public boolean updateOrderInstance(OrderInstance order) {     
        logger.debug("DATABASE GATEWAY - UPDATE ORDER: " + order.toString());
        try
        {
            return client.updateOrder(order);
        }
        catch (Exception e)
        {
            logger.error("DatabaseGateway - updateOrder - database operation error - please check the log file - " + e);
            return false;
        }
    }  

    @Override
    public boolean trackUnexpectedProductData(UnexpectedProductData unexpectedProductData) {
        logger.debug("DATABASE GATEWAY - UNEXPECTED PRODUCT DATA: " + unexpectedProductData.toString());
        return client.trackUnexpectedProductData(unexpectedProductData);
    }

    @Override
    public boolean trackProductLeavingWorkstationOrTransportData(ProductLeavingWorkstationOrTransportData productLeavingWorkstationOrTransportData) {
        logger.debug("DATABASE GATEWAY - PRODUCT LEAVING WORKSTATION OR TRANSPORT DATA: " + productLeavingWorkstationOrTransportData.toString());
        return client.trackProductLeavingWorkstationOrTransportData(productLeavingWorkstationOrTransportData);
    }

    @Override
    public boolean newPhysicalLocation(PhysicalLocation physicalLocation) {
        logger.debug("DATABASE GATEWAY - NEW PHYSICAL LOCATION: " + physicalLocation.toString());
        return client.newPhysicalLocation(physicalLocation);
    }

    @Override
    public boolean newRecipeExecutionData(RecipeExecutionData recipeExecutionData) {
        logger.debug("DATABASE GATEWAY - RECIPE EXECUTION DATA: " + recipeExecutionData.toString());
        return client.newRecipeExecutionData(recipeExecutionData);
    }

    @Override
//    public boolean updateWorkstation(CyberPhysicalAgentDescription newCpad) {
    public boolean updateWorkstation(SubSystem newCpad) {
        logger.debug("DATABASE GATEWAY - UPDATE WORKSTATION DATA: " + newCpad.toString());
        return client.updateWorkstation(newCpad);
    }

    /**
     * Not being used in the current status of the platform. 
     * To be removed in further updates.
     * @param newParameters N/A
     * @return N/A
     */
    @Deprecated
    @Override
    public boolean reparametrizeOptimizer(List<OptimizationParameter> newParameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean newEquipmentObservation(EquipmentObservation equipmentObservation) {
        logger.debug("DATABASE GATEWAY - NEW EQUIPMENT OBSERVATION IN PLATFORM: " + equipmentObservation.toString());
        return client.newEquipmentObservation(equipmentObservation);
    }
    
    @Override
    public boolean newEquipmentObservationRel2(EquipmentObservationRel2 equipmentObservationRel2) {
        logger.debug("DATABASE GATEWAY - NEW EQUIPMENT OBSERVATION REL2 IN PLATFORM: " + equipmentObservationRel2.toString());
        return client.newEquipmentObservationRel2(equipmentObservationRel2);
    }
    
    @Override
    public boolean newTriggeredSkill(TriggeredSkill triggeredSkill) {
        logger.debug("DATABASE GATEWAY - NEW TRIGGERED SKILL IN PLATFORM: " + triggeredSkill.toString());
        return client.newTriggeredSkill(triggeredSkill);
    }
    @Override
    public boolean newTriggeredRecipe(TriggeredRecipe triggeredRecipe) {
        logger.debug("DATABASE GATEWAY - NEW TRIGGERED RECIPE IN PLATFORM: " + triggeredRecipe.toString());
        return client.newTriggeredRecipe(triggeredRecipe);
    }
    
    @Override
    public boolean newEquipmentAssessment(EquipmentAssessment equipmentAssessment) {
        logger.debug("DATABASE GATEWAY - NEW EQUIPMENT ASSESSMENT IN PLATFORM: " + equipmentAssessment.toString());
        return client.newEquipmentAssessment(equipmentAssessment);
    }
    
    @Override
    public boolean newProcessAssessment(ProcessAssessment processAssessment) {
        logger.debug("DATABASE GATEWAY - NEW PROCESS ASSESSMENT IN PLATFORM: " + processAssessment.toString());
        return client.newProcessAssessment(processAssessment);
    }
    
    public List<EquipmentObservation> listEquipmentObservations() {
        logger.debug("DATABASE GATEWAY - LIST EQUIPMENT OBSERVATIONS IN PLATFORM ");

        try {
            return client.listEquipmentObservations();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public List<EquipmentAssessment> listSystemAssessments() {
        logger.debug("DATABASE GATEWAY - LIST SYSTEM ASSESSMENT IN PLATFORM ");

        try {
            return client.listSystemAssessments();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    public List<EquipmentObservationRel2> listSystemObservationRel2s() {
        logger.debug("DATABASE GATEWAY - LIST SYSTEM OBSERVATIONS REL2 IN PLATFORM ");

        try {
            return client.listSystemObservationRel2s();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    public List<EquipmentObservationRel2> listEquipmentObservationRel2s(String equipmentId) {
        logger.debug("DATABASE GATEWAY - LIST EQUIPMENT OBSERVATIONS REL2 for equipment " + equipmentId + " IN PLATFORM ");

        try {
            return client.listEquipmentObservationRel2s(equipmentId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    public List<EquipmentObservationRel2> listEquipmentObservationRel2s() {
        logger.debug("DATABASE GATEWAY - LIST EQUIPMENT OBSERVATIONS REL2 IN PLATFORM ");

        try {
            return client.listEquipmentObservationRel2s();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public List<EquipmentAssessment> listEquipmentAssessments() {
        logger.debug("DATABASE GATEWAY - LIST EQUIPMENT ASSESSMENTS IN PLATFORM ");

        try {
            return client.listEquipmentAssessments();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    public List<EquipmentAssessment> listEquipmentAssessments(String equipmentId) {
        logger.debug("DATABASE GATEWAY - LIST EQUIPMENT ASSESSMENTS for equipment " + equipmentId + " IN PLATFORM ");

        try {
            return client.listEquipmentAssessments(equipmentId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean isSystemRampUpChangeStagePossible()            
    {       
        SystemChangeStage scs = getLastSystemStageChange();
        logger.debug("system change state: " + scs);
        if (scs == null)
            return false;   // no phase change found
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);        
        String afterDate = sdf.format(scs.getRegistered());
        
        int i; // = countSystemAssessmentsAfterDate(afterDate);
        i = 0;
        try {
            i = client.countSystemAssessmentsAfterDate(afterDate);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        
        return i > 0;
    }
    
    @Override
    public boolean isSubSystemRampUpChangeStagePossible(String subSystemId)            
    {       
        if (subSystemId == null)
            return false;
        
        SubSystemChangeStage scs = getLastSubSystemStageChange(subSystemId);
        logger.debug("subsystem change state: " + scs);
        if (scs == null)
            return false;   // no phase change found
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);        
        String afterDate = sdf.format(scs.getRegistered());
        
        int ea = 0;
        try {
            ea = client.countEquipmentAssessmentsAfterDate(subSystemId, afterDate);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        int pa = 0;
        try {
            pa = client.countProcessAssessmentsAfterDate(subSystemId, afterDate);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        
        return (ea + pa) > 0;
    }
    
    /* @Override */
    /*
    public int countSystemAssessmentsAfterDate(String afterDate)
    {       
        logger.debug("DATABASE GATEWAY - COUNT SYSTEM ASSESSMENTS IN PLATFORM AFTER " + afterDate);

        try {
            return client.countSystemAssessmentsAfterDate(afterDate);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return 0;
    }
    */

    public List<ProcessAssessment> listProcessAssessments() {
        logger.debug("DATABASE GATEWAY - LIST PROCESS ASSESSMENTS IN PLATFORM ");

        try {
            return client.listProcessAssessments();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    public List<ProcessAssessment> listProcessAssessmentsForRecipe(String recipeId) {
        logger.debug("DATABASE GATEWAY - LIST PROCESS ASSESSMENTS for recipe " + recipeId + " IN PLATFORM ");

        try {
//            return client.listProcessAssessments(recipeId);
            return client.listProcessAssessmentsForRecipe(recipeId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    public List<ProcessAssessment> listProcessAssessments(String recipeId) {
        return listProcessAssessments(recipeId);
    }
    public List<ProcessAssessment> listProcessAssessmentsForSkill(String skillId) {
        logger.debug("DATABASE GATEWAY - LIST PROCESS ASSESSMENTS for skill " + skillId + " IN PLATFORM ");

        try {
//            return client.listProcessAssessments(recipeId);
            return client.listProcessAssessmentsForSkill(skillId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Recipe> getRecipes() {
        logger.debug("DATABASE GATEWAY - GET RECIPES FROM THE PLATFORM ");

        try {
            return client.getRecipes();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public List<Recipe> getRecipesBySubSystemId(String subSystemId) {
        logger.debug("DATABASE GATEWAY - GET RECIPES BY SUBSYSTEMID FROM THE PLATFORM ");

        try {
            return client.getRecipesBySubSystemId(subSystemId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean putRecipes(Recipe[] recipes) {
        logger.debug("DATABASE GATEWAY - PUT RECIPES IN PLATFORM: " + recipes);
        try {
            return client.putRecipes(recipes);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public List<SubSystem> getSubSystems() {
        logger.debug("DATABASE GATEWAY - GET SUBSYSTEMS FROM THE PLATFORM ");

        try {
            return client.getSubSystems();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    
    @Override
    public ExecutionTable getExecutionTableById(String executionTableId)
    {
        logger.debug("DATABASE GATEWAY - GET EXECUTION TABLE BY ID FROM THE PLATFORM ");

        try {
            return client.loadExecutionTable(executionTableId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }
    
    @Override
    public ExecutionTable getExecutionTableBySubSystemId(String subSystemId)
    {
        logger.debug("DATABASE GATEWAY - GET EXECUTION TABLE BY SUBSYSTEM ID FROM THE PLATFORM ");

        try {
            return client.loadExecutionTableBySubSystemId(subSystemId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;                
    }
    
    @Override
    public boolean updateExecutionTable(ExecutionTable executionTable) {     
        logger.debug("DATABASE GATEWAY - UPDATE EXECUTION TABLE: " + executionTable.toString());
        try
        {
            return client.updateExecutionTable(executionTable);
        }
        catch (Exception e)
        {
            logger.error("DatabaseGateway - updateExecutionTable - database operation error - please check the log file - " + e);
            return false;
        }
    }  
    
    @Override
    public List<ExecutionTable> getExecutionTables()
    {
        logger.debug("DATABASE GATEWAY - GET EXECUTION TABLES FROM THE PLATFORM ");

        try {
            return client.getExecutionTables();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public boolean updateWorkstationRecipes(SubSystemRecipes subSystemRecipes) {
        logger.debug("DATABASE GATEWAY - UPDATE WORKSTATION RECIPES DATA: " + subSystemRecipes.toString());
        return client.updateWorkstationRecipes(subSystemRecipes);
    }

    @Override
    public boolean newProductDefinition(Product product) {
        logger.debug("DATABASE GATEWAY - NEW PRODUCT DEFINITION: " + product);
        return client.newProduct(product);
    }

    @Override
    public List<RecipeExecutionData> getRecipeExecutionData() {
        logger.debug("DATABASE GATEWAY - GET RECIPE EXECUTION DATA FROM THE PLATFORM ");

        try {
            return client.getRecipeExecutionData();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<RecipeExecutionData> getRecipeExecutionData(RecipeExecutionDataFilter recipeExecutionDataFilter) {
        logger.debug("DATABASE GATEWAY - GET RECIPE EXECUTION DATA FROM THE PLATFORM ");

        try {
            return client.getRecipeExecutionData(recipeExecutionDataFilter);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Product getProduct(String productId) {
        logger.debug("DATABASE GATEWAY - GET PRODUCT FROM THE PLATFORM ");

        try {
            return client.loadProduct(productId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public EquipmentObservationRel2 getEquipmentObservationRel2(String equipmentObservationRel2Id) {
        logger.debug("DATABASE GATEWAY - GET EQUIPMENT OBSERVATION REL2 FROM THE PLATFORM ");

        try {
            return client.loadEquipmentObservationRel2(equipmentObservationRel2Id);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }
    @Override
    public EquipmentAssessment getEquipmentAssessment(String equipmentAssessmentId) {
        logger.debug("DATABASE GATEWAY - GET EQUIPMENT ASSESSMENT FROM THE PLATFORM ");

        try {
            return client.loadEquipmentAssessment(equipmentAssessmentId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }
    @Override
    public ProcessAssessment getProcessAssessment(String processAssessmentId) {
        logger.debug("DATABASE GATEWAY - GET PROCESS ASSESSMENT FROM THE PLATFORM ");

        try {
            return client.loadProcessAssessment(processAssessmentId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }
    public PhysicalAdjustment getPhysicalAdjustment(String physicalAdjustmentId) {
        logger.debug("DATABASE GATEWAY - GET PHYSICAL ADJUSTMENT FROM THE PLATFORM ");

        try {
            return client.loadPhysicalAdjustment(physicalAdjustmentId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public OrderInstance getOrderInstance(String orderInstanceId) {
        logger.debug("DATABASE GATEWAY - GET ORDER INSTANCE FROM THE PLATFORM ");

        try {
            return client.loadOrderInstance(orderInstanceId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public Skill getSkill(String skillId) {
        logger.debug("DATABASE GATEWAY - GET SKILL FROM THE PLATFORM ");

        try {
            return client.loadSkill(skillId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public List<ProductInstance> getProductInstancesPerRecipe(String recipeId) {
        logger.debug("DATABASE GATEWAY - GET PRODUCT INSTANCES PER RECIPE FROM THE PLATFORM ");

        try {
            return client.getProductInstancesPerRecipe(recipeId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public List<Recipe> getRecipesPerProductInstance(String productInstanceId) {
        logger.debug("DATABASE GATEWAY - GET RECIPES PER PRODUCT INSTANCE FROM THE PLATFORM ");

        try {
            return client.getRecipesPerProductInstance(productInstanceId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public List<String> getKPISettingNamesPerRecipeExecutionData(RecipeExecutionDataFilter recipeExecutionDataFilter)
    {
        logger.debug("DATABASE GATEWAY - GET KPI SETTING NAMES PER RECIPE FROM THE PLATFORM ");

        try {
            return client.getKPISettingNamesPerRecipeExecutionData(recipeExecutionDataFilter);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;        
    }

    @Override
    public List<OrderInstance> listOrderInstances() {
        logger.debug("DATABASE GATEWAY - LIST ORDER INSTANCES IN PLATFORM ");

        try {
            return client.listOrderInstances();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<TriggeredSkill> listTriggeredSkills(String skillId) {
        logger.debug("DATABASE GATEWAY - LIST TRIGGERED SKILL OPERATIONS IN PLATFORM FOR SKILL " + skillId);

        try {
            return client.listTriggeredSkills(skillId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    @Override
    public List<TriggeredRecipe> listTriggeredRecipes(String recipeId) {
        logger.debug("DATABASE GATEWAY - LIST TRIGGERED RECIPE OPERATIONS IN PLATFORM FOR RECIPE " + recipeId);

        try {
            return client.listTriggeredRecipes(recipeId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<ProductInstance> listProductInstancesNotYetProduced() {
        logger.debug("DATABASE GATEWAY - LIST PRODUCT INSTANCES NOT YET PRODUCED IN PLATFORM ");

        try {
            return client.listProductInstancesNotYetProduced();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<SystemChangeStage> listSystemStageChanges() {
        logger.debug("DATABASE GATEWAY - LIST SYSTEM STAGE CHANGES IN PLATFORM ");

        try {
            return client.listSystemStageChanges();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean newSystemStageChange(SystemChangeStage systemChangeStage) {
        logger.debug("DATABASE GATEWAY - NEW SYSTEM STATUS IN PLATFORM ");

        return client.newSystemStageChange(systemChangeStage);
    }
    
    @Override
    public SystemChangeStage getLastSystemStageChange() {
        logger.debug("DATABASE GATEWAY - GET LAST SYSTEM STAGE CHANGE IN PLATFORM ");

        try
        {
            return client.getLastSystemStageChange();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }

        return null;        
    }

    @Override
    public boolean newPhysicalAdjustment(PhysicalAdjustment physicalAdjustment) {
        logger.debug("DATABASE GATEWAY - NEW PHYSICAL ADJUSTMENT IN PLATFORM: " + physicalAdjustment.toString());
        return client.newPhysicalAdjustment(physicalAdjustment);
    }

    @Override
    public List<PhysicalAdjustment> listPhysicalAdjustments() {
        logger.debug("DATABASE GATEWAY - LIST PHYSICAL ADJUSTMENTS IN PLATFORM ");

        try {
            return client.listPhysicalAdjustments();
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    @Override
    public List<PhysicalAdjustment> listPhysicalAdjustments(String equipmentId) {
        logger.debug("DATABASE GATEWAY - LIST PHYSICAL ADJUSTMENTS for equipment " + equipmentId + " IN PLATFORM ");

        try {
            return client.listPhysicalAdjustments(equipmentId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean newSubSystemStageChange(SubSystemChangeStage subSystemChangeStage) {
        logger.debug("DATABASE GATEWAY - NEW SUBSYSTEM STATUS IN PLATFORM ");

        return client.newSubSystemStageChange(subSystemChangeStage);
    }
    
    @Override
    public SubSystemChangeStage getLastSubSystemStageChange(String subSystemId) {
        logger.debug("DATABASE GATEWAY - GET LAST SUB SYSTEM STATUS CHANGE IN PLATFORM ");

        try
        {
            return client.getLastSubSystemStageChange(subSystemId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }

        return null;        
    }

    @Override
    public List<SubSystemChangeStage> listSubSystemStageChanges(String subSystemId) {
        logger.debug("DATABASE GATEWAY - LIST SUBSYSTEM STATUS IN PLATFORM ");

        try {
            return client.listSubSystemStageChanges(subSystemId);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

}
