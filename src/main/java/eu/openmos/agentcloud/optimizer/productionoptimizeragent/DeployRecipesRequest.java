package eu.openmos.agentcloud.optimizer.productionoptimizeragent;

// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.model.Recipe;
import jade.core.AID;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The DeployRecipesRequest class contains the information that should be sent to an agent in order for it to deploy a set of recipes
 * 
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 */
public class DeployRecipesRequest {

    /**
     * DEPLOY is a constant that defines the deploy request as containing user approved recipes for deployment
     */
    public final static int DEPLOY = 0;

    /**
     * DEPLOY_SUGGEST is a constant that defines the deploy request as containing suggested and not approved recipes for deployment
     */
    public final static int DEPLOY_SUGGEST = 1;

    /**
     * SOP is a constant that defines the deploy request as an activation request
     */
    public final static int SOP = 2;    
    
    private final List<Recipe> allRecipesToBeDeployed;
    private final int type;

    /**
     * The constructor
     * 
     * @param allRecipesToBeDeployed is a list of all the recipes to de considered
     * @param type is the type of deployment to be considered
     */
    public DeployRecipesRequest(List<Recipe> allRecipesToBeDeployed, int type) {
        this.allRecipesToBeDeployed = allRecipesToBeDeployed;
        this.type = type;
    }  

    /**
     * The getRecipesToBeDeployed method returns a list of all the recipes that are to be deployed in one resource with a specific AID
     * 
     * @param targetAgent is the agent whose recipes are to be extracted from the list
     * @return the list of recipes to be deployed by that agent
     */
    public List<Recipe> getRecipesToBeDeployed(AID targetAgent) {
        List<Recipe> result = new LinkedList<>();
        allRecipesToBeDeployed.stream().filter((r) -> (new AID(r.getUniqueAgentName(), false) == targetAgent)).forEach((r) -> {
            result.add(r);
        });
        return result;
    }

    /**
     * Getter for type
     * 
     * @return the type of the deployment
     */
    public int getType() {
        return type;
    }        

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        for(Recipe r : allRecipesToBeDeployed) {
//            builder.append(r.toString());
//            builder.append(eu.openmos.model.utilities.SerializationConstants.TOKEN_DEPLOY_RECIPES_REQUEST_LIST_ITEM);
//        }
//        builder.append(eu.openmos.model.utilities.SerializationConstants.TOKEN_DEPLOY_RECIPES_REQUEST);
//        builder.append(type);
//        builder.append(eu.openmos.model.utilities.SerializationConstants.TOKEN_DEPLOY_RECIPES_REQUEST);
//        return builder.toString();
//    }

    /**
     * The fromString method takes a String description, of an DeployRecipesRequest, created with the toString method on this class and creates the corresponding object
     * 
     * @param object is the String description of the object
     * @return the new object created from the String
     */
//    public static DeployRecipesRequest fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.model.utilities.SerializationConstants.TOKEN_DEPLOY_RECIPES_REQUEST);
//        StringTokenizer recipesToBeDeployedTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.model.utilities.SerializationConstants.TOKEN_DEPLOY_RECIPES_REQUEST_LIST_ITEM);
//        List<Recipe> recipesToBeDeployed = new LinkedList<>();
//        while(recipesToBeDeployedTokenizer.hasMoreTokens()) {
//            String token = recipesToBeDeployedTokenizer.nextToken();
//            if(!token.isEmpty())
////                recipesToBeDeployed.add(Recipe.fromString(token));
//                recipesToBeDeployed.add(Recipe.deserialize(token));
//        }
//        return new DeployRecipesRequest(recipesToBeDeployed, Integer.parseInt(tokenizer.nextToken()));
//    }

    /**
     * The validate method is s placeholder for a future validation function that asserts the consistency of the request.
     * 
     * @return always true in the current implementation
     */
    public boolean validate() {
        return true;
    }

}
