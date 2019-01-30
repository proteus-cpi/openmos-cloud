/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.agent.utilities;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import org.apache.log4j.Logger;

/**
 * Set of utilities for Jade Directory Facilitator (DF) wrapping.
 * @see http://jade.tilab.com/doc/api/jade/domain/DFService.html
 * 
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 */
public class DFInteraction {
    
    private static final Logger logger = Logger.getLogger(DFInteraction.class.getName());
    
    /**
     * Register an Agent in DF.
     * 
     * @param myAgent Reference to the agent to be registered
     * @param serviceName Name of the agent to be registered
     * @param serviceType Type of the agent to be registered
     */
    public static void RegisterInDF(Agent myAgent, String serviceName, String serviceType) {

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(serviceName);
        dfd.addServices(sd);

        try {
            DFService.register(myAgent, dfd);
        } catch (FIPAException ex) {
            logger.error(ex);
        }

    }

    /**
     * Search in DF for a specific agent-type.
     * 
     * @param type Type of the agent to look-for
     * @param myAgent The agent is performing the search
     * @return
     */
    public static DFAgentDescription[] SearchInDF(String type, Agent myAgent) {

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        dfd.addServices(sd);

        DFAgentDescription[] result = null;
        try {
            result = DFService.search(myAgent, dfd);
        } catch (FIPAException ex) {
            logger.error(ex);
        }

        return result;
    }
    
    /**
     *
     * @param type
     * @param name
     * @param myAgent
     * @return
     */
//    public static DFAgentDescription SearchAgentInDF(String type, String name, Agent myAgent) {
//        logger.debug("SearchAgentInDF start");
//        logger.debug("SearchAgentInDF type: [" + type + "]");
//        logger.debug("SearchAgentInDF name: [" + name + "]");
//        logger.debug("SearchAgentInDF agent: [" + myAgent + "]");
//        DFAgentDescription[] result = SearchInDF(type, myAgent);
//        logger.debug("SearchInDF done");
//        for (int i = 0; i < result.length; i++)
//        {
//            logger.debug("SearchAgentInDF: current = [" + result[i].getName() + "]");
//            // ( agent-identifier :name Resource_1479895230761@192.168.15.1:1099/JADE  :addresses (sequence http://v2HSVIL4WIN7.wenet.local:7778/acc ))
//            // if (result[i].getName().toString().startsWith(name))
//            if (result[i].getName().toString().indexOf(name) != -1)
//            {
//                logger.debug("SearchAgentInDF - got it! [" + result[i] + "]" );
//                return result[i];
//            }
//        }
//
//        return null;
//    }
    
    /**
     * Unregister an agent from the DF.
     * 
     * @param agentToBeDeregistered  the agent to be unregistered
     */
    public static void DeregisterFromDF(Agent agentToBeDeregistered) {
        try {            
            DFService.deregister(agentToBeDeregistered);
        } catch (FIPAException ex) {
            logger.error(ex);
        }
    }    
}
