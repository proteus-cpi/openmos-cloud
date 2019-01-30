package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class TriggeredRecipe extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(TriggeredRecipe.class.getName());    
    private static final long serialVersionUID = 6529685098267760001L;
    
    private String uniqueId;
    private String recipeId;
    private String productInstanceId;
    private String userName;
    private boolean successfully = true;    

    public TriggeredRecipe() {
        super();
    }

    public TriggeredRecipe(String uniqueId, String recipeId, String productInstanceId, String userName, boolean successfully, Date registered) {
        super(registered);
        this.uniqueId = uniqueId;
        this.recipeId = recipeId;
        this.productInstanceId = productInstanceId;
        this.userName = userName;
        this.successfully = successfully;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(String productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }    

    public boolean isSuccessfully() {
        return successfully;
    }

    public void setSuccessfully(boolean successfully) {
        this.successfully = successfully;
    }

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
    }   
    
    /**
     * Method that deserializes a BSON object.
     * 
     * @param bsonTriggeredRecipe - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static TriggeredRecipe fromBSON(Document bsonTriggeredRecipe) {   
        return (TriggeredRecipe)TriggeredRecipe.fromBSON2(bsonTriggeredRecipe, TriggeredRecipe.class);
    }
}
