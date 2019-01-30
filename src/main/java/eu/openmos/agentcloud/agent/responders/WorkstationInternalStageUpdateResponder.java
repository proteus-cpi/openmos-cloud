package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.model.SubSystem;
import eu.openmos.model.SubSystemChangeStage;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notifies the agent (resource or transport) whenever it needs to update its internal STAGE memory data.
 * 
 * @author valerio.gentile
 */
public class WorkstationInternalStageUpdateResponder extends AchieveREResponder {

        Logger logger = LoggerFactory.getLogger(WorkstationInternalStageUpdateResponder.class);

        public WorkstationInternalStageUpdateResponder(
                Agent a, 
                MessageTemplate mt
        ) 
        {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            SubSystemChangeStage subSystemChangeStage;
            try {
                subSystemChangeStage = (SubSystemChangeStage)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }

            // VaG - 21/02/2018
            // After updating database we update internal field of memory object.
            SubSystem cpad = ((CyberPhysicalAgent) myAgent).getCpad();
            cpad.setStage(subSystemChangeStage.getNewStage());
            cpad.setRegistered(subSystemChangeStage.getRegistered());
            ((CyberPhysicalAgent) myAgent).setCpad(cpad);
            
            logger.debug(getClass().getName() + " - handleRequest - agent updated its internal STAGE object: " + cpad);
            ACLMessage reply = request.createReply();
            try {
                reply.setContentObject(cpad);
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage());
            }
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }
    }
