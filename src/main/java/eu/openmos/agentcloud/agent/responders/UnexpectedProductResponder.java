/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.agent.responders;

import eu.openmos.model.UnexpectedProductData;
import eu.openmos.agentcloud.optimizer.OptimizerAgent;
import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.text.ParseException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Responder for unexpected product messages arriving via socket.
 * 
 * @author valerio.gentile
 */
public class UnexpectedProductResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(UnexpectedProductResponder.class);

        public UnexpectedProductResponder(
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
            UnexpectedProductData unexpectedProductData;
            try {
                unexpectedProductData = (UnexpectedProductData)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            optimizationAlgorithm.trackUnexpectedProductData(unexpectedProductData);
            
            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }
    }
