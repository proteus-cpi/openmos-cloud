package eu.openmos.agentcloud.productagent;

import eu.openmos.model.RawProductData;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

/**
 * Notifies the product when the transport finishes transporting it to the next
 * location. When the new location is received it informs the optimizer of the
 * new location. Similar logic needs to be implemented in the optimizer so that
 * the optimizer can tell if the received information concerns a physical or 
 * logical location.
 * 
 * Content: The content of this message must be a serialized object of type
 * AgentData.
 * Counterpart: Transport Agent
 * Ontology: ONTO_LOCATION.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 *      
 */
public class NewLocationResponder extends AchieveREResponder {

    /**
     * Default AchieveREResponder constructor.
     * 
     * @param a - the agent that will hold this behaviour.
     * @param mt - the template for the messages to which this behaviour will
     * listen to.
     */
    public NewLocationResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    /**
     * Method that handles the request message that will come from the counterpart.
     * 
     * @param request - message that triggers the protocol.
     * @return Agree or Failure message.
     * @throws NotUnderstoodException - default exception
     * @throws RefuseException - default exception
     */
    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        ACLMessage response = request.createReply();
        response.setPerformative(ACLMessage.INFORM);
        
        // AgentData data = AgentData.fromString(request.getContent());
        RawProductData data;
        try {
            data = (RawProductData)request.getContentObject();
        } catch (UnreadableException ex) {
            throw new RefuseException(ex.getLocalizedMessage());
        }
        
        // OLD CODE - TBV
        /*
        if(data.getLogicalLocation().getLocation().equals(""))
            ((ProductAgent) myAgent).physicalLocations.add(data.getPhysicalLocation());
        else
            ((ProductAgent) myAgent).logicalLocations.add(data.getLogicalLocation());                    
        */
        if(!data.getLogicalLocation().getLocation().equals(""))
            ((ProductAgent) myAgent).logicalLocations.add(data.getLogicalLocation());            
//        myAgent.addBehaviour(new NotifyOptimizerOfUpdatedData(myAgent, NotifyOptimizerOfUpdatedData.buildMessage(myAgent, request.getContent())));
        myAgent.addBehaviour(new NotifyOptimizerOfUpdatedData(myAgent, NotifyOptimizerOfUpdatedData.buildMessage(myAgent, data)));
        return response; 
    }    
//    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
//        ACLMessage response = request.createReply();
//        response.setPerformative(ACLMessage.INFORM);
//        
//        // AgentData data = AgentData.fromString(request.getContent());
//        RawProductData data;
//        try {
//            data = RawProductData.fromString(request.getContent());
//        } catch (ParseException ex) {
//            throw new RefuseException(ex.getMessage());
//        }
//        
//        // OLD CODE - TBV
//        /*
//        if(data.getLogicalLocation().getLocation().equals(""))
//            ((ProductAgent) myAgent).physicalLocations.add(data.getPhysicalLocation());
//        else
//            ((ProductAgent) myAgent).logicalLocations.add(data.getLogicalLocation());                    
//        */
//        if(!data.getLogicalLocation().getLocation().equals(""))
//            ((ProductAgent) myAgent).logicalLocations.add(data.getLogicalLocation());            
//        myAgent.addBehaviour(new NotifyOptimizerOfUpdatedData(myAgent, NotifyOptimizerOfUpdatedData.buildMessage(myAgent, request.getContent())));
//        return response; 
//    }
}
