package eu.openmos.agentcloud.agent.responders;

import eu.openmos.agentcloud.optimizer.PluggableOptimizerInterface;
import eu.openmos.model.TriggeredSkill;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import static jdk.nashorn.internal.objects.NativeDebug.getClass;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ListTriggeredSkillsResponder extends AchieveREResponder {

        private PluggableOptimizerInterface optimizationAlgorithm;
        Logger logger = Logger.getLogger(ListTriggeredSkillsResponder.class);

        public ListTriggeredSkillsResponder(
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
            String skillId;
            try {
                skillId = (String)request.getContentObject();
            } catch (UnreadableException ex) {
                throw new RefuseException(ex.getLocalizedMessage());
            }
            logger.debug("responder -> skillId = " + skillId);
        
            List<TriggeredSkill> listToReturn = optimizationAlgorithm.listTriggeredSkills(skillId);
            
            // list is not serializable, so need conversion
            TriggeredSkill[] triggeredSkills = listToReturn.stream().toArray(TriggeredSkill[]::new);
            ArrayList<TriggeredSkill> al = new ArrayList(listToReturn);
            
            
            ACLMessage reply = request.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            try {
                reply.setContentObject(al);
                logger.debug(getClass().getName() + " - handleRequest - " + triggeredSkills);
                for (int j = 0; j < triggeredSkills.length; j++)
                    logger.debug("j = " + j + " -> " + triggeredSkills[j]);
            } catch (IOException ex) {
                throw new RefuseException(ex.getLocalizedMessage());                
            }
            return reply;
        }
    }

