/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cyberphysicalagent;

import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;
import org.apache.log4j.Logger;

/**
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 */
public class ProcessTakeDownRequestBehaviour extends AchieveREResponder {

    private static final Logger logger = Logger.getLogger(ProcessTakeDownRequestBehaviour.class);    
    private final long deletionDelay; 
    
    public ProcessTakeDownRequestBehaviour(Agent a, MessageTemplate mt, long deletionDelay) {
        super(a, mt);
        this.deletionDelay = deletionDelay;
    }
    
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        logger.debug(myAgent.getLocalName() + " - " + request.toString());
        
        ACLMessage agree = request.createReply();
        agree.setPerformative(ACLMessage.AGREE);
//        agree.setContent("");
//        agree.setContent(myAgent.getLocalName()); // OR..
        agree.setContent(request.getContent()); // as an alternative
        
        DFAgentDescription[] dfad = DFInteraction.SearchInDF(Constants.DF_PRODUCTION_OPTIMIZER, myAgent);
        ACLMessage requestToOptimizer = new ACLMessage(ACLMessage.REQUEST);
        requestToOptimizer.addReceiver(dfad[0].getName());
        requestToOptimizer.setOntology(Constants.REMOVE_AGENT_FROM_OPTIMIZER);        
        // requestToOptimizer.setContent(((CyberPhysicalAgent) myAgent).getCpad().toString());    
//        requestToOptimizer.setContent("");    
        requestToOptimizer.setContent(request.getContent());    
        
        registerPrepareResultNotification(new NotifyOptimizerBehaviour(myAgent, requestToOptimizer));
        return agree;
        
    }
    
    private class NotifyOptimizerBehaviour extends AchieveREInitiator {
        
        public NotifyOptimizerBehaviour(Agent a, ACLMessage msg) {
            super(a, msg);
            logger.debug(msg);
        }
        
        @Override
        protected void handleInform(ACLMessage inform) {
            ACLMessage originalRequest = (ACLMessage) ((AchieveREResponder) getParent()).getDataStore().get(((AchieveREResponder) getParent()).REQUEST_KEY);
            ACLMessage notificationMessage = originalRequest.createReply();
            // vale ACLMessage notificationMessage = inform.createReply();
            notificationMessage.setPerformative(ACLMessage.INFORM);
            
            // VaG - 28/07/2017
            notificationMessage.setContent(originalRequest.getContent());
            
            logger.debug(getClass().getName() + " - " + notificationMessage);
            ((AchieveREResponder) getParent()).getDataStore().put(((AchieveREResponder) getParent()).RESULT_NOTIFICATION_KEY, notificationMessage);

            ((CyberPhysicalAgent) myAgent).setDeleteYourself(true);
            
            myAgent.addBehaviour(new WakerBehaviour(myAgent, deletionDelay) {
                @Override
                protected void onWake() {       
                    logger.debug(getClass().getName() + " onWake - before myAgent.dodelete()");
                    ((CyberPhysicalAgent)myAgent).doDelete(); //To change body of generated methods, choose Tools | Templates.
                }                
            });
        }
        
    }
    
}
