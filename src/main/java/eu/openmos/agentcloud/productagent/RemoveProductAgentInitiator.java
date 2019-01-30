package eu.openmos.agentcloud.productagent;

import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.FinishedProductInfo;
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
 * Initiator that is to be launched whenever the CIA (Cloud Interface Agent) is 
 * notified to remove a product agent. This will start a FIPA Request Protocol that
 * will request the agent itself to suicide.
 * 
 */
public class RemoveProductAgentInitiator extends AchieveREInitiator 
{
   private static final Logger logger = Logger.getLogger(RemoveProductAgentInitiator.class);
    
    public static ACLMessage buildMessage(AID receiver, FinishedProductInfo finishedProductInfo /* String content */)  
    {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setOntology(Constants.REMOVE_PRODUCT_AGENT);
        msg.addReceiver(receiver);
       try {
           // msg.setContent(content);
           msg.setContentObject(finishedProductInfo);
       } catch (IOException ex) {
           logger.error(ex.getLocalizedMessage());
       }
        logger.debug(new Object() { }.getClass().getEnclosingClass().getName() + " - " + msg.toString());        
        return msg;
    }
    
    public RemoveProductAgentInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        logger.info(getClass().getName() + " - Product agent being removed...");
    }  

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        logger.info(getClass().getName() + " - Product agent refused to remove itself.");
    }  
    
    @Override
    protected void handleInform(ACLMessage inform) {        
        logger.info(getClass().getName() + " - Product agent removed succesfully..");

        // call service made available by the msb
        logger.debug("Call service to inform the MSB.");
        FinishedProductInfo fpi = null;
       try {
           fpi = (FinishedProductInfo)inform.getContentObject();
       } catch (UnreadableException ex) {
           logger.error(ex.getLocalizedMessage());
       }
       if (fpi != null)
       {
            String productAgentId = fpi.getProductInstanceId();
            MSBHelper.confirmAgentRemoved(productAgentId);
       }
       else
           logger.warn("RemoveProductAgentInitiator - handleInform - finishedproductinfo is null! can not inform MSB");
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        logger.info(getClass().getName() + " - Product agent was not removed.");

        // call service made available by the msb
        logger.debug("Call service to inform the MSB.");
        FinishedProductInfo fpi = null;
       try {
           fpi = (FinishedProductInfo)failure.getContentObject();
       } catch (UnreadableException ex) {
           logger.error(ex.getLocalizedMessage());
       }
       if (fpi != null)
       {
            String productAgentId = fpi.getProductInstanceId();
            MSBHelper.notifyAgentNotRemoved(productAgentId);
       }
       else
           logger.warn("RemoveProductAgentInitiator - handleInform - finishedproductinfo is null! can not inform MSB");
    }
    
}
