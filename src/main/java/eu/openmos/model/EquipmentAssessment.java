package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes user assessment concerning a physical equipment.
 * Holds a list of rows (EquipmentAssessmentRow).
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class EquipmentAssessment extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(EquipmentAssessment.class.getName());    
    private static final long serialVersionUID = 6529685098267757911L;
    
    /**
     * Observation ID.
     */
    private String uniqueId;
    /**
     * Observation name.
     */
    private String name;
    /**
     * Observation description.
     */
    private String description;
    
    /**
     * Pointer to the physical equipment (subsystem or module) whom the observation is related to.
     * Can be null if the observation is related to the whole system.
     */
    private String equipmentId;
    /**
     * Can be "system", "subSystem" or "module".
     */
    private String equipmentType;
    // VaG - 05/09/2018
    private String equipmentName;
    
    /**
     * List of details.
     */
    List<EquipmentAssessmentRow> rows;
    
    private String userText;
    
    // name of the user that register the assessment
    private String userName;

    // current status of the system while inserting the assessment
    private String systemStage;

    /**
     * Default constructor.
     */
    public EquipmentAssessment() {super();}

    public EquipmentAssessment(String uniqueId, String name, String description, 
            String equipmentId, String equipmentType,
            List<EquipmentAssessmentRow> rows,
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
        
        this.rows = rows;
        
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

    public List<EquipmentAssessmentRow> getRows() {
        return rows;
    }

    public void setRows(List<EquipmentAssessmentRow> rows) {
        this.rows = rows;
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
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> rowIds = null;
        if (rows != null)
            rowIds = rows.stream().map(row -> row.getUniqueId()).collect(Collectors.toList());
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        
        doc.append(DatabaseConstants.EQUIPMENT_ID, equipmentId);
        doc.append(DatabaseConstants.EQUIPMENT_TYPE, equipmentType);
        
        doc.append(DatabaseConstants.EQUIPMENT_ASSESSMENT_ROW_IDS, rowIds);
        
        doc.append(DatabaseConstants.USER_TEXT, userText);
        
        doc.append(DatabaseConstants.USER_NAME, userName);
        
        doc.append(DatabaseConstants.SYSTEM_STAGE, systemStage);
        
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("EQUIPMENT ASSESSMENT TOBSON: " + doc.toString());
        
        return doc;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

}