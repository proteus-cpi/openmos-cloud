package eu.openmos.agentcloud.services.rest.data;

import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * HMI representation of an equipment physical adjustment user insertion.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class HMIPhysicalAdjustment implements Serializable {
    private static final Logger logger = Logger.getLogger(HMIPhysicalAdjustment.class.getName());    
    private static final long serialVersionUID = 6529685098267757892L;

    private String equipmentId;
    private String equipmentType;
    private String systemStage;
    private String equipmentStage;
    private String userName;
    private String newValue;
    private String oldValue;
    private String physicalAdjustmentParameterId;
    
    public HMIPhysicalAdjustment() {
    }

    public HMIPhysicalAdjustment(String equipmentId, String equipmentType, 
            String systemStage, String equipmentStage, 
            String userName, String newValue, String oldValue, String physicalAdjustmentParameterId) {
        this.equipmentId = equipmentId;
        this.equipmentType = equipmentType;
        this.systemStage = systemStage;
        this.equipmentStage = equipmentStage;
        this.userName = userName;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.physicalAdjustmentParameterId = physicalAdjustmentParameterId;
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

    public String getSystemStage() {
        return systemStage;
    }

    public void setSystemStage(String systemStage) {
        this.systemStage = systemStage;
    }

    public String getEquipmentStage() {
        return equipmentStage;
    }

    public void setEquipmentStage(String equipmentStage) {
        this.equipmentStage = equipmentStage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getPhysicalAdjustmentParameterId() {
        return physicalAdjustmentParameterId;
    }

    public void setPhysicalAdjustmentParameterId(String physicalAdjustmentParameterId) {
        this.physicalAdjustmentParameterId = physicalAdjustmentParameterId;
    }
}