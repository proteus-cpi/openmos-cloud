package eu.openmos.agentcloud.cloudinterfaceagent;

import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ExecutionTable;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
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
public class StartExecutionTableUpdateInitiator extends AchieveREInitiator 
{
   private static final Logger logger = Logger.getLogger(StartExecutionTableUpdateInitiator.class);
    
    public static ACLMessage buildMessage(AID receiver, ExecutionTable executionTable) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.ONTO_START_EXECUTION_TABLE_UPDATE);
        msg.addReceiver(receiver);
       try {
           msg.setContentObject(executionTable);
       } catch (IOException ex) {
           logger.error(ex);
       }
        logger.debug(new Object() { }.getClass().getEnclosingClass().getName() + " - " + msg.toString());        
        return msg;
    }
    
    public StartExecutionTableUpdateInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        logger.info(getClass().getName() + " - Agent going to update its internal execution table...");
    }  

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        logger.info(getClass().getName() + " - Agent refused to update its internal execution table.");
    }  
    
    @Override
    protected void handleInform(ACLMessage inform) {        
        logger.info(getClass().getName() + " - Agent updated its internal execution table succesfully..");

        // call service made available by the msb
        logger.debug("Call service to inform the MSB.");
        ExecutionTable executionTable = null;
       try {
           executionTable = (ExecutionTable) inform.getContentObject();
       } catch (UnreadableException ex) {
           logger.error(ex.getLocalizedMessage());
       }
       if (executionTable != null)
            MSBHelper.updateExecutionTable(executionTable);
       else 
           logger.warn("execution table null -> can not update msb execution table");
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.info(getClass().getName() + " - Agent DID NOT update its internal execution table.");

//        // call service made available by the msb
//        logger.debug("Call service to inform the MSB.");
//        String equipmentId = failure.getContent();
//        MSBHelper.notifyAgentNotRemoved(equipmentId);
    }
    
}
