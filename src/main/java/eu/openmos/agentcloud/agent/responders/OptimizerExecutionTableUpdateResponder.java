package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.agentcloud.servicebus.MSBHelper;
import eu.openmos.model.ExecutionTable;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import javax.xml.ws.WebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notifies the optimizer whenever a workstation needs to be update its internal execution table.
 * 
 * @author valerio.gentile
 */
public class OptimizerExecutionTableUpdateResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = LoggerFactory.getLogger(OptimizerExecutionTableUpdateResponder.class);

        public OptimizerExecutionTableUpdateResponder(
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
            ExecutionTable executionTable;
            try {
                executionTable = (ExecutionTable)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            optimizationAlgorithm.updateExecutionTable(executionTable);
            
            ACLMessage reply = request.createReply();

            // VaG - 31/10/2017 TO BE CHECKED WITH PEDRO 
            logger.info(getClass().getName() + " - Going to notify MSB about updated execution table");
            
            // VaG - 17/01/2018
            // In case MSB is not running....            
            // boolean etUpdated = MSBHelper.updateExecutionTable(executionTable);
            boolean etUpdated = false;            
            try
            {
                etUpdated = MSBHelper.updateExecutionTable(executionTable);
            }
            catch (WebServiceException wse)
            {
                logger.error(getClass().getName() + " - MSB webservices for updated execution tables notification are not responding... maybe MSB is down?");
                // can stop and close websocket just opened
                // return false;
            }

            if(!etUpdated){
                logger.info("Execution table " + executionTable.getUniqueId() + " could not be deployed to the MSB.");
                reply.setPerformative(ACLMessage.FAILURE);
            }else{
                logger.info("Execution table " + executionTable.getUniqueId() + " deployed to the MSB.");            
                reply.setPerformative(ACLMessage.INFORM);
            }
            return reply;

            
//            logger.debug(getClass().getName() + " - handleRequest - " + request.getContent());
//            ACLMessage reply = request.createReply();
//            reply.setPerformative(ACLMessage.INFORM);
//            return reply;
        }
    }
