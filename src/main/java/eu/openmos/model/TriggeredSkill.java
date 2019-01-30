package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class TriggeredSkill extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(TriggeredSkill.class.getName());    
    private static final long serialVersionUID = 6529685098267760002L;
    
    private String uniqueId;
    private String skillId;
    private String recipeId;
    private String productInstanceId;
    private String userName;
    private boolean successfully = true;  

    public TriggeredSkill() {
        super();
    }

    public TriggeredSkill(String uniqueId, String skillId, String recipeId, String productInstanceId, String userName, boolean successfully, Date registered) {
        super(registered);
        this.uniqueId = uniqueId;
        this.skillId = skillId;
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

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
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
     * @param bsonTriggeredSkill - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static TriggeredSkill fromBSON(Document bsonTriggeredSkill) {   
        return (TriggeredSkill)TriggeredSkill.fromBSON2(bsonTriggeredSkill, TriggeredSkill.class);
    }
}
