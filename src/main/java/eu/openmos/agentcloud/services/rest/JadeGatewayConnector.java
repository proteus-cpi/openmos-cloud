package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.cloudinterfaceagent.data.ExecutionTableDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.OrderInstanceDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.ProductDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.RecipeDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.RecipeExecutionDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.SkillDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.SubSystemDataWrapper;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.services.rest.data.PhysicalAdjustmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentAssessmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentObservationDataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentObservationRel2DataWrapper;
import eu.openmos.agentcloud.services.rest.data.ProcessAssessmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.TriggersDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import jade.core.Profile;
import jade.wrapper.gateway.JadeGateway;
import jade.util.leap.Properties;
import jade.wrapper.gateway.GatewayAgent;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class JadeGatewayConnector extends GatewayAgent {
        private static final Logger logger = Logger.getLogger(JadeGatewayConnector.class);     
        private static JadeGatewayConnector instance = null;

        protected JadeGatewayConnector()
        {
            // Initialize the JadeGateway to connect to the running JADE-based system.
            String PLATFORM_HOST_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_HOST_PARAMETER);
            String PLATFORM_PORT_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_PORT_PARAMETER);

            Properties pp = new Properties();
            pp.setProperty(Profile.MAIN_HOST, PLATFORM_HOST_VALUE);
            pp.setProperty(Profile.MAIN_PORT, PLATFORM_PORT_VALUE);

            JadeGateway.init("eu.openmos.agentcloud.services.rest.MyGatewayAgent", pp);
        }
        
        public static JadeGatewayConnector getInstance()
        {
            if (instance == null)
                instance = new JadeGatewayConnector();
            
            return instance;
        }

    public EquipmentObservationDataWrapper newEquipmentObservation(EquipmentObservationDataWrapper equipmentObservationDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentObservationDataWrapper -> " + equipmentObservationDataWrapper);
            JadeGateway.execute(equipmentObservationDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentObservationDataWrapper -> " + equipmentObservationDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentObservationDataWrapper;
    }
    
    public TriggersDataWrapper newTriggeredSkill(TriggersDataWrapper triggersDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            JadeGateway.execute(triggersDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return triggersDataWrapper;
    }
    
    public TriggersDataWrapper newTriggeredRecipe(TriggersDataWrapper triggersDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            JadeGateway.execute(triggersDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return triggersDataWrapper;
    }

    public TriggersDataWrapper listTriggeredSkills(TriggersDataWrapper triggersDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            JadeGateway.execute(triggersDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return triggersDataWrapper;
    }    
    
    public TriggersDataWrapper listTriggeredRecipes(TriggersDataWrapper triggersDataWrapper)
    {
        boolean status = false;
        try
        {
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            JadeGateway.execute(triggersDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> triggersDataWrapper -> " + triggersDataWrapper);
            status = true;            
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return triggersDataWrapper;
    }            
    
    public EquipmentObservationDataWrapper listEquipmentObservations(EquipmentObservationDataWrapper equipmentObservationDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentObservationDataWrapper -> " + equipmentObservationDataWrapper);
            JadeGateway.execute(equipmentObservationDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentObservationDataWrapper -> " + equipmentObservationDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentObservationDataWrapper;
    }
        
    public EquipmentObservationRel2DataWrapper newEquipmentObservationRel2(EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentObservationRel2DataWrapper -> " + equipmentObservationRel2DataWrapper);
            JadeGateway.execute(equipmentObservationRel2DataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentObservationRel2DataWrapper -> " + equipmentObservationRel2DataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentObservationRel2DataWrapper;
    }
        
    public EquipmentAssessmentDataWrapper newEquipmentAssessment(EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + equipmentAssessmentDataWrapper);
            JadeGateway.execute(equipmentAssessmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentObservationRel2DataWrapper -> " + equipmentAssessmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentAssessmentDataWrapper;
    }

    public PhysicalAdjustmentDataWrapper newPhysicalAdjustment(PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAdjustmentDataWrapper -> " + physicalAdjustmentDataWrapper);
            JadeGateway.execute(physicalAdjustmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAdjustmentDataWrapper -> " + physicalAdjustmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return physicalAdjustmentDataWrapper;
    }

    public ProcessAssessmentDataWrapper newProcessAssessment(ProcessAssessmentDataWrapper processAssessmentDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> processAssessmentDataWrapper -> " + processAssessmentDataWrapper);
            JadeGateway.execute(processAssessmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> processAssessmentDataWrapper -> " + processAssessmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return processAssessmentDataWrapper;
    }

    public EquipmentObservationRel2DataWrapper listEquipmentObservationRel2s(EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentObservationRel2DataWrapper -> " + equipmentObservationRel2DataWrapper);
            JadeGateway.execute(equipmentObservationRel2DataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentObservationRel2DataWrapper -> " + equipmentObservationRel2DataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentObservationRel2DataWrapper;
    }
        
    public EquipmentObservationRel2DataWrapper getEquipmentObservationRel2(EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentObservationRel2DataWrapper -> " + equipmentObservationRel2DataWrapper);
            JadeGateway.execute(equipmentObservationRel2DataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentObservationRel2DataWrapper -> " + equipmentObservationRel2DataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentObservationRel2DataWrapper;
    }
        
    public OrderInstanceDataWrapper listOrderInstances(OrderInstanceDataWrapper orderInstanceDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> orderInstanceDataWrapper -> " + orderInstanceDataWrapper);
            JadeGateway.execute(orderInstanceDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> orderInstanceDataWrapper -> " + orderInstanceDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return orderInstanceDataWrapper;
    }
        
    public OrderInstanceDataWrapper listProductInstancesNotYetProduced(OrderInstanceDataWrapper orderInstanceDataWrapper)
    {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> orderInstanceDataWrapper -> " + orderInstanceDataWrapper);
            JadeGateway.execute(orderInstanceDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> orderInstanceDataWrapper -> " + orderInstanceDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return orderInstanceDataWrapper;
    }
        
    public static void main(String[] args) {
        logger.debug("inizio main");
        JadeGatewayConnector jgc = JadeGatewayConnector.getInstance();

        // LOAD TEST
        EquipmentObservationDataWrapper equipmentObservationDataWrapper = new EquipmentObservationDataWrapper();
        equipmentObservationDataWrapper.setOntology(Constants.ONTO_LIST_EQUIPMENT_OBSERVATIONS);
        equipmentObservationDataWrapper = jgc.newEquipmentObservation(equipmentObservationDataWrapper);
            
        // logger.debug("output dell'operazione (nel connector)" + equipmentObservationDataWrapper.getEquipmentObservations());
        logger.debug("output dell'operazione (nel connector)" + equipmentObservationDataWrapper.getEquipmentObservationsArrayList());

/*        
        // INSERT TEST
        for (int i = 0; i < 10; i++)
        {
            EquipmentObservationDataWrapper equipmentObservationDataWrapper = new EquipmentObservationDataWrapper();
            EquipmentObservation eo = new EquipmentObservation();
            eo.setUniqueId(i + "-" + (new Date()).toString());
            eo.setDescription("PROVA VALE DA MAIN - " + i);
            eo.setRegistered(new Date());
            equipmentObservationDataWrapper.setEquipmentObservation(eo);
            equipmentObservationDataWrapper.setOntology(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION);
            equipmentObservationDataWrapper = jgc.newEquipmentObservation(equipmentObservationDataWrapper);
            
            logger.debug("output dell'operazione (nel connector)" + equipmentObservationDataWrapper.getMessage() );
        }
*/        
        logger.debug("fine main");
        System.exit(0);
    }

    public RecipeDataWrapper getRecipes(RecipeDataWrapper recipeDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> recipeDataWrapper -> " + recipeDataWrapper);
            JadeGateway.execute(recipeDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> recipeDataWrapper -> " + recipeDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return recipeDataWrapper;
    }
    
    public SubSystemDataWrapper getSubSystems(SubSystemDataWrapper subSystemDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> subSystemDataWrapper -> " + subSystemDataWrapper);
            JadeGateway.execute(subSystemDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> subSystemDataWrapper -> " + subSystemDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return subSystemDataWrapper;
    }
    
    public ExecutionTableDataWrapper getExecutionTables(ExecutionTableDataWrapper executionTableDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> executionTableDataWrapper -> " + executionTableDataWrapper);
            JadeGateway.execute(executionTableDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> executionTableDataWrapper -> " + executionTableDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return executionTableDataWrapper;
    }
    
    public ExecutionTableDataWrapper getExecutionTableById(ExecutionTableDataWrapper executionTableDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> executionTableDataWrapper -> " + executionTableDataWrapper);
            JadeGateway.execute(executionTableDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> executionTableDataWrapper -> " + executionTableDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return executionTableDataWrapper;
    }
    
    public ExecutionTableDataWrapper getExecutionTableBySubSystemId(ExecutionTableDataWrapper executionTableDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> executionTableDataWrapper -> " + executionTableDataWrapper);
            JadeGateway.execute(executionTableDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> executionTableDataWrapper -> " + executionTableDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return executionTableDataWrapper;
    }
        
  public RecipeDataWrapper getRecipesBySubSystemId(RecipeDataWrapper recipeDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> recipeDataWrapper -> " + recipeDataWrapper);
            JadeGateway.execute(recipeDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> recipeDataWrapper -> " + recipeDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return recipeDataWrapper;
    }
  
    public RecipeExecutionDataWrapper getRecipeExecutionData(RecipeExecutionDataWrapper recipeExecutionDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> recipeExecutionDataWrapper -> " + recipeExecutionDataWrapper);
            JadeGateway.execute(recipeExecutionDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> recipeExecutionDataWrapper -> " + recipeExecutionDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return recipeExecutionDataWrapper;
    }
    
    public SkillDataWrapper getSkill(SkillDataWrapper skillDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> skillDataWrapper -> " + skillDataWrapper);
            JadeGateway.execute(skillDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> skillDataWrapper -> " + skillDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return skillDataWrapper;
    }

    public ProductDataWrapper getProduct(ProductDataWrapper productDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> productDataWrapper -> " + productDataWrapper);
            JadeGateway.execute(productDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> productDataWrapper -> " + productDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return productDataWrapper;
    }

    public OrderInstanceDataWrapper getOrderInstance(OrderInstanceDataWrapper orderInstanceDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> orderInstanceDataWrapper -> " + orderInstanceDataWrapper);
            JadeGateway.execute(orderInstanceDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> orderInstanceDataWrapper -> " + orderInstanceDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return orderInstanceDataWrapper;
    }

    public EquipmentAssessmentDataWrapper listEquipmentAssessments(EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + equipmentAssessmentDataWrapper);
            JadeGateway.execute(equipmentAssessmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + equipmentAssessmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentAssessmentDataWrapper;
    }

    public EquipmentAssessmentDataWrapper getEquipmentAssessment(EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + equipmentAssessmentDataWrapper);
            JadeGateway.execute(equipmentAssessmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + equipmentAssessmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentAssessmentDataWrapper;
    }

    public PhysicalAdjustmentDataWrapper listPhysicalAdjustments(PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAdjustmentDataWrapper -> " + physicalAdjustmentDataWrapper);
            JadeGateway.execute(physicalAdjustmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAdjustmentDataWrapper -> " + physicalAdjustmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return physicalAdjustmentDataWrapper;
    }

    public PhysicalAdjustmentDataWrapper getPhysicalAdjustment(PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAdjustmentDataWrapper -> " + physicalAdjustmentDataWrapper);
            JadeGateway.execute(physicalAdjustmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAdjustmentDataWrapper -> " + physicalAdjustmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return physicalAdjustmentDataWrapper;
    }

    public ProcessAssessmentDataWrapper listProcessAssessments(ProcessAssessmentDataWrapper processAssessmentDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + processAssessmentDataWrapper);
            JadeGateway.execute(processAssessmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + processAssessmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return processAssessmentDataWrapper;
    }
    public ProcessAssessmentDataWrapper getProcessAssessment(ProcessAssessmentDataWrapper processAssessmentDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + processAssessmentDataWrapper);
            JadeGateway.execute(processAssessmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + processAssessmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return processAssessmentDataWrapper;
    }

    public EquipmentAssessmentDataWrapper checkIfSystemRampUpChangeStagePossible(EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + equipmentAssessmentDataWrapper);
            JadeGateway.execute(equipmentAssessmentDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> equipmentAssessmentDataWrapper -> " + equipmentAssessmentDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return equipmentAssessmentDataWrapper;
    }

    public SubSystemDataWrapper checkIfSubSystemRampUpChangeStagePossible(SubSystemDataWrapper subSystemDataWrapper) {
        boolean status = false;
        try 
        {                        
            logger.debug("dentro il jade gateway connector, appena PRIMA di chiamare l'agente mygatewayagent -> subSystemDataWrapper -> " + subSystemDataWrapper);
            JadeGateway.execute(subSystemDataWrapper);
            logger.debug("dentro il jade gateway connector, appena DOPO di chiamare l'agente mygatewayagent -> subSystemDataWrapper -> " + subSystemDataWrapper);
            status = true;
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return subSystemDataWrapper;
    }
}