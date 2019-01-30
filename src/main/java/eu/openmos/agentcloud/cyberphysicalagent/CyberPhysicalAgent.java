/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cyberphysicalagent;

import eu.openmos.model.LogicalLocation;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.agentcloud.OpenMosAgent;
import eu.openmos.agentcloud.utilities.Constants;
//import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.agentcloud.agent.utilities.DFInteraction;
import eu.openmos.model.SubSystem;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.log4j.Logger;

/**
 * Abstract class used to describe common features to Resource, Transport and
 * Product Agents.
 * 
 * @author Pedro Lima <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro<luis.ribeiro@liu.se>
 * @author Valerio Gentile<valerio.gentile@we-plus.eu>
 */
public abstract class CyberPhysicalAgent extends OpenMosAgent {
    /**
    * Object that writes logs into the file.
    */
    private static final Logger logger = Logger.getLogger(CyberPhysicalAgent.class.getName());

    /**
    * Object that holds the current physical location of the device abstracted 
    * by this agent.
    */
    private PhysicalLocation locationPhysical;
    /**
     * Object that holds the current logical location of the device abstracted 
     * by this agent.
     */
    private LogicalLocation locationLogical;
    private boolean deleteYourself = false;
    /**
     * Time after which the agent should suicide.
     */
//    private long deletionDelayTime = 5000;
    protected long deletionDelayTime = 5000;
    /**
     * Object that describes this agent.
     */
//    private CyberPhysicalAgentDescription cpad;
    private SubSystem cpad;

    /**
     * Parameterised constructor of the Cyber-Physical Agent.
     * 
     * @param locationPhysical - Physical location of the device.
     * @param locationLogical - Logical location of the device.
     * @param cpad - Description of the agent.
     */
//    public CyberPhysicalAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, CyberPhysicalAgentDescription cpad) {
    public CyberPhysicalAgent(PhysicalLocation locationPhysical, LogicalLocation locationLogical, SubSystem cpad) {
        this.locationPhysical = locationPhysical;
        this.locationLogical = locationLogical;
        this.cpad = cpad;
    }

    /**
     * Default constructor.
     */
    public CyberPhysicalAgent() {
    }
        
    public boolean isDeleteYourself() {
        return deleteYourself;
    }

    public void setDeleteYourself(boolean deleteYourself) {
        this.deleteYourself = deleteYourself;
    }

    public long getDeletionDelayTime() {
        return deletionDelayTime;
    }

    public void setDeletionDelayTime(long deletionDelayTime) {
        this.deletionDelayTime = deletionDelayTime;
    }
           
    public void setLocationPhysical(PhysicalLocation locationPhysical) {
        this.locationPhysical = locationPhysical;
    }

    public void setLocationLogical(LogicalLocation locationLogical) {
        this.locationLogical = locationLogical;
    }

    public PhysicalLocation getLocationPhysical() {
        return locationPhysical;
    }
    
    public LogicalLocation getLocationLogical() {
        return locationLogical;
    }

//    public CyberPhysicalAgentDescription getCpad() {
//        return cpad;
//    }
//
//    public void setCpad(CyberPhysicalAgentDescription cpad) {
//        this.cpad = cpad;
//    }

    public SubSystem getCpad() {
        return cpad;
    }

    public void setCpad(SubSystem cpad) {
        this.cpad = cpad;
    }
    
    /**
     * Setup method for the Cyber-Physical Agent. Adds a behaviour that allows
     * the agent to receive takedown requests and suicide.
     */
    @Override
    protected void setup() {
        
        addBehaviour(new ProcessTakeDownRequestBehaviour(this,
                MessageTemplate.and(MessageTemplate.MatchOntology(Constants.REMOVE_AGENT), MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
                deletionDelayTime));
    }        

     /**
     * Takedown method of the Cyber-Physical Agent. It deregisters the agent 
     * from the DF.
     */
    @Override
    protected void takeDown() {
        logger.debug(getClass().getName() + " - takeDown - " + getLocalName() + " going to deregister...");
        DFInteraction.DeregisterFromDF(this);
        logger.debug(getClass().getName() + " - takeDown - " + getLocalName() + " deregistration done");
    }
    
    
}
