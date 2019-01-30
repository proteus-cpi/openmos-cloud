package eu.openmos.model;

import java.util.Date;
import java.io.Serializable;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Abstract class for equipment adjustments.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public abstract class EquipmentAdjustment extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(EquipmentAdjustment.class.getName());    
    private static final long serialVersionUID = 6529685098267757991L;
    
    /**
     * Adjustment ID.
     */
    protected String uniqueId;
    /**
     * Adjustment name.
     */
    protected String name;
    /**
     * Adjustment description.
     */
    protected String description;
    
    /**
     * Pointer to the physical equipment (subsystem or module) whom the adjustment is related to.
     * Can be null if the observation is related to the whole system.
     */
    protected String equipmentId;
    /**
     * Can be "system", "subSystem" or "module".
     */
    protected String equipmentType;
    
    protected String userText;
    
    // name of the user that register the adjustment
    protected String userName;

    // current status of the system while inserting the adjustment
    protected String systemStage;

    /**
     * Default constructor.
     */
    public EquipmentAdjustment() {super();}

    public EquipmentAdjustment(String uniqueId, String name, String description, 
            String equipmentId, String equipmentType,
//            PhysicalAdjustmentParameterSetting physicalAdjustmentParameterSetting,
            String userText,
            String userName,
            String systemStage,
            Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.equipmentId = equipmentId;
        this.equipmentType = equipmentType;
        
//        this.physicalAdjustmentParameterSetting = physicalAdjustmentParameterSetting;
        
        this.userText = userText;
        
        this.userName = userName;
        this.systemStage = systemStage;
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

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSystemStage() {
        return systemStage;
    }

    public void setSystemStage(String systemStage) {
        this.systemStage = systemStage;
    }

    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public abstract Document toBSON();
/*        
0    {
        Document doc = new Document();
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        
        doc.append(DatabaseConstants.EQUIPMENT_ID, equipmentId);
        doc.append(DatabaseConstants.EQUIPMENT_TYPE, equipmentType);
        
        doc.append(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_SETTING_ID, physicalAdjustmentParameterSetting.getUniqueId());
        
        doc.append(DatabaseConstants.USER_TEXT, userText);
        
        doc.append(DatabaseConstants.USER_NAME, userName);
        
        doc.append(DatabaseConstants.SYSTEM_STATUS, systemStage);
        
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("EQUIPMENT ADJUSTMENT TOBSON: " + doc.toString());
        
        return doc;
    }
*/
}