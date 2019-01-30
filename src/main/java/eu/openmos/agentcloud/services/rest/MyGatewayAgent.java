package eu.openmos.agentcloud.services.rest;

import eu.openmos.agentcloud.cloudinterfaceagent.data.ExecutionTableDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.OrderInstanceDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.ProductDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.RecipeDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.RecipeExecutionDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.SkillDataWrapper;
import eu.openmos.agentcloud.cloudinterfaceagent.data.SubSystemDataWrapper;
import eu.openmos.agentcloud.services.rest.data.PhysicalAdjustmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentObservationDataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentObservationRel2DataWrapper;
import eu.openmos.agentcloud.services.rest.data.EquipmentAssessmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.ProcessAssessmentDataWrapper;
import eu.openmos.agentcloud.services.rest.data.TriggersDataWrapper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.EquipmentAdjustment;
import eu.openmos.model.EquipmentAssessment;
import eu.openmos.model.EquipmentObservation;
import eu.openmos.model.EquipmentObservationRel2;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.PhysicalAdjustment;
import eu.openmos.model.ProcessAssessment;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.RecipeExecutionDataFilter;
import eu.openmos.model.Skill;
import eu.openmos.model.SubSystem;
import eu.openmos.model.TriggeredRecipe;
import eu.openmos.model.TriggeredSkill;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.gateway.GatewayAgent;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class MyGatewayAgent extends GatewayAgent {
    private static final Logger logger = Logger.getLogger(MyGatewayAgent.class);   
    EquipmentObservationDataWrapper equipmentObservationDataWrapper;
    EquipmentObservationRel2DataWrapper equipmentObservationRel2DataWrapper;
    EquipmentAssessmentDataWrapper equipmentAssessmentDataWrapper;
    PhysicalAdjustmentDataWrapper physicalAdjustmentDataWrapper;
    ProcessAssessmentDataWrapper processAssessmentDataWrapper;
    RecipeDataWrapper recipeDataWrapper;
    SubSystemDataWrapper subSystemDataWrapper;
    ExecutionTableDataWrapper executionTableDataWrapper;
    RecipeExecutionDataWrapper recipeExecutionDataWrapper;
    SkillDataWrapper skillDataWrapper;
    ProductDataWrapper productDataWrapper;
    OrderInstanceDataWrapper orderInstanceDataWrapper;
    TriggersDataWrapper triggersDataWrapper;

    @Override
    protected void processCommand(Object obj) 
    {
        if (obj instanceof EquipmentObservationDataWrapper) 
            send(processEquipmentObservation(obj));
        else if (obj instanceof EquipmentObservationRel2DataWrapper) 
            send(processEquipmentObservationRel2(obj));
        else if (obj instanceof EquipmentAssessmentDataWrapper) 
            send(processEquipmentAssessment(obj));
        else if (obj instanceof PhysicalAdjustmentDataWrapper) 
            send(processPhysicalAdjustment(obj));
        else if (obj instanceof ProcessAssessmentDataWrapper) 
            send(processProcessAssessment(obj));
        else if (obj instanceof RecipeDataWrapper) 
            send(processRecipe(obj));
        else if (obj instanceof SubSystemDataWrapper) 
            send(processSubSystem(obj));
        else if (obj instanceof ExecutionTableDataWrapper) 
            send(processExecutionTable(obj));
        else if (obj instanceof RecipeExecutionDataWrapper) 
            send(processRecipeExecutionData(obj));
        else if (obj instanceof SkillDataWrapper) 
            send(processSkill(obj));
        else if (obj instanceof ProductDataWrapper) 
            send(processProduct(obj));
        else if (obj instanceof OrderInstanceDataWrapper) 
            send(processOrderInstance(obj));
        else if (obj instanceof TriggersDataWrapper)
            send(processTriggers(obj));
    }

    @Override
    protected void setup() {
        super.setup();
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action() {
                ACLMessage msg = receive();

                if (msg!=null)
                {
//                    if (equipmentObservationDataWrapper!=null) 
                    if (msg.getOntology().equals(Constants.ONTO_LIST_EQUIPMENT_OBSERVATIONS)
                            || msg.getOntology().equals(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION))
                    {
                        actionEquipmentObservation(msg);
                        releaseCommand(equipmentObservationDataWrapper);
                    } 
                    else if (msg.getOntology().equals(Constants.ONTO_LIST_TRIGGERED_SKILLS)
                            || msg.getOntology().equals(Constants.ONTO_NEW_TRIGGERED_SKILL)
                            || msg.getOntology().equals(Constants.ONTO_LIST_TRIGGERED_RECIPES)
                            || msg.getOntology().equals(Constants.ONTO_NEW_TRIGGERED_RECIPE))
                    {
                        actionTriggers(msg);
                        releaseCommand(triggersDataWrapper);
                    }
//                    else if (equipmentObservationRel2DataWrapper!=null) 
                    else if (msg.getOntology().equals(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S)
                            || msg.getOntology().equals(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT)
                            || msg.getOntology().equals(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_SYSTEM)
                            || msg.getOntology().equals(Constants.ONTO_GET_EQUIPMENT_OBSERVATION_REL2)
                            || msg.getOntology().equals(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION_REL2))
                    {
                        actionEquipmentObservationRel2(msg);
                        releaseCommand(equipmentObservationRel2DataWrapper);
                    } 
                    else if (msg.getOntology().equals(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS)
                            || msg.getOntology().equals(Constants.ONTO_NEW_EQUIPMENT_ASSESSMENT) 
                            || msg.getOntology().equals(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT)
                            || msg.getOntology().equals(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_SYSTEM)
                            || msg.getOntology().equals(Constants.ONTO_GET_EQUIPMENT_ASSESSMENT)
                            || msg.getOntology().equals(Constants.ONTO_IS_SYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE))
                    {
                        actionEquipmentAssessment(msg);
                        releaseCommand(equipmentAssessmentDataWrapper);
                    } 
                    else if (msg.getOntology().equals(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS)
                            || msg.getOntology().equals(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT)
                            || msg.getOntology().equals(Constants.ONTO_GET_PHYSICAL_ADJUSTMENT)
                            || msg.getOntology().equals(Constants.ONTO_NEW_PHYSICAL_ADJUSTMENT))
                    {
                        actionPhysicalAdjustment(msg);
                        releaseCommand(physicalAdjustmentDataWrapper);
                    } 
                    else if (msg.getOntology().equals(Constants.ONTO_LIST_PROCESS_ASSESSMENTS)
                            || msg.getOntology().equals(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE)
                            || msg.getOntology().equals(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL)
                            || msg.getOntology().equals(Constants.ONTO_GET_PROCESS_ASSESSMENT)
                            || msg.getOntology().equals(Constants.ONTO_NEW_PROCESS_ASSESSMENT))
                    {
                        actionProcessAssessment(msg);
                        releaseCommand(processAssessmentDataWrapper);
                    } 
//                    else if (recipeDataWrapper!=null)
                    else if (msg.getOntology().equals(Constants.ONTO_GET_RECIPES_BY_SUBSYSTEM_ID) 
                            || msg.getOntology().equals(Constants.ONTO_GET_RECIPES) )
                    {
                        actionRecipe(msg);
                        releaseCommand(recipeDataWrapper);
                    }
                    else if (msg.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA) 
                            || msg.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE)
                            || msg.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE)
                            || msg.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE))
                    {
                        actionRecipeExecutionData(msg);
                        releaseCommand(recipeExecutionDataWrapper);
                    }
//                    else if (subSystemDataWrapper!=null) 
                    else if (msg.getOntology().equals(Constants.ONTO_GET_SUBSYSTEMS)
                            || msg.getOntology().equals(Constants.ONTO_IS_SUBSYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE))
                    {
                        actionSubSystem(msg);
                        releaseCommand(subSystemDataWrapper);
                    }
//                    else if (executionTableDataWrapper!=null)
                    else if (msg.getOntology().equals(Constants.ONTO_GET_EXECUTION_TABLE_BY_SUBSYSTEM_ID) 
                            || msg.getOntology().equals(Constants.ONTO_GET_EXECUTION_TABLE_BY_ID) 
                            || msg.getOntology().equals(Constants.ONTO_GET_EXECUTION_TABLES))
                    {
                        actionExecutionTable(msg);
                        releaseCommand(executionTableDataWrapper);
                    }
//                    else if (skillDataWrapper!=null)
                    else if (msg.getOntology().equals(Constants.ONTO_GET_SKILL))
                    {
                        actionSkill(msg);
                        releaseCommand(skillDataWrapper);
                    }
//                    else if (productDataWrapper!=null)
                    else if (msg.getOntology().equals(Constants.ONTO_GET_PRODUCT))
                    {
                        actionProduct(msg);
                        releaseCommand(productDataWrapper);
                    }
//                    else if (orderInstanceDataWrapper!=null)
                    else if (msg.getOntology().equals(Constants.ONTO_GET_ORDER_INSTANCE) ||
                            msg.getOntology().equals(Constants.ONTO_LIST_ORDER_INSTANCES) || 
                            msg.getOntology().equals(Constants.ONTO_LIST_PRODUCT_INSTANCES_NOT_YET_PRODUCED))
                    {
                        actionOrderInstance(msg);
                        releaseCommand(orderInstanceDataWrapper);
                    }
                    else 
                        block();
                } 
                else 
                    block();
            }
        });
        
    }
    
    private ACLMessage processEquipmentObservation(Object obj)
    {
        logger.debug("MyGatewayAgent - processCommand - case of EquipmentObservationDataWrapper " );
        equipmentObservationDataWrapper = (EquipmentObservationDataWrapper) obj;

//        EquipmentObservation eo = equipmentObservationDataWrapper.getEquipmentObservation();
//        String actionRequested = equipmentObservationDataWrapper.getOntology();

       logger.debug("dentro l'agente mygatewayagent -> action = " + equipmentObservationDataWrapper.getOntology() + " -  eo -> " + equipmentObservationDataWrapper.getEquipmentObservation());

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
        msg.setOntology(equipmentObservationDataWrapper.getOntology());
        if (equipmentObservationDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION))
        {
            try {
                msg.setContentObject(equipmentObservationDataWrapper.getEquipmentObservation());
            } catch (IOException ex) {
                logger.error(ex);            
            }
        }
        return msg;
    }
    
    private ACLMessage processTriggers(Object obj)
    {
        logger.debug("MyGatewayAgent - processCommand - case of TriggersDataWrapper ");
        triggersDataWrapper = (TriggersDataWrapper) obj;
        
        logger.debug("dentro l'agente mygatewayagent -> action = " + triggersDataWrapper.getOntology() + " -> eo -> " + triggersDataWrapper);
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("productionOptimizerAgent", AID.ISLOCALNAME));
        msg.setOntology(triggersDataWrapper.getOntology());
        if (triggersDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_TRIGGERED_SKILL))
        {
            try
            {
                msg.setContentObject(triggersDataWrapper.getTriggeredSkill());               
            }
            catch (IOException ex) {
                logger.error(ex);
            }
        }
        else if (triggersDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_TRIGGERED_RECIPE))
        {
            try
            {
                msg.setContentObject(triggersDataWrapper.getTriggeredRecipe());
            }
            catch (IOException ex) {
                logger.error(ex);
            }
            
        }
        else if (triggersDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_TRIGGERED_SKILLS))
        {
            try 
            {
                msg.setContentObject(triggersDataWrapper.getTriggeredSkillId());
            }
            catch (IOException ex) {
                logger.error(ex);
            }
        }
        else if (triggersDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_TRIGGERED_RECIPES))
        {
            try
            {
                msg.setContentObject(triggersDataWrapper.getTriggeredRecipeId());
            }
            catch (IOException ex)
            {
                logger.error(ex);
            }
        }
        return msg;
    }

    private ACLMessage processEquipmentObservationRel2(Object obj)
    {
            logger.debug("MyGatewayAgent - processCommand - case of EquipmentObservationRel2DataWrapper " );
            equipmentObservationRel2DataWrapper = (EquipmentObservationRel2DataWrapper) obj;

           logger.debug("dentro l'agente mygatewayagent -> action = " + equipmentObservationRel2DataWrapper.getOntology() + " -  eo -> " + equipmentObservationRel2DataWrapper.getEquipmentObservationRel2());
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
            msg.setOntology(equipmentObservationRel2DataWrapper.getOntology());
            if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION_REL2))
            {
                try {
                    msg.setContentObject(equipmentObservationRel2DataWrapper.getEquipmentObservationRel2());
                } catch (IOException ex) {
                    logger.error(ex);            
                }
            } else if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S))
            {
                // nothing
            } else if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT))
            {
                msg.setContent(equipmentObservationRel2DataWrapper.getEquipmentId());
            } else if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_SYSTEM))
            {
                // nothing 
            } else if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EQUIPMENT_OBSERVATION_REL2))
            {
                msg.setContent(equipmentObservationRel2DataWrapper.getEquipmentObservationRel2Id());
            }
            return msg;
    }

    private ACLMessage processEquipmentAssessment(Object obj)
    {
            logger.debug("MyGatewayAgent - processCommand - case of EquipmentAssessmentDataWrapper " );
            equipmentAssessmentDataWrapper = (EquipmentAssessmentDataWrapper) obj;

           logger.debug("dentro l'agente mygatewayagent -> action = " + equipmentAssessmentDataWrapper.getOntology() + " -  eo -> " + equipmentAssessmentDataWrapper.getEquipmentAssessment());
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
            msg.setOntology(equipmentAssessmentDataWrapper.getOntology());
            if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_EQUIPMENT_ASSESSMENT))
            {
                try {
                    msg.setContentObject(equipmentAssessmentDataWrapper.getEquipmentAssessment());
                } catch (IOException ex) {
                    logger.error(ex);            
                }
            } else if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_IS_SYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE))
            {
                // nothing
            } else if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS))
            {
                // nothing
            } else if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_SYSTEM))
            {
                // nothing
            } else if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT))
            {
                msg.setContent(equipmentAssessmentDataWrapper.getEquipmentId());
            } else if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EQUIPMENT_ASSESSMENT))
            {
                msg.setContent(equipmentAssessmentDataWrapper.getEquipmentAssessmentId());
            }
            return msg;
    }

    private ACLMessage processPhysicalAdjustment(Object obj)
    {
            logger.debug("MyGatewayAgent - processCommand - case of PhysicalAdjustmentDataWrapper " );
            physicalAdjustmentDataWrapper = (PhysicalAdjustmentDataWrapper) obj;

           logger.debug("dentro l'agente mygatewayagent -> action = " + physicalAdjustmentDataWrapper.getOntology() + " -  eo -> " + physicalAdjustmentDataWrapper.getPhysicalAdjustment());
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
            msg.setOntology(physicalAdjustmentDataWrapper.getOntology());
            if (physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_PHYSICAL_ADJUSTMENT))
            {
                try {
                    msg.setContentObject(physicalAdjustmentDataWrapper.getPhysicalAdjustment());
                } catch (IOException ex) {
                    logger.error(ex);            
                }
            } else if (physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS))
            {
                // nothing
            } else if (physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT))
            {
                msg.setContent(physicalAdjustmentDataWrapper.getEquipmentId());
            } else if (physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_PHYSICAL_ADJUSTMENT))
            {
                msg.setContent(physicalAdjustmentDataWrapper.getPhysicalAdjustmentId());
            }

            return msg;
    }

    private ACLMessage processProcessAssessment(Object obj)
    {
            logger.debug("MyGatewayAgent - processCommand - case of ProcessAssessmentDataWrapper " );
            processAssessmentDataWrapper = (ProcessAssessmentDataWrapper) obj;

           logger.debug("dentro l'agente mygatewayagent -> action = " + processAssessmentDataWrapper.getOntology() + " -  eo -> " + processAssessmentDataWrapper.getProcessAssessment());
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
            msg.setOntology(processAssessmentDataWrapper.getOntology());
            if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_PROCESS_ASSESSMENT))
            {
                try {
                    msg.setContentObject(processAssessmentDataWrapper.getProcessAssessment());
                } catch (IOException ex) {
                    logger.error(ex);            
                }
            } else if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS))
            {
                // nothing
            } else if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE))
            {
                msg.setContent(processAssessmentDataWrapper.getRecipeId());
            } else if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL))
            {
                msg.setContent(processAssessmentDataWrapper.getSkillId());
            } else if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_PROCESS_ASSESSMENT))
            {
                msg.setContent(processAssessmentDataWrapper.getProcessAssessmentId());
            }
            return msg;
    }

    private ACLMessage processRecipe(Object obj)
    {
            logger.debug("MyGatewayAgent - processCommand - case of RecipeDataWrapper " );
            
            recipeDataWrapper = (RecipeDataWrapper) obj;

//            Recipe[] recipes = recipeDataWrapper.getRecipes();
//            String subSystemId = recipeDataWrapper.getSubSystemId();
//            String actionRequested = recipeDataWrapper.getOntology();
            
           logger.debug("dentro l'agente mygatewayagent -> action = " + recipeDataWrapper.getOntology() + " -  eo -> " + recipeDataWrapper.getSubSystemId());
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
            msg.setOntology(recipeDataWrapper.getOntology());
            if (recipeDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_RECIPES_BY_SUBSYSTEM_ID))
                msg.setContent(recipeDataWrapper.getSubSystemId());

            return msg;        
    }

    private ACLMessage processSubSystem(Object obj)
    {
            logger.debug("MyGatewayAgent - processCommand - case of SubSystemDataWrapper " );
            
            subSystemDataWrapper = (SubSystemDataWrapper) obj;

//            SubSystem[] subSystems = subSystemDataWrapper.getSubSystems();
            String actionRequested = subSystemDataWrapper.getOntology();
            
           logger.debug("dentro l'agente mygatewayagent -> action = " + subSystemDataWrapper.getOntology());
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
            msg.setOntology(subSystemDataWrapper.getOntology());
            
            if (subSystemDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_IS_SUBSYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE))
                msg.setContent(subSystemDataWrapper.getSubSystemId());

            return msg;
    }

    private ACLMessage processExecutionTable(Object obj)
    {
            logger.debug("MyGatewayAgent - processCommand - case of ExecutionTableDataWrapper " );
            
            executionTableDataWrapper = (ExecutionTableDataWrapper) obj;

//            ExecutionTable[] executionTables = executionTableDataWrapper.getExecutionTables();
//            ExecutionTable executionTableById = executionTableDataWrapper.getExecutionTableById();
//            ExecutionTable executionTableBySubSystemId = executionTableDataWrapper.getExecutionTableBySubSystemId();
//            String executionTableId = executionTableDataWrapper.getExecutionTableId();
//            String subSystemId = executionTableDataWrapper.getSubSystemId();
//            String actionRequested = executionTableDataWrapper.getOntology();
            
           logger.debug("dentro l'agente mygatewayagent -> action = " + executionTableDataWrapper.getOntology());
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
            msg.setOntology(executionTableDataWrapper.getOntology());
            
            if (executionTableDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EXECUTION_TABLE_BY_ID))
                msg.setContent(executionTableDataWrapper.getExecutionTableId());
            else if (executionTableDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EXECUTION_TABLE_BY_SUBSYSTEM_ID))
                msg.setContent(executionTableDataWrapper.getSubSystemId());

            return msg;
    }

    private ACLMessage processRecipeExecutionData(Object obj)
    {
        logger.debug("MyGatewayAgent - processCommand - case of RecipeExecutionDataWrapper " );

        recipeExecutionDataWrapper = (RecipeExecutionDataWrapper) obj;

       logger.debug("dentro l'agente mygatewayagent -> action = " + recipeExecutionDataWrapper.getOntology());

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
        msg.setOntology(recipeExecutionDataWrapper.getOntology());

        if (recipeExecutionDataWrapper.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA))
        {
            RecipeExecutionDataFilter myFilter = recipeExecutionDataWrapper.getRecipeExecutionDataFilter();
            logger.debug("dentro l'agente mygatewayagent -> filter = " + myFilter);
            if (myFilter != null)
            {
                try {
                    msg.setContentObject(myFilter);
                } catch (IOException ex) {
                    logger.error(ex.getLocalizedMessage());
                }
            }
        }
        else if (recipeExecutionDataWrapper.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE))
        {
            RecipeExecutionDataFilter myFilter = recipeExecutionDataWrapper.getRecipeExecutionDataFilter();
            logger.debug("dentro l'agente mygatewayagent -> filter = " + myFilter);
            if (myFilter != null)
            {
                msg.setContent(myFilter.getRecipeId());
            }
        }
        else if (recipeExecutionDataWrapper.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE))
        {
            RecipeExecutionDataFilter myFilter = recipeExecutionDataWrapper.getRecipeExecutionDataFilter();
            logger.debug("dentro l'agente mygatewayagent -> filter = " + myFilter);
            if (myFilter != null)
            {
                msg.setContent(myFilter.getProductInstanceId());
            }
        }
        else if (recipeExecutionDataWrapper.getOntology().equals(Constants.ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE))
        {
            RecipeExecutionDataFilter myFilter = recipeExecutionDataWrapper.getRecipeExecutionDataFilter();
            logger.debug("dentro l'agente mygatewayagent -> filter = " + myFilter);
            if (myFilter != null)
            {
                try {
                    msg.setContentObject(myFilter);
                } catch (IOException ex) {
                    logger.error(ex.getLocalizedMessage());
                }
            }
        }

        return msg;
    }
    
    private ACLMessage processSkill(Object obj)
    {
        logger.debug("MyGatewayAgent - processCommand - case of SkillDataWrapper " );

        skillDataWrapper = (SkillDataWrapper) obj;

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
        msg.setOntology(skillDataWrapper.getOntology());
        msg.setContent(skillDataWrapper.getSkillId());
        
        logger.debug("dentro l'agente mygatewayagent -> msg = " + msg);
        
        return msg;
    }
    
    private ACLMessage processProduct(Object obj)
    {
        logger.debug("MyGatewayAgent - processCommand - case of ProductDataWrapper " );

        productDataWrapper = (ProductDataWrapper) obj;

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
        msg.setOntology(productDataWrapper.getOntology());
        msg.setContent(productDataWrapper.getProductId());
        
        logger.debug("dentro l'agente mygatewayagent -> msg = " + msg);
        
        return msg;
    }
    
    private ACLMessage processOrderInstance(Object obj)
    {
        logger.debug("MyGatewayAgent - processCommand - case of OrderInstanceDataWrapper " );

        orderInstanceDataWrapper = (OrderInstanceDataWrapper) obj;

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID( "productionOptimizerAgent", AID.ISLOCALNAME) );
        msg.setOntology(orderInstanceDataWrapper.getOntology());
        
        if (orderInstanceDataWrapper.getOntology().equals(Constants.ONTO_GET_ORDER_INSTANCE))
            msg.setContent(orderInstanceDataWrapper.getOrderInstanceId());
        
        logger.debug("dentro l'agente mygatewayagent -> msg = " + msg);
        
        return msg;
    }
    
    private void actionRecipe(ACLMessage msg)
    {
        logger.debug("recipeDataWrapper msg ricevuto = " /* + msg.getContent() */ );

        if (msg.getPerformative() == ACLMessage.INFORM)
        {
            try {
                recipeDataWrapper.setRecipesArrayList((ArrayList<Recipe>)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                recipeDataWrapper.setMessage(ex.getLocalizedMessage());
            }
        } else 
        {
            recipeDataWrapper.setMessage(msg.getContent());
        }
    }
    
    private void actionTriggers(ACLMessage msg)
    {
        logger.debug("triggersDataWrapper ricevuto");
        
        if (msg.getPerformative() == ACLMessage.INFORM)
        {
            if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_TRIGGERED_SKILLS))
            {
                try
                {
                    triggersDataWrapper.setTriggeredSkillsArrayList((ArrayList<TriggeredSkill>)msg.getContentObject());
                } catch (UnreadableException ex)
                {
                    ex.printStackTrace();
                    triggersDataWrapper.setMessage(ex.getLocalizedMessage());
                            
                }
            }
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_TRIGGERED_RECIPES))
            {
                try
                {
                    triggersDataWrapper.setTriggeredRecipesArrayList((ArrayList<TriggeredRecipe>)msg.getContentObject());                    
                } catch (UnreadableException ex)
                {
                    ex.printStackTrace();
                    triggersDataWrapper.setMessage(ex.getLocalizedMessage());
                }
            }
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_TRIGGERED_SKILL))
                triggersDataWrapper.setMessage(msg.getContent());
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_TRIGGERED_RECIPE))
                triggersDataWrapper.setMessage(msg.getContent());
        }
        else
            triggersDataWrapper.setMessage(msg.getContent());
    }

    private void actionRecipeExecutionData(ACLMessage msg)
    {
        logger.debug("recipeExecutionDataWrapper msg ricevuto = " /* + msg.getContent() */ );

        if (msg.getPerformative() == ACLMessage.INFORM)
        {
            if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_GET_RECIPEEXECUTIONDATA))
            {
                try {
                    recipeExecutionDataWrapper.setRecipeExecutionDataArrayList((ArrayList<RecipeExecutionData>)msg.getContentObject());
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                    logger.error("QUI ECCEZIONE: " + ex);
                    recipeExecutionDataWrapper.setMessage(ex.getLocalizedMessage());
                }
            }
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_GET_RECIPEEXECUTIONDATA_PRODUCTINSTANCESPERRECIPE))
            {
                try {
                    recipeExecutionDataWrapper.setProductInstancesPerRecipe((ArrayList<ProductInstance>)msg.getContentObject());
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                    logger.error("QUI ECCEZIONE: " + ex);
                    recipeExecutionDataWrapper.setMessage(ex.getLocalizedMessage());
                }                
            }
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_GET_RECIPEEXECUTIONDATA_RECIPESPERPRODUCTINSTANCE))
            {
                try {
                    recipeExecutionDataWrapper.setRecipesPerProductInstance((ArrayList<Recipe>)msg.getContentObject());
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                    logger.error("QUI ECCEZIONE: " + ex);
                    recipeExecutionDataWrapper.setMessage(ex.getLocalizedMessage());
                }                
            }
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_GET_RECIPEEXECUTIONDATA_KPISETTINGNAMESPERRECIPE))
            {
                try {
                    recipeExecutionDataWrapper.setKPISettingNamesPerRecipe((ArrayList<String>)msg.getContentObject());
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                    logger.error("QUI ECCEZIONE: " + ex);
                    recipeExecutionDataWrapper.setMessage(ex.getLocalizedMessage());
                }                
            }
        } else 
        {
            recipeExecutionDataWrapper.setMessage(msg.getContent());
        }
    }

    private void actionSkill(ACLMessage msg)
    {
        logger.debug("skillDataWrapper msg ricevuto = " + msg );

        if (msg.getPerformative() == ACLMessage.INFORM)
        {
            try {
                skillDataWrapper.setSkill((Skill)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                skillDataWrapper.setMessage(ex.getLocalizedMessage());
            }
        } else 
        {
            skillDataWrapper.setMessage(msg.getContent());
        }
    }

    private void actionProduct(ACLMessage msg)
    {
        logger.debug("productDataWrapper msg ricevuto = " + msg );

        if (msg.getPerformative() == ACLMessage.INFORM)
        {
            try {
                productDataWrapper.setProduct((Product)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                productDataWrapper.setMessage(ex.getLocalizedMessage());
            }
        } else 
        {
            productDataWrapper.setMessage(msg.getContent());
        }
    }

    private void actionOrderInstance(ACLMessage msg)
    {
        logger.debug("orderInstanceDataWrapper msg ricevuto = " + msg );

        if (msg.getPerformative() == ACLMessage.INFORM)
        {
            if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_GET_ORDER_INSTANCE))
            {
                try {
                    orderInstanceDataWrapper.setOrderInstance((OrderInstance)msg.getContentObject());
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                    logger.error("QUI ECCEZIONE: " + ex);
                    orderInstanceDataWrapper.setMessage(ex.getLocalizedMessage());
                }
            }
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_ORDER_INSTANCES))
            {
                try {
                    orderInstanceDataWrapper.setOrderInstancesArrayList((ArrayList<OrderInstance>)msg.getContentObject());
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                    logger.error("QUI ECCEZIONE: " + ex);
                    orderInstanceDataWrapper.setMessage(ex.getLocalizedMessage());
                }
            }
            else if (msg.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PRODUCT_INSTANCES_NOT_YET_PRODUCED))
            {
                try {
                    orderInstanceDataWrapper.setProductInstancesNotYetProducedArrayList((ArrayList<ProductInstance>)msg.getContentObject());
                } catch (UnreadableException ex) {
                    ex.printStackTrace();
                    logger.error("QUI ECCEZIONE: " + ex);
                    orderInstanceDataWrapper.setMessage(ex.getLocalizedMessage());
                }
            }            
        } else 
        {
            orderInstanceDataWrapper.setMessage(msg.getContent());
        }
    }

    private void actionSubSystem(ACLMessage msg)
    {
        logger.debug("subSystemDataWrapper msg ricevuto = " /* + msg.getContent() */ );

        if (subSystemDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_SUBSYSTEMS))
            try {
                subSystemDataWrapper.setSubSystemsArrayList((ArrayList<SubSystem>)msg.getContentObject());
        } catch (UnreadableException ex) {
            ex.printStackTrace();
            logger.error("QUI ECCEZIONE: " + ex);
            subSystemDataWrapper.setMessage(ex.getLocalizedMessage());
        }       
        
        if (subSystemDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_IS_SUBSYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE))
            try {
                subSystemDataWrapper.setSubSystemRampUpChangeStatePossible((Boolean)msg.getContentObject());
        } catch (UnreadableException ex) {
            ex.printStackTrace();
            logger.error("QUI ECCEZIONE: " + ex);
            subSystemDataWrapper.setMessage(ex.getLocalizedMessage());
        }           
    }

    private void actionExecutionTable(ACLMessage msg)
    {
        logger.debug("executionTableDataWrapper msg ricevuto = " /* + msg.getContent() */ );

        logger.debug("executionTableDataWrapper ontology = " + executionTableDataWrapper.getOntology());
        if (executionTableDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EXECUTION_TABLES))
            try {
                executionTableDataWrapper.setExecutionTablesArrayList((ArrayList<ExecutionTable>)msg.getContentObject());
        } catch (UnreadableException ex) {
            ex.printStackTrace();
            logger.error("QUI ECCEZIONE: " + ex);
            executionTableDataWrapper.setMessage(ex.getLocalizedMessage());
        }   
        if (executionTableDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EXECUTION_TABLE_BY_ID))
            try {
                executionTableDataWrapper.setExecutionTable((ExecutionTable)msg.getContentObject());
        } catch (UnreadableException ex) {
            ex.printStackTrace();
            logger.error("QUI ECCEZIONE: " + ex);
            executionTableDataWrapper.setMessage(ex.getLocalizedMessage());
        }   
        if (executionTableDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EXECUTION_TABLE_BY_SUBSYSTEM_ID))
            try {
                executionTableDataWrapper.setExecutionTable((ExecutionTable)msg.getContentObject());
        } catch (UnreadableException ex) {
            ex.printStackTrace();
            logger.error("QUI ECCEZIONE: " + ex);
            executionTableDataWrapper.setMessage(ex.getLocalizedMessage());
        }   

        logger.debug("going to release command executionTableDataWrapper");        
    }

    private void actionEquipmentObservationRel2(ACLMessage msg)
    {
        logger.debug("equipmentObservationRel2DataWrapper msg ricevuto = " /* + msg.getContent() */);

        if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION_REL2))
            equipmentObservationRel2DataWrapper.setMessage(msg.getContent());   

        if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S)
                || equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_EQUIPMENT)
                || equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATION_REL2S_FOR_SYSTEM))
            try {
                equipmentObservationRel2DataWrapper.setEquipmentObservationRel2sArrayList((ArrayList<EquipmentObservationRel2>)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                equipmentObservationRel2DataWrapper.setMessage(ex.getLocalizedMessage());
            }   

        if (equipmentObservationRel2DataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EQUIPMENT_OBSERVATION_REL2))
            try {
                equipmentObservationRel2DataWrapper.setEquipmentObservationRel2((EquipmentObservationRel2)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                equipmentObservationRel2DataWrapper.setMessage(ex.getLocalizedMessage());
            }   
    }

    private void actionEquipmentAssessment(ACLMessage msg)
    {
        logger.debug("equipmentAssessmentDataWrapper msg ricevuto = " /* + msg.getContent() */);

        if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_EQUIPMENT_ASSESSMENT))
            equipmentAssessmentDataWrapper.setMessage(msg.getContent());   

        if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS) 
                || equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_EQUIPMENT)
                || equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_ASSESSMENTS_FOR_SYSTEM))
            try {
                equipmentAssessmentDataWrapper.setEquipmentAssessmentsArrayList((ArrayList<EquipmentAssessment>)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                equipmentAssessmentDataWrapper.setMessage(ex.getLocalizedMessage());
            }   

        if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_IS_SYSTEM_RAMPUP_CHANGE_STAGE_POSSIBLE))
            try {
                equipmentAssessmentDataWrapper.setRampUpChangeStagePossible((Boolean)msg.getContentObject());
                // equipmentAssessmentDataWrapper.setEquipmentAssessmentsArrayList((ArrayList<EquipmentAssessment>)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                equipmentAssessmentDataWrapper.setMessage(ex.getLocalizedMessage());
            }   

        if (equipmentAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_EQUIPMENT_ASSESSMENT))
            try {
                equipmentAssessmentDataWrapper.setEquipmentAssessment((EquipmentAssessment)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                equipmentAssessmentDataWrapper.setMessage(ex.getLocalizedMessage());
            }   

    }

    private void actionPhysicalAdjustment(ACLMessage msg)
    {
        logger.debug("physicalAdjustmentDataWrapper msg ricevuto = " /* + msg.getContent() */);

        if (physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_PHYSICAL_ADJUSTMENT))
            physicalAdjustmentDataWrapper.setMessage(msg.getContent());   

        if (physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS)
                || physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PHYSICAL_ADJUSTMENTS_FOR_EQUIPMENT))
            try {
                physicalAdjustmentDataWrapper.setPhysicalAdjustmentsArrayList((ArrayList<PhysicalAdjustment>)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                physicalAdjustmentDataWrapper.setMessage(ex.getLocalizedMessage());
            }   

        if (physicalAdjustmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_PHYSICAL_ADJUSTMENT))
            try {
                physicalAdjustmentDataWrapper.setPhysicalAdjustment((PhysicalAdjustment)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                physicalAdjustmentDataWrapper.setMessage(ex.getLocalizedMessage());
            }   
    }

    private void actionProcessAssessment(ACLMessage msg)
    {
        logger.debug("processAssessmentDataWrapper msg ricevuto = " + msg);

        if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_PROCESS_ASSESSMENT))
            processAssessmentDataWrapper.setMessage(msg.getContent());   

        if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS)
                || processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_RECIPE)
                || processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_PROCESS_ASSESSMENTS_FOR_SKILL))
            try {
                processAssessmentDataWrapper.setProcessAssessmentsArrayList((ArrayList<ProcessAssessment>)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                processAssessmentDataWrapper.setMessage(ex.getLocalizedMessage());
            }   

        if (processAssessmentDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_GET_PROCESS_ASSESSMENT))
            try {
                processAssessmentDataWrapper.setProcessAssessment((ProcessAssessment)msg.getContentObject());
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                logger.error("QUI ECCEZIONE: " + ex);
                processAssessmentDataWrapper.setMessage(ex.getLocalizedMessage());
            }   
    }

    private void actionEquipmentObservation(ACLMessage msg)
    {
        logger.debug("equipmentObservationDataWrapper msg ricevuto = " /* + msg.getContent() */);

        if (equipmentObservationDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_NEW_EQUIPMENT_OBSERVATION))
            equipmentObservationDataWrapper.setMessage(msg.getContent());   

        if (equipmentObservationDataWrapper.getOntology().equalsIgnoreCase(Constants.ONTO_LIST_EQUIPMENT_OBSERVATIONS))
            try {
//                                 equipmentObservationDataWrapper.setEquipmentObservations((EquipmentObservation[])msg.getContentObject());
                equipmentObservationDataWrapper.setEquipmentObservationsArrayList((ArrayList<EquipmentObservation>)msg.getContentObject());
        } catch (UnreadableException ex) {
            ex.printStackTrace();
            logger.error("QUI ECCEZIONE: " + ex);
            equipmentObservationDataWrapper.setMessage(ex.getLocalizedMessage());
        }   
    }
}
