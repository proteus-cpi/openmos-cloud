package eu.openmos.agentcloud.cloudinterfaceagent.data;

import eu.openmos.model.Base;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.ProductInstance;
import java.io.Serializable;
import java.util.ArrayList;

public class OrderInstanceDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757720L;      
    
    private OrderInstance orderInstance;  
    private String orderInstanceId;
    private ArrayList<OrderInstance> orderInstancesArrayList;
    private ArrayList<ProductInstance> productInstancesNotYetProducedArrayList;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public OrderInstance getOrderInstance() {
        return orderInstance;
    }

    public void setOrderInstance(OrderInstance orderInstance) {
        this.orderInstance = orderInstance;
    }

    public String getOrderInstanceId() {
        return orderInstanceId;
    }

    public void setOrderInstanceId(String orderInstanceId) {
        this.orderInstanceId = orderInstanceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public ArrayList<OrderInstance> getOrderInstancesArrayList() {
        return orderInstancesArrayList;
    }

    public void setOrderInstancesArrayList(ArrayList<OrderInstance> orderInstancesArrayList) {
        this.orderInstancesArrayList = orderInstancesArrayList;
    }
    
    public ArrayList<ProductInstance> getProductInstancesNotYetProducedArrayList() {
        return productInstancesNotYetProducedArrayList;
    }

    public void setProductInstancesNotYetProducedArrayList(ArrayList<ProductInstance> productInstancesNotYetProducedArrayList) {
        this.productInstancesNotYetProducedArrayList = productInstancesNotYetProducedArrayList;
    }
    
}
