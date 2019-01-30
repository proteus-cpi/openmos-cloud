/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.agent.responders;

//import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.agentcloud.agent.initiators.WorkstationInternalUpdateInitiator;
import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.model.UnexpectedProductData;
import eu.openmos.agentcloud.optimizer.OptimizerAgent;
import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.SubSystem;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notifies the optimizer whenever a workstation needs to be updated.
 * 
 * @author valerio.gentile
 */
public class WorkstationUpdateResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(WorkstationUpdateResponder.class);

        public WorkstationUpdateResponder(
                Agent a, 
                PluggableOptimizerInterface optimizationAlgorithm, 
                MessageTemplate mt
        ) 
        {
            super(a, mt);
            this.optimizationAlgorithm = optimizationAlgorithm;
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//            CyberPhysicalAgentDescription cpad;
//            try {
//                cpad = CyberPhysicalAgentDescription.fromString(request.getContent());
//            } catch (ParseException ex) {
//                throw new RefuseException(ex.getMessage());
//            }
            SubSystem cpad;
            try {
                cpad = (SubSystem)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            optimizationAlgorithm.updateWorkstation(cpad);

            // VaG - 07/12/2017
            // After updating database we update internal memory object.
//            ((CyberPhysicalAgent) myAgent).setCpad(cpad);
            AID agentToBeUpdated = new AID(cpad.getUniqueId(), false);
            ACLMessage updateMessage = WorkstationInternalUpdateInitiator.buildMessage(agentToBeUpdated, cpad);
            myAgent.addBehaviour(new WorkstationInternalUpdateInitiator(myAgent, updateMessage));
            
//            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
            logger.debug(getClass().getName() + " - handleRequest");
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(cpad);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            return reply;
        }
    }
