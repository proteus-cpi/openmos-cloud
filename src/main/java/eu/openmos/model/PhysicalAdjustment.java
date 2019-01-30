package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Concrete class for equipment physical adjustments.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class PhysicalAdjustment extends EquipmentAdjustment implements Serializable {
    private static final Logger logger = Logger.getLogger(PhysicalAdjustment.class.getName());    
    private static final long serialVersionUID = 6529685098267757992L;
    
    private PhysicalAdjustmentParameterSetting physicalAdjustmentParameterSetting;
    private String physicalAdjustmentType;

    public PhysicalAdjustment() {
        super();
    }

    public PhysicalAdjustment(String uniqueId, String name, String description, String equipmentId, 
            String equipmentType, 
            PhysicalAdjustmentParameterSetting physicalAdjustmentParameterSetting, 
            String physicalAdjustmentType,
            String userText, String userName, String systemStage, Date registered) {
        super(uniqueId, name, description, equipmentId, equipmentType, 
                /* physicalAdjustmentParameterSetting, */ 
                userText, userName, systemStage, registered);

        this.physicalAdjustmentParameterSetting = physicalAdjustmentParameterSetting;     
        this.physicalAdjustmentType = physicalAdjustmentType;
    }
    
    public PhysicalAdjustmentParameterSetting getPhysicalAdjustmentParameterSetting() {
        return physicalAdjustmentParameterSetting;
    }

    public void setPhysicalAdjustmentParameterSetting(PhysicalAdjustmentParameterSetting physicalAdjustmentParameterSetting) {
        this.physicalAdjustmentParameterSetting = physicalAdjustmentParameterSetting;
    }

    public String getPhysicalAdjustmentType() {
        return physicalAdjustmentType;
    }

    public void setPhysicalAdjustmentType(String physicalAdjustmentType) {
        this.physicalAdjustmentType = physicalAdjustmentType;
    }

    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        
        doc.append(DatabaseConstants.EQUIPMENT_ID, equipmentId);
        doc.append(DatabaseConstants.EQUIPMENT_TYPE, equipmentType);
        
        doc.append(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_SETTING_ID, physicalAdjustmentParameterSetting.getUniqueId());
        doc.append(DatabaseConstants.PHYSICAL_ADJUSTMENT_TYPE, physicalAdjustmentType);
        
        doc.append(DatabaseConstants.USER_TEXT, userText);
        
        doc.append(DatabaseConstants.USER_NAME, userName);
        
        doc.append(DatabaseConstants.SYSTEM_STAGE, systemStage);
        
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("PHYSICAL ADJUSTMENT TOBSON: " + doc.toString());
        
        return doc;
    }
}