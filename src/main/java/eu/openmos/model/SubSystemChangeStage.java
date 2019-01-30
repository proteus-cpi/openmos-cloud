package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.Document;

/**
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class SubSystemChangeStage extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757498L;    

    private String uniqueId;
    private String subSystemId;
    private String newStage;

    // reflection stuff
    public SubSystemChangeStage() { super();    }

    public SubSystemChangeStage(
            String uniqueId,
            String subSystemId,
            String newStage,
            Date registeredTimestamp) {
        
        super(registeredTimestamp);

        this.subSystemId = subSystemId;                
        this.newStage = newStage;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String subSystemId) {
        this.subSystemId = subSystemId;
    }    
    
    public String getNewStage() {
        return newStage;
    }

    public void setNewStage(String newStage) {
        this.newStage = newStage;
    }

//    public Document toBSON() {
//        return toBSON2();
//    }
    public Document toBSON() {
        Document doc = new Document();
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.SUBSYSTEM_ID, subSystemId);
        doc.append(DatabaseConstants.SUBSYSTEM_STAGE, newStage);
        doc.append(DatabaseConstants.REGISTERED, this.registered == null ? "null" : sdf.format(this.registered));
        
        return doc;
    }
    
}
