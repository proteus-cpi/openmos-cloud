package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.cyberphysicalagent.CyberPhysicalAgent;
import eu.openmos.model.SubSystem;
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
 * Notifies the agent (resource or transport) whenever it needs to update its internal memory data.
 * 
 * @author valerio.gentile
 */
public class WorkstationInternalUpdateResponder extends AchieveREResponder {

        Logger logger = LoggerFactory.getLogger(WorkstationInternalUpdateResponder.class);

        public WorkstationInternalUpdateResponder(
                Agent a, 
                MessageTemplate mt
        ) 
        {
            super(a, mt);
        }

        @Override
        protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
            SubSystem cpad;
            try {
                cpad = (SubSystem)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }

            // VaG - 11/12/2017
            // After updating database we update internal memory object.
            ((CyberPhysicalAgent) myAgent).setCpad(cpad);
            
//            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
            logger.debug(getClass().getName() + " - handleRequest - agent updated its internal object: " + cpad);
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
