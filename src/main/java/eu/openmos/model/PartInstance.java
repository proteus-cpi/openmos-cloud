/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author valerio.gentile
 */
public class PartInstance extends Base implements Serializable {
    
    private static final Logger logger = Logger.getLogger(PartInstance.class.getName());
    private static final long serialVersionUID = 6529685098267757119L;

    /**
     * Part instance unique identifier.
     */
    protected String uniqueId;
    /**
     * Part instance name.
     */
    protected String name;
    /**
     * Part instance description.
     */
    protected String description;
    
    /**
     * Pointer to the part (to the model part).
     */
    private Part part;

    /**
     * Empty constructor.
     */
    public PartInstance() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param uniqueId
     * @param name
     * @param description
     * @param part
     * @param registered 
     */
    public PartInstance(String uniqueId, String name, String description, Part part, Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.part = part;
    }
    
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }
    
     /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);        
        String stringRegisteredTimestamp = sdf.format(this.registered);
        
        String partId = null;
        if (part != null)
            partId = part.getUniqueId();
        
        return new Document()
                .append(DatabaseConstants.UNIQUE_ID, uniqueId)
                .append(DatabaseConstants.NAME, name)
                .append(DatabaseConstants.DESCRIPTION, description)
                .append(DatabaseConstants.PART_ID, partId)
                .append(DatabaseConstants.REGISTERED, stringRegisteredTimestamp);
    }
    
}
