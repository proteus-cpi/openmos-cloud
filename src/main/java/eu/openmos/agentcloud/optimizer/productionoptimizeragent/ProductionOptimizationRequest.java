package eu.openmos.agentcloud.optimizer.productionoptimizeragent;

import eu.openmos.agentcloud.utilities.OptimizationParameter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The ProductionOptimizationRequest class contains the information that should be sent to the production optimizer agent in order for it to perform the different optimization related tasks
 * 
 * @author Luis Ribeiro
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class ProductionOptimizationRequest {

    /**
     * INITIALIZE is a constant that defines that the production optimizer agent should initialize the optimization algorithm
     */
    public static final int INITIALIZE = 0;

    /**
     * OPTIMIZE is a constant that defines that the production optimizer agent should optimize the system using user approved recipes, deploy them and activate them
     */
    public static final int OPTIMIZE = 1;

    /**
     * STOP is a constant that defines that the production optimizer agent should stop the optimization
     */
    public static final int STOP = 2;

    /**
     * RESET is a constant that defines that the production optimizer agent should reset the the optimization algorithm
     */
    public static final int RESET = 3;

    /**
     * REPARAMETRIZE is a constant that defines that the production optimizer agent should re-parametrize the the optimization algorithm
     */
    public static final int REPARAMETRIZE = 4;

    /**
     * SUGGEST_OPTIMIZE is a constant that defines that the production optimizer agent should optimize the system using dynamically created and non approved recipes
     */
    public static final int SUGGEST_OPTIMIZE = 5;

    private final int type;
    private final List<OptimizationParameter> parameters;

    /**
     * The constructor
     * 
     * @param type is the type of request
     * @param parameters is as list of optimization parameter if the request is for reparametrization
     */
    public ProductionOptimizationRequest(int type, List<OptimizationParameter> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    /**
     * Getter for Type
     * 
     * @return the type of the request
     */
    public int getType() {
        return type;
    }

    /**
     * Getter for Parameters     
     * 
     * @return the list of parameters
     */
    public List<OptimizationParameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(type);
        builder.append(eu.openmos.model.utilities.SerializationConstants.TOKEN_PRODUCTION_OPTIMIZATION_REQUEST);
        for(OptimizationParameter p : parameters) {
            builder.append(p.toString());
            builder.append(eu.openmos.model.utilities.SerializationConstants.TOKEN_PRODUCTION_OPTIMIZATION_REQUEST_LIST_ITEM);
        }
        return builder.toString();
    }    

    /**
     * The fromString method takes a String description, of an ProductionOptimizationRequest, created with the toString method on this class and creates the corresponding object
     * 
     * @param object is the String description of the object
     * @return the new object created from the String
     */
    public static ProductionOptimizationRequest fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.model.utilities.SerializationConstants.TOKEN_PRODUCTION_OPTIMIZATION_REQUEST);
        int type = Integer.parseInt(tokenizer.nextToken());
        StringTokenizer parametersTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.model.utilities.SerializationConstants.TOKEN_PRODUCTION_OPTIMIZATION_REQUEST_LIST_ITEM);
        List<OptimizationParameter> parameters = new LinkedList<>();
        while(parametersTokenizer.hasMoreTokens()) {
            String token = parametersTokenizer.nextToken();
            if(!token.isEmpty())
                parameters.add(OptimizationParameter.fromString(token));
        }
        return new ProductionOptimizationRequest(type, parameters);
    }
    
    /**
     * The validate method is s placeholder for a future validation function that asserts the consistency of the request.
     * 
     * @return always true in the current implementation
     */
    public boolean validate(){
        return true;
    }

}
