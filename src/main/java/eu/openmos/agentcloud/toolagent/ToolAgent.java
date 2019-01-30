package eu.openmos.agentcloud.toolagent;

import eu.openmos.agentcloud.OpenMosAgent;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.log4j.Logger;

/**
 * Superclass for agents that are utilities and are not product, transport or resource agents, e.g.
 * - Cloud Interface agent
 * - Deployment agent
 * - Optimizer agent
 * - Platform Launcher agent,
 * - Production Optimizer agent
 * 
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ToolAgent extends OpenMosAgent {
    private static final Logger logger = Logger.getLogger(ToolAgent.class.getName());
    private long deletionDelayTime = 5000;

    /**
     *
     * @return time in milliseconds after which the agent should suicide
     */
    public long getDeletionDelayTime() {
        return deletionDelayTime;
    }

    /**
     * It sets time after which the agent should suicide (in milliseconds).
     * 
     * @param deletionDelayTime
     */
    public void setDeletionDelayTime(long deletionDelayTime) {
        this.deletionDelayTime = deletionDelayTime;
    }        

    /**
     * Setup method for the Tool Agent. 
     * Adds a behaviour that allows the agent to receive takedown requests and suicide.
     */
    @Override
    protected void setup() {
        addBehaviour(new ToolAgentTakeDownRequestBehaviour(this,
                MessageTemplate.and(MessageTemplate.MatchOntology(Constants.REMOVE_AGENT), MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
                deletionDelayTime));
    }


    /**
     * Takedown method of the Tool Agent. It deregisters the agent from the DF.
     */    
    @Override
    protected void takeDown() {
        logger.debug(getClass().getName() + " - takeDown - " + getLocalName() + " going to deregister...");
        DFInteraction.DeregisterFromDF(this);
        logger.debug(getClass().getName() + " - takeDown - " + getLocalName() + " deregistration done");
    }
}
