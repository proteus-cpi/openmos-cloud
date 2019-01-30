package eu.openmos.agentcloud.cloudinterfaceagent.data;

import eu.openmos.model.Base;
import eu.openmos.model.Product;
import java.io.Serializable;

public class ProductDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757720L;      
    
    private Product product;    
    private String productId;

    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
}
