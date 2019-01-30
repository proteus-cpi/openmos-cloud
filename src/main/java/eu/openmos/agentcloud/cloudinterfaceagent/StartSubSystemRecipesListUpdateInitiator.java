package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * 
 * Copied from DeployNewAgentInitiator
 * 
 * Initiator that is to be launched whenever the CIA (Cloud Interface Agent) is 
 * notified to update an agent internal execution table. This will start a FIPA Request Protocol that
 * will request the agent itself to update its internal table and notify the optimizer in order
 * to update the database.
 * 
 */
public class StartSubSystemRecipesListUpdateInitiator extends AchieveREInitiator 
{
   private static final Logger logger = Logger.getLogger(StartSubSystemRecipesListUpdateInitiator.class);
    
    public static ACLMessage buildMessage(AID receiver, Recipe[] recipes) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_START_SUBSYSTEM_RECIPES_LIST_UPDATE);
        msg.addReceiver(receiver);
       try {
           msg.setContentObject(recipes);
       } catch (IOException ex) {
           logger.error(ex);
       }
        logger.debug(new Object() { }.getClass().getEnclosingClass().getName() + " - " + msg.toString());        
        return msg;
    }
    
    public StartSubSystemRecipesListUpdateInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        logger.info(getClass().getName() + " - Agent going to update its internal recipes list...");
    }  

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        logger.info(getClass().getName() + " - Agent refused to update its internal recipes list.");
    }  
    
    @Override
    protected void handleInform(ACLMessage inform) {        
        logger.info(getClass().getName() + " - Agent updated its internal recipes list succesfully..");

       try {
            // call service made available by the msb
            logger.debug("Call service to inform the MSB about recipes update.");
            Recipe[] recipesToUpdateOnMSB = (Recipe[]) inform.getContentObject();
//            boolean recipeDeployed = MSBHelper.updateRecipes(myAgent.getLocalName(), Constants.RECIPES_SUGGESTION, recipesToUpdateOnMSB);
            List<Recipe> recipesList = Arrays.asList(recipesToUpdateOnMSB);
            boolean recipeDeployed = MSBHelper.updateRecipes(inform.getSender().getLocalName(), Constants.RECIPES_SUGGESTION, recipesList);
       } catch (UnreadableException ex) {
           java.util.logging.Logger.getLogger(StartSubSystemRecipesListUpdateInitiator.class.getName()).log(Level.SEVERE, null, ex);
       }
        

    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.info(getClass().getName() + " - Agent DID NOT update its internal recipes list.");

//        // call service made available by the msb
//        logger.debug("Call service to inform the MSB.");
//        String equipmentId = failure.getContent();
//        MSBHelper.notifyAgentNotRemoved(equipmentId);
    }
    
}
