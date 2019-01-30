/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.utilities;

/**
 * Agent-cloud constants definition.
 * 
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.ptO_G
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Constants {    
    
    /**
     * Database related constants
     */
    public static final String HISTORY_DB_COLLECTION_RECIPES = "history_recipes";
    public static final String HISTORY_DB_COLLECTION_KPI_SETTINGS = "history_kpi_settings";
    public static final String HISTORY_DB_COLLECTION_PARAMETER_SETTINGS = "history_parameter_settings";

    public static final String DB_COLLECTION_RECIPES = "recipes";
    public static final String DB_COLLECTION_KPI_SETTINGS = "kpi_settings";
    public static final String DB_COLLECTION_RED_KPI_SETTINGS = "red_kpi_settings";
    public static final String DB_COLLECTION_PARAMETER_SETTINGS = "parameter_settings";
    public static final String DB_COLLECTION_RED_PARAMETER_SETTINGS = "red_parameter_settings";
    public static final String DB_COLLECTION_PARAMETERS = "parameters";
    public static final String DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETERS = "physicalAdjustmentParameters";
    
    public static final String DB_COLLECTION_KPIS = "kpis";
    public static final String DB_COLLECTION_SKILLS = "skills";

    public static final String DB_COLLECTION_TRIGGERED_SKILLS = "triggered_skills";    
    public static final String DB_COLLECTION_TRIGGERED_RECIPES = "triggered_recipes";    
    
    // public static final String DB_COLLECTION_PORTS = "ports";
    public static final String DB_COLLECTION_PHYSICAL_PORTS = "physicalPorts";
    public static final String DB_COLLECTION_INFORMATION_PORTS = "informationPorts";
    public static final String DB_COLLECTION_CONTROL_PORTS = "controlPorts";
    public static final String DB_COLLECTION_PARAMETER_PORTS = "parameterPorts";
    
    public static final String DB_COLLECTION_SKILL_REQUIREMENTS = "skill_requirements";
    public static final String DB_COLLECTION_SKILL_TYPES = "skill_types";
    public static final String DB_COLLECTION_ORDERS = "orders";
    public static final String DB_COLLECTION_ORDER_INSTANCES = "order_instances";
    public static final String DB_COLLECTION_ORDER_LINES = "order_lines";
    public static final String DB_COLLECTION_AGENTS = "agents";
    public static final String DB_COLLECTION_PRODUCT_DESCRIPTIONS = "product_descriptions";
    public static final String DB_COLLECTION_COMPONENTS = "components";
    public static final String DB_COLLECTION_PRODUCTS = "products";
    public static final String DB_COLLECTION_PARTS = "parts";
    public static final String DB_COLLECTION_PART_INSTANCES = "part_instances";    
    public static final String DB_COLLECTION_EXECUTION_TABLES = "execution_tables";
    public static final String DB_COLLECTION_EXECUTION_TABLE_ROWS = "execution_table_rows";
    public static final String DB_COLLECTION_EQUIPMENTS = "equipments";
    public static final String DB_COLLECTION_MODULES = "modules";
    public static final String DB_COLLECTION_SUB_SYSTEMS = "sub_systems";
    public static final String DB_COLLECTION_AGENTS_WITH_MISSING_HB = "agents_with_missing_hb";
    public static final String DB_COLLECTION_RAW_EQUIPMENT_DATA = "raw_equipment_data";
    public static final String DB_COLLECTION_RAW_PRODUCT_DATA = "raw_product_data";
    public static final String DB_COLLECTION_UNEXPECTED_PRODUCT_DATA = "unexpected_product_data";    
    public static final String DB_COLLECTION_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA = "product_leaving_workstation_or_transport_data";
    public static final String DB_COLLECTION_PHYSICAL_LOCATIONS = "physical_locations";
    public static final String DB_COLLECTION_RECIPE_EXECUTION_DATA = "recipe_execution_data";

    public static final String DB_COLLECTION_EQUIPMENT_OBSERVATIONS = "equipment_observations";
    public static final String DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2 = "equipment_observation2_rel2";
    public static final String DB_COLLECTION_EQUIPMENT_OBSERVATION_REL2_ROWS = "equipment_observation2_rel2_rows";
    public static final String DB_COLLECTION_EQUIPMENT_ASSESSMENTS = "equipment_assessments";
    public static final String DB_COLLECTION_EQUIPMENT_ASSESSMENT_ROWS = "equipment_assessment_rows";
    public static final String DB_COLLECTION_PROCESS_ASSESSMENTS = "process_assessments";
    public static final String DB_COLLECTION_PROCESS_ASSESSMENT_ROWS = "process_assessment_rows";
    public static final String DB_COLLECTION_SYSTEM_STAGE_CHANGES = "system_stage_changes";        
    public static final String DB_COLLECTION_SUBSYSTEM_STAGE_CHANGES = "subSystem_stage_changes";
    public static final String DB_COLLECTION_PHYSICAL_ADJUSTMENTS = "equipment_adjustments";
    public static final String DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETER_SETTINGS = "physical_adjustment_parameter_settings";

    /**
     * Name of the property into the default.properties file that define the host where the agent cloud jade platform is running.
     * Example:  openmos.agentplatform.host=localhost
     */
    public static final String PLATFORM_HOST_PARAMETER = "openmos.agentplatform.host";

    /**
     * Name of the property into the default.properties file that define the port number where the agent cloud jade platform is listening.
     * Example:  openmos.agentplatform.port=1099
     */
    public static final String PLATFORM_PORT_PARAMETER = "openmos.agentplatform.port";

    /**
     * Name of the property into the default.properties file that define the name of the agent that starts the platform.
     * Example:  openmos.agentplatform.name=openmosPlatformStarter
     */
    public static final String PLATFORM_NAME_PARAMETER = "openmos.agentplatform.name";

    /**
     * Name of the property into the default.properties file that define the current version of the agent-cloud platform.
     * Example:  openmos.agentplatform.release=0.0.1
     */
    public static final String PLATFORM_RELEASE_PARAMETER = "openmos.agentplatform.release";

    /**
     * Name of the property into the default.properties file that define if the Jade graphical console has to be started or not.
     * Default value is false.
     * Example:  openmos.agentplatform.graphical.console=true
     */
    public static final String PLATFORM_GUI_PARAMETER = "openmos.agentplatform.graphical.console";

    /**
     * Name of the property into the default.properties file that define the port number where the agent cloud bootstrap class is listening for shutdown messages.
     * Example:  openmos.agentplatform.shutdown.port=9998
     */
    public static final String PLATFORM_SHUTDOWN_PORT_PARAMETER = "openmos.agentplatform.shutdown.port";

    /**
     * Name of the property into the default.properties file that locates the MSB service for recipes deployment.
     * Example:  openmos.msb.recipesdeployer.ws.endpoint=http://localhost:9997/wsRecipesDeployer
     */
    public static final String MSB_RECIPESDEPLOYMENT_WS_PARAMETER = "openmos.msb.recipesdeployment.ws.endpoint";
    
    /**
     * 
     */
    public static final String MSB_EVENTCONFIRMATION_WS_PARAMETER = "openmos.msb.eventconfirmation.ws.endpoint";

    
    /**
     * The shutdown message sent to the agent cloud bootstrap class to start the shutdown of the entire platform.
     */
    public static final String PLATFORM_SHUTDOWN_MESSAGE = "Bye.";

    /**
     * Name of the property into the default.properties file that locates the agent data web-service,
     * e.g. the service that wraps the agent-cloud database repository.
     * The agent data web-service is currently not in use.
     * Example:  openmos.agent.data.ws.endpoint=http://localhost:7777/wsAgentDataTest1DAO
     */
    public static final String AGENT_DATA_WS_PARAMETER = "openmos.agent.data.ws.endpoint";

    /**
     * Name of the property into the default.properties file that locates the data cloud web-service,
     * e.g. the service that wraps the tracking repository located somewhere in the cloud.
     * The data cloud web-service is currently not in use.
     * Example:  openmos.data.cloud.ws.endpoint=http://localhost:8888/wsDataCloudMessageTracker
     */
    public static final String DATA_SERVICES_WS_PARAMETER = "openmos.data.cloud.ws.endpoint";

    /**
     * Name of the property into the default.properties file that define the logical name of the optimizer to be used.
     * Example:  openmos.agent.productionoptimizer.optimization.algorithm.name=ENERGY_OPTIMIZER
     */
    public static final String PRODUCTION_OPTIMIZER_ALGORITHM_NAME = "openmos.agent.productionoptimizer.optimization.algorithm.name";

    /**
     * Name of the property into the default.properties file that define the main class name of the optimizer to be used.
     * The class must be located into the agent-cloud java classpath at the platform startup time.
     * Example:  openmos.agent.productionoptimizer.optimization.algorithm.class=eu.openmos.energyoptimization.EnergyOptimizer
     */
    public static final String PRODUCTION_OPTIMIZER_ALGORITHM_CLASS_NAME = "openmos.agent.productionoptimizer.optimization.algorithm.class";

    /**
     *
     */
    // public static final String PRODUCTION_OPTIMIZER_ALGORITHM_PARAMETER_NAME = "openmos.agent.productionoptimizer.optimization.algorithm.parameter";

    /**
     *
     */
    // public static final String PRODUCTION_OPTIMIZER_ALGORITHM_PARAMETER_INSTANCE_NAME = "openmos.agent.productionoptimizer.optimization.algorithm.parameter.instance";

    /**
     * Name of the property into the default.properties file that locates the cloud interface web-service,
     * e.g. the entry-point web-service for the platform. This web-service exposes for example calls for agent creation and removal.
     * Example:  openmos.agent.systemconfigurator.ws.endpoint=http://localhost:9999/wsSystemConfigurator
     */
    public static final String AGENT_SYSTEMCONFIGURATOR_WS_PARAMETER = "openmos.agent.systemconfigurator.ws.endpoint";

    /**
     * Name of the property into the default.properties file that locates the shutdown web-service,
     * e.g. the service called by the bootstrap class when the user request the shutdown of the entire platform.
     * The service takes care of a clean agents removal before the platform is shutted down.
     * Example:  openmos.agent.agentplatformkiller.ws.endpoint=http://localhost:9999/wsAgentPlatformKiller
     */
    public static final String AGENT_AGENTPLATFORMKILLER_WS_PARAMETER = "openmos.agent.agentplatformkiller.ws.endpoint";
    
    //Message Ontologies

    /**
     *
     */
    public static final String ONTO_REGISTER_CPA_IN_OPTIMIZER = "cpaRegOpt";

    /**
     *
     */
    public static final String ONTO_PROC_OPT_REQS = "procOptReq";

    /**
     *
     */
    public static final String ONTO_DEPLOY_RECIPES = "depRecipes";

    /**
     *
     */
    public static final String ONTO_DEPLOY_SOP = "sopRecipes";
    
    /**
     * 
     */
    public static final String ONTO_DEPLOY_SUGGEST_RECIPES = "depSugRecipes";

    /**
     *
     */
    public static final String ONTO_MISSED_LB = "missedLB";

    /**
     *
     */
    public static final String ONTO_UPDATE_CPA_PERFORMANCE_DATA = "updateData";

    /**
     *
     */
    public static final String ONTO_LOCATION = "location";

    /**
     *
     */
    public static final String ONTO_APPLIED_RECIPE = "appliedRecipe";

    /**
     *
     */
    public static final String ONTO_PRODUCT_DESCRIPTION = "productDescription";

    /**
     *
     */
    public static final String ONTO_NEWLY_DEPLOYED_ORDER = "deployNewOrder";

    /**
     *
     */
    public static final String ONTO_UPDATED_ORDER = "deployNewOrder";

    /**
     *
     */
    public static final String ONTO_DEPLOY_NEW_ORDER = "deployNewOrder";
    public static final String ONTO_DEPLOY_NEW_PRODUCT_INSTANCE = "deployNewProductInstance";

    /**
     *
     */
    public static final String ONTO_UPDATE_ORDER = "updateOrder";
    
    /**
    * WP4 Cloud Platform Re-work related code.
    * 
    * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
    * @author Valerio Gentile <valerio.gentile@we-plus.eu>
    */
    
     /**
     *
     */
    public static final String ONTO_PROCESS_NEW_RECIPE_ON_THE_SYSTEM = "procNewRecOnTheSys";

    public static final String ONTO_GET_SKILL = "getSkill";
    public static final String ONTO_GET_PRODUCT = "getProduct";
    public static final String ONTO_GET_ORDER_INSTANCE = "getOrderInstance";
    
    public static final String ONTO_GET_RECIPES = "getRecipes";
    public static final String ONTO_GET_RECIPEEXECUTIONDATA = "getRecipeExecutionData";   
    public static final String ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE = "getRecipeExecutionData_productInstancesPerRecipe"; 
    public static final String ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE = "getRecipeExecutionData_recipesPerProductInstance"; 
    public static final String ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE = "getRecipeExecutionData_kpiSettingNamesPerRecipe"; 
    
    public static final String ONTO_GET_RECIPES_BY_SUBSYSTEM_ID = "getRecipesBySubSystemId";    
    public static final String ONTO_PUT_RECIPES = "putRecipes";
    public static final String ONTO_GET_SUBSYSTEMS = "getSubSystems";
    public static final String ONTO_GET_EXECUTION_TABLE_BY_ID = "getExecutionTableById";
    public static final String ONTO_GET_EXECUTION_TABLE_BY_SUBSYSTEM_ID = "getExecutionTableBySubSystemId";
    public static final String ONTO_GET_EXECUTION_TABLES = "getExecutionTables";
    public static final String ONTO_UPDATE_EXECUTION_TABLE = "updateExecutionTable";
    public static final String ONTO_START_EXECUTION_TABLE_UPDATE = "startExecutionTableUpdate";    
    public static final String ONTO_START_SUBSYSTEM_RECIPES_LIST_UPDATE = "startSubSystemRecipesListUpdate";    
    public static final String ONTO_START_SUBSYSTEM_RECIPE_UPDATE = "startSubSystemRecipeUpdate";    
/**
     *
     */
    public static final String ONTO_PROCESS_NEW_SKILL_ON_THE_SYSTEM = "procNewSkillOnTheSys";
    
    /**
     *
     */
    public static final String ONTO_FINISHED_PRODUCT = "finishedProduct";
    public static final String ONTO_STARTED_PRODUCT = "startedProduct";    
    
    /**
     *
     */
    public static final String ONTO_ORDER_REMOVAL = "orderRemoval";

    /**
     * Ontology for unexpected product receival.
     */
    public static String ONTO_UNEXPECTED_PRODUCT = "unexpectedProduct";
     
   /**
     * Ontology for unexpected product receival.
     */
    public static String ONTO_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT = "productLeavingWorkstationOrTransport";
   /**
     * Ontology for new physical location (inotec tracker).
     */
    public static String ONTO_NEW_PHYSICAL_LOCATION = "newPhysicalLocation";
    public static String ONTO_NEW_PRODUCT_DEFINITION = "newProductDefinition";
    /**
     * Ontology for new recipe execution data receival
     */
    public static String ONTO_RECIPE_EXECUTION = "recipeExecution";
   /**
     * Ontology for workstation update
     */
    public static String ONTO_WORKSTATION_INTERNAL_UPDATE = "workstationInternalUpdate";
    public static String ONTO_WORKSTATION_INTERNAL_STAGE_UPDATE = "workstationInternalStageUpdate";    
    public static String ONTO_WORKSTATION_UPDATE = "workstationUpdate";
    public static String ONTO_WORKSTATION_RECIPES_UPDATE = "workstationRecipesUpdate";
    public static String ONTO_PRODUCT_INSTANCE_INTERNAL_UPDATE = "productInstanceInternalUpdate";
    public static String ONTO_PRODUCT_INSTANCE_UPDATE = "productInstanceUpdate";
    
    public static String ONTO_NEW_EQUIPMENT_OBSERVATION = "newEquipmentObservation";
    public static String ONTO_LIST_EQUIPMENT_OBSERVATIONS = "listEquipmentObservations";
    
    public static String ONTO_NEW_EQUIPMENT_OBSERVATION_REL2 = "newEquipmentObservationRel2";
    public static String ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S = "listEquipmentObservationRel2s";
    public static String ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT = "listEquipmentObservationRel2sForEquipment";
    public static String ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_SYSTEM = "listEquipmentObservationRel2sForSystem";
    public static String ONTO_GET_EQUIPMENT_OBSERVATION_REL2 = "getEquipmentObservationRel2";
    
    public static String ONTO_NEW_EQUIPMENT_ASSESSMENT = "newEquipmentAssessment";
    public static String ONTO_LIST_EQUIPMENT_ASSESSMENTS = "listEquipmentAssessments";
    public static String ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT = "listEquipmentAssessmentsForEquipment";
    public static String ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_SYSTEM = "listEquipmentAssessmentsForSystem";
    public static String ONTO_GET_EQUIPMENT_ASSESSMENT = "getEquipmentAssessment";
    
    public static String ONTO_LIST_PROCESS_ASSESSMENTS = "listProcessAssessments";
    public static String ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE = "listProcessAssessmentsForRecipe";
    public static String ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL = "listProcessAssessmentsForSkill";
    public static String ONTO_GET_PROCESS_ASSESSMENT = "getProcessAssessment";
    
    public static String ONTO_NEW_PROCESS_ASSESSMENT = "newProcessAssessment";
    public static String ONTO_LIST_ORDER_INSTANCES = "listOrderInstances";
    public static String ONTO_LIST_PRODUCT_INSTANCES_NOT_YET_PRODUCED = "listProductInstancesNotYetProduced";
    
    public static String ONTO_NEW_SYSTEM_STAGE_CHANGE = "newSystemStageChange";
    public static String ONTO_NEW_SUBSYSTEM_STAGE_CHANGE = "newSubSystemStageChange";
    public static String ONTO_IS_SYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE = "isSystemRampupChangeStagePossible";
    public static String ONTO_IS_SUBSYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE = "isSubSystemRampupChangeStagePossible";
    
    public static String ONTO_NEW_PHYSICAL_ADJUSTMENT = "newProcessAdjustment";
    public static String ONTO_LIST_PHYSICAL_ADJUSTMENTS = "listEquipmentAdjustments";
    public static String ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT = "listEquipmentAdjustmentsForEquipment";
    public static String ONTO_GET_PHYSICAL_ADJUSTMENT= "getPhysicalAdjustment";

    //Deployment Agent

    public static String ONTO_LIST_TRIGGERED_SKILLS = "listTriggeredSkills";
    public static String ONTO_LIST_TRIGGERED_RECIPES = "listTriggeredRecipes";
    public static String ONTO_NEW_TRIGGERED_SKILL = "newTriggeredSkill";
    public static String ONTO_NEW_TRIGGERED_RECIPE = "newTriggeredRecipe";
    
    /**
     * It defines the Jade Directory Facilitator type for the deployment agent.
     */
    public static final String DF_DEPLOYMENT = "deployment_df_service";

    /**
     *
     */
    public static final String DEPLOY_NEW_AGENT = "deploy_new_agent_ontology";

    /**
     *
     */
    public static final String REMOVE_AGENT = "remove_agent_ontology";
    public static final String REMOVE_PRODUCT_AGENT = "remove_product_agent";

    /**
     *
     */
    public static final String REMOVE_AGENT_FROM_OPTIMIZER = "remove_agent_from_optimizer_ontology";
    
    //Product Agent

    /**
     * It defines the Jade Directory Facilitator type for product agents.
     */
    public static final String DF_PRODUCT = "product_df_service";

    /**
     * It defines the Jade Directory Facilitator service name for the products agent.
     */
    public static final String PRODUCT_SERVICE_NAME = "product_service_name";
    
    //TransportAgent

    /**
     * It defines the Jade Directory Facilitator type for transport agents.
     */
    public static final String DF_TRANSPORT = "transport_df_service";
    
    //ResourceAgent

    /**
     * It defines the Jade Directory Facilitator type for resource agents.
     */
    public static final String DF_RESOURCE = "resource_df_service";

    /**
     * It defines the Jade Directory Facilitator service name for resource agents.
     */
    public static final String RESOURCE_SERVICE_NAME = "resource_service_name";

    /**
     *
     */
    public static final String MISSED_LIFE_BEAT = "missed_life_beat_ontology";

    /**
     *
     */
    public static final String NEW_PRODUCT_DETECTED = "new_product_detected_ontology";

    /**
     *
     */
    public static final String EXTRACTED_DATA = "extracted_data_ontology";
    
    //ProductionOptimizer

    /**
     * It defines the Jade Directory Facilitator service name for the production optimizer agent.
     */
    public static final String DF_PRODUCTION_OPTIMIZER = "productionoptimizer_df_service";

    /**
     *
     */
    public static final String TRIGGER_OPTIMIZATION = "trigger_optimization_ontology";
    
    //SystemOptimizer

    /**
     * It defines the Jade Directory Facilitator type for the system optimizer agent.
     */
    // public static final String DF_SYSTEM_OPTIMIZER = "systemoptimizer_df_service";
    
    //Cloud Interface Agent

    /**
     * It defines the Jade Directory Facilitator type for the cloud interface agent.
     */
    public static final String DF_CLOUD_INTERFACE = "cloudinterface_df_service";  
    
    //Platform Starter

    /**
     * It defines the Jade Directory Facilitator type for the platform starter agent.
     */
    public static final String DF_PLATFORMSTARTER = "platformstarter_df_service";  

    /**
     * It defines the Jade Directory Facilitator service name for the platform starter agent.
     */
    public static final String PLATFORM_STARTER_SERVICE_NAME = "platformstarter_service_name";

    /**
     * Fully qualified java name of the class implementing resource agents.
     */
    public static final String RESOURCE_AGENT_CLASS = "eu.openmos.agentcloud.resourceagent.ResourceAgent";

    /**
     * Fully qualified java name of the class implementing product agents.
     */
    public static final String PRODUCT_AGENT_CLASS = "eu.openmos.agentcloud.productagent.ProductAgent";

    /**
     * Fully qualified java name of the class implementing transport agents.
     */
    public static final String TRANSPORT_AGENT_CLASS = "eu.openmos.agentcloud.transportagent.TransportAgent";

    /**
     *
     */
    // public static final String SIMPLETRANSPORT_AGENT_CLASS = "eu.openmos.agentcloud.transportagent.SimpleTransportAgent";

    /**
     *
     */
    // public static final String MULTITRANSPORT_AGENT_CLASS = "eu.openmos.agentcloud.transportagent.MultiTransportAgent";

    /**
     * Fully qualified java name of the class implementing the PlatformLauncherAgent.
     */
    public static final String PLATFORM_LAUNCHER_AGENT_CLASS = "eu.openmos.agentcloud.starter.PlatformLauncherAgent";
    
    /**
     * Descriptive name of the cloud interface agent.
     */
    public static final String CLOUD_INTERFACE_AGENT_NAME = "cloudInterfaceAgent";

    /**
     * Descriptive name of the production optimizer agent.
     */
    public static final String PRODUCTION_OPTIMIZER_AGENT_NAME = "productionOptimizerAgent";

    /**
     * Descriptive name of the system optimizer agent.
     */
    // public static final String SYSTEM_OPTIMIZER_AGENT_NAME = "systemOptimizerAgent";

    /**
     * Descriptive name of the deployment agent.
     */
    public static final String DEPLOYMENT_AGENT_NAME = "deploymentAgent";


    // recipes deployment

    /**
     * It tells the MSB that recipes sent to it are of a "suggested" type.
     */
    public static int RECIPES_SUGGESTION = 1;

    /**
     * It tells the MSB that recipes sent to it are of a "actual" type, e.g. have to be deployed and will be activated.
     */
    public static int RECIPES_ACTUAL = 2;

    /**
     * It tells the MSB that recipes sent to it must be activated.
     */
    public static int RECIPES_ACTIVATION = 3;    
    
    // type of messages received via websocket

    /**
     * It defines messages of type "life beat" for both resource and transport agents, coming from the Manifacturing Service Bus to the agent cloud via p2p communication (websocket).
     */
    public static String MSB_MESSAGE_TYPE_LIFEBEAT = "_MESSAGE_LIFEBEAT_";

    /**
     * It defines messages of type "extracted data" for both resource and transport agents, coming from the Manifacturing Service Bus to the agent cloud via p2p communication (websocket).
     */
    public static String MSB_MESSAGE_TYPE_EXTRACTEDDATA = "_MESSAGE_EXTRACTEDDATA_";

    /**
     * It defines messages of type "new location" for transport agents only, coming from the Manifacturing Service Bus to the agent cloud via p2p communication (websocket).
     */
    public static String MSB_MESSAGE_TYPE_NEWLOCATION = "_MESSAGE_NEWLOCATION_";

    /**
     * It defines messages of type "applied recipes" for resource agents only, coming from the Manifacturing Service Bus to the agent cloud via p2p communication (websocket).
     */
    public static String MSB_MESSAGE_TYPE_APPLIEDRECIPES = "_MESSAGE_APPLIEDRECIPES_";

    /**
     * It defines messages of type "suicide" for both resource and transport agents, coming from the Manifacturing Service Bus to the agent cloud via p2p communication (websocket).
     */
    public static String MSB_MESSAGE_TYPE_SUICIDE = "_SUICIDE_";

    /**
     * It defines a category for messages of an unknown type, coming from the Manifacturing Service Bus to the agent cloud via p2p communication (websocket).
     */
    public static String MSB_MESSAGE_TYPE_UNKNOWN = "_UNKNOWN_";

    public static String MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA = "_MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA_";
    public static String MSB_MESSAGE_TYPE_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT = "_MSB_MESSAGE_TYPE_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_";
    public static String MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA = "_MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA_";
    public static String MSB_MESSAGE_TYPE_WORKSTATION_UPDATE = "_MSB_MESSAGE_TYPE_MSB_MESSAGE_TYPE_WORKSTATION_UPDATE_";    
    public static String MSB_MESSAGE_TYPE_EXECUTION_TABLE_UPDATE = "_MSB_MESSAGE_TYPE_EXECUTION_TABLE_UPDATE_";
}
