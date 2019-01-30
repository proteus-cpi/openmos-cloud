/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.servicebus;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.msb.ws.recipesdeployment.wsimport.RecipesDeployment;
import eu.openmos.msb.ws.recipesdeployment.wsimport.RecipesDeployment_Service;
import java.util.LinkedList;
// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.model.Recipe;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ProductInstance;
import eu.openmos.msb.ws.eventconfirmation.wsimport.EventConfirmation;
import eu.openmos.msb.ws.eventconfirmation.wsimport.EventConfirmation_Service;
import java.util.List;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.apache.log4j.Logger;

/**
 * Wrapper for calls directed to the Manifacturing Service Bus.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class MSBHelper {
    private static final Logger logger = Logger.getLogger(MSBHelper.class.getName());
    
    /**
     * This method aims to send a list of recipes to the MSB.
     * 
     * @param deviceName  The name of the agent = the name of the underlying device
     * @param mode   Possible values: suggested recipes, actual recipes, activate the recipes
     * @param recipes  List of recipes for the device
     * @see eu.openmos.agentcloud.utilities.Constants.RECIPES_SUGGESTION
     * @see eu.openmos.agentcloud.utilities.Constants.RECIPES_ACTUAL
     * @see eu.openmos.agentcloud.utilities.Constants.RECIPES_ACTIVATION
     * 
     * @return   true or false depending on the MSB's response
     * 
     * TODO check implementation according to other methods!!!!
     */
    public static boolean updateRecipes(String deviceName, int mode, List<Recipe> recipes)
    {
        logger.info(new Object () {}.getClass().getEnclosingClass().getName() + " - sendRecipes - start");

        RecipesDeployment_Service recipesDeploymentService = new RecipesDeployment_Service();
	RecipesDeployment recipesDeployment = recipesDeploymentService.getRecipesDeploymentImplPort();
        
        // http://localhost:9997/wsRecipesDeployment
        // openmos.msb.recipesdeployment.ws.endpoint=http://localhost:9997/wsRecipesDeployment
/////////////////////////////
        String MSB_RECIPESDEPLOYMENT_WS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.MSB_RECIPESDEPLOYMENT_WS_PARAMETER);
        logger.info("MSB Recipes deployment address = [" + MSB_RECIPESDEPLOYMENT_WS_VALUE + "]");            

        BindingProvider bindingProvider = (BindingProvider) recipesDeployment;
        bindingProvider.getRequestContext().put(
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSB_RECIPESDEPLOYMENT_WS_VALUE);
//////////////////////////////
        
        boolean status = recipesDeployment.updateRecipes(deviceName, mode, recipes);
        
        logger.info(new Object () {}.getClass().getEnclosingClass().getName() + " - sendRecipes - status = [" + status + "]");
        return status;
    }
    
    public static void main(String[] args)
    {
        MSBHelper.updateRecipes("test device name", 1, new LinkedList<Recipe>());
    }

    public static boolean confirmCloudActive()
    {
        String operationName = "confirmCloudActive";
        String loggerInfo = getClassName() + " - " + operationName + " - ";
        
        logger.info(loggerInfo + "begin");

        try
        {
            getEventConfirmationWS().cloudActive();
        }
        catch (WebServiceException wse)
        {
            logger.error(loggerInfo + "MSB webservices are not responding... maybe MSB is down?");
            // can stop and close websocket just opened
            return false;
        }
        
        logger.info(loggerInfo + "msb notified about cloud startup...");
        return true;
    }

    public static boolean confirmAgentCreated(String agentName)
    {
        String operationName = "confirmAgentCreated";
        String loggerInfo = getClassName() + " - " + operationName + " - ";
        
        logger.info(loggerInfo + "begin");

        boolean status = false;
        
        try
        {
            status = getEventConfirmationWS().agentCreated(agentName);
        }
        catch (WebServiceException wse)
        {
            logger.error(loggerInfo + "MSB webservices are not responding... maybe MSB is down?");
            // can stop and close websocket just opened
            return false;
        }
        
        logger.info(loggerInfo + "status = [" + status + "]");
        return status;
    }

    public static boolean notifyAgentNotCreated(String agentName)
    {
        String operationName = "notifyAgentNotCreated";
        String loggerInfo = getClassName() + " - " + operationName + " - ";
        
        logger.info(loggerInfo + "begin");

        boolean status = false;
        
        try
        {
            status = getEventConfirmationWS().agentNotCreated(agentName);
        }
        catch (WebServiceException wse)
        {
            logger.error(loggerInfo + "MSB webservices are not responding... maybe MSB is down?");
            // can stop and close websocket just opened
            return false;
        }
        
        logger.info(loggerInfo + "status = [" + status + "]");
        return status;
    }

    public static boolean confirmAgentRemoved(String agentName)
    {
        String operationName = "confirmAgentRemoved";
        String loggerInfo = getClassName() + " - " + operationName + " - ";
        
        logger.info(loggerInfo + "begin");

        boolean status = false;
        
        try
        {
            logger.debug(loggerInfo + "agentName = [" + agentName + "]");
            status = getEventConfirmationWS().agentRemoved(agentName);
        }
        catch (WebServiceException wse)
        {
            logger.error(loggerInfo + "MSB webservices are not responding... maybe MSB is down?");
            // can stop and close websocket just opened
            return false;
        }
        
        logger.info(loggerInfo + "status = [" + status + "]");
        return status;
    }

    public static boolean notifyAgentNotRemoved(String agentName)
    {
        String operationName = "notifyAgentNotRemoved";
        String loggerInfo = getClassName() + " - " + operationName + " - ";
        
        logger.info(loggerInfo + "begin");

        boolean status = false;
        
        try
        {
            status = getEventConfirmationWS().agentNotRemoved(agentName);
        }
        catch (WebServiceException wse)
        {
            logger.error(loggerInfo + "MSB webservices are not responding... maybe MSB is down?");
            // can stop and close websocket just opened
            return false;
        }
        
        logger.info(loggerInfo + "status = [" + status + "]");
        return status;
    }

    public static boolean confirmOrderCreated(String orderId, List<String> agentsOrderDetails)
    {
        String operationName = "confirmOrderCreated";
        String loggerInfo = getClassName() + " - " + operationName + " - ";
        
        logger.info(loggerInfo + "begin: orderId = [" + orderId + "] details = [" + agentsOrderDetails + "]");

        boolean status = false;
        
        try
        {
            status = getEventConfirmationWS().orderInstanceCreated(orderId, agentsOrderDetails);
        }
        catch (WebServiceException wse)
        {
            logger.error(loggerInfo + "MSB webservices are not responding... maybe MSB is down?");
            // can stop and close websocket just opened
            return false;
        }
        
        logger.info(loggerInfo + "status = [" + status + "]");
        return status;
    }

    public static boolean notifyOrderNotCreated(String orderId)
    {
        String operationName = "notifyOrderNotCreated";
        String loggerInfo = getClassName() + " - " + operationName + " - ";
        
        logger.info(loggerInfo + "begin");

        boolean status = false;
        
        try
        {
            status = getEventConfirmationWS().orderInstanceNotCreated(orderId);
        }
        catch (WebServiceException wse)
        {
            logger.error(loggerInfo + "MSB webservices are not responding... maybe MSB is down?");
            // can stop and close websocket just opened
            return false;
        }
        
        logger.info(loggerInfo + "status = [" + status + "]");
        return status;
    }
    
    private static EventConfirmation getEventConfirmationWS()
    {
        EventConfirmation_Service eventConfirmationService = new EventConfirmation_Service();
	EventConfirmation eventConfirmation = eventConfirmationService.getEventConfirmationImplPort();
        
        String MSB_EVENTCONFIRMATION_WS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.MSB_EVENTCONFIRMATION_WS_PARAMETER);
        logger.info("MSB event configrmation ws address = [" + MSB_EVENTCONFIRMATION_WS_VALUE + "]");            

        BindingProvider bindingProvider = (BindingProvider) eventConfirmation;
        bindingProvider.getRequestContext().put(
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSB_EVENTCONFIRMATION_WS_VALUE);
        
        return eventConfirmation;
    }
            

    private static String getClassName()
    {
        return new Object () {}.getClass().getEnclosingClass().getName();
    }

    public static void confirmProductInstanceCreated(ProductInstance productInstance) {
        // TBI  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void notifyProductInstanceNotCreated(ProductInstance productInstance) {
        // TBI  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static boolean updateExecutionTable(ExecutionTable executionTable)
    {
        logger.info(new Object () {}.getClass().getEnclosingClass().getName() + " - updateExecutionTable - start");

        RecipesDeployment_Service recipesDeploymentService = new RecipesDeployment_Service();
	RecipesDeployment recipesDeployment = recipesDeploymentService.getRecipesDeploymentImplPort();
        
        String MSB_RECIPESDEPLOYMENT_WS_VALUE = ConfigurationLoader.getMandatoryProperty(Constants.MSB_RECIPESDEPLOYMENT_WS_PARAMETER);
        logger.info("MSB Recipes deployment address = [" + MSB_RECIPESDEPLOYMENT_WS_VALUE + "]");            

        BindingProvider bindingProvider = (BindingProvider) recipesDeployment;
        bindingProvider.getRequestContext().put(
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSB_RECIPESDEPLOYMENT_WS_VALUE);
        
        boolean status = recipesDeployment.updateExecutionTable(executionTable);
        
        logger.info(new Object () {}.getClass().getEnclosingClass().getName() + " - updateExecutionTable - status = [" + status + "]");
        return status;
    }
    
}

