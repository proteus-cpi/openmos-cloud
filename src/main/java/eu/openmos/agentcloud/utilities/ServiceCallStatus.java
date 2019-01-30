/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.utilities;

/**
 * Object to be returned as an operation status for agent creation and removal operations.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ServiceCallStatus {

    /**
     * Code for an OK operation.
     */
    public static final String OK = "success.openmos.agentcloud.cloudinterface.systemconfigurator";

    /**
     * Code for a KO operation.
     */
    public static final String KO = "error.openmos.agentcloud.cloudinterface.systemconfigurator";

    /**
     * Code for message describing a successful agent creation operation call.
     */
    public static final String CREATENEWAGENT_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.createnewagent.call.success";
    public static final String NEWPRODUCTDEFINITION_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.newproductdefinition.call.success";
//    /**
//     * Code for message describing a successful agent creation operation.
//     */
//    public static final String CREATENEWAGENT_SUCCESS = "agentcloud.CI.systemconfigurator.createnewagent.success";
    /**
     * Code for message describing a successful agent removal operation call.
     */
    public static final String REMOVEAGENT_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.removeagent.call.success";
//    /**
//     * Code for message describing a successful agent removal operation.
//     */
//    public static final String REMOVEAGENT_SUCCESS = "agentcloud.CI.systemconfigurator.removeagent.success";
    /**
     * Code for message describing a missing parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_PARAMETER_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.parameter.null";
    /**
     * Code for message describing a missing id parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_ID_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.id.null";
    /**
     * Code for message describing a missing agent type parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_TYPE_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.type.null";
    /**
     * Code for message describing an unknown agent type parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_TYPE_UNKNOWN = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.type.unknown";
    /**
     * Code for message describing missing agent parameters during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_PARAMETERS_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.parameters.null";
    /**
     * 
     */
    // public static final String REMOVEAGENT_EXISTINGAGENTINFO_TYPE_NULL = "agentcloud.CI.systemconfigurator.removeagent.existingagentinfo.type.null";
    /**
     * Code for message describing a missing agent name parameter during agent removal operation.
     */
    public static final String REMOVEAGENT_EXISTINGAGENTINFO_PARAMETER_NULL = "agentcloud.CI.systemconfigurator.removeagent.existingagentinfo.parameter.null";
    public static final String FINISHEDPRODUCT_EXISTINGAGENTINFO_PARAMETER_NULL = "agentcloud.CI.systemconfigurator.finishedproduct.existingagentinfo.parameter.null";

    public static final String UPDATEEXECUTIONTABLE_EXISTINGAGENTINFO_PARAMETER_NULL = "agentcloud.CI.systemconfigurator.updateexecutiontable.existingagentinfo.parameter.null";
    public static final String UPDATEEXECUTIONTABLE_CALL_SUCCESS  = "agentcloud.CI.systemconfigurator.updateexecutiontable.call.success";
    public static final String UPDATEEXECUTIONTABLE_CALL_FAILURE  = "agentcloud.CI.systemconfigurator.updateexecutiontable.call.failure";
    
    /**
     *
     */
    // public static final String REMOVEAGENT_EXISTINGAGENTINFO_ID_NULL = "agentcloud.CI.systemconfigurator.removeagent.existingagentinfo.id.null";
    /**
     *
     */
    // public static final String REMOVEAGENT_RESOURCE_NOT_FOUND = "agentcloud.CI.systemconfigurator.removeagent.resource.not.found";

    /**
     * Code for message describing a successful order submission.
     */
    public static final String ACCEPTNEWORDER_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.acceptneworder.call.success";    
    /**
     * Code for message describing a failure of the order submission operation.
     */
    public static final String ACCEPTNEWORDER_CALL_FAILURE = "agentcloud.CI.systemconfigurator.acceptneworder.call.failure";    

    /**
     * Code for message describing a successful order update.
     */
    public static final String UPDATEORDER_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.updateorder.call.success";    
    /**
     * Code for message describing a failure of the order update operation.
     */
    public static final String UPDATEORDER_CALL_FAILURE = "agentcloud.CI.systemconfigurator.updateorder.call.failure";    

    public static final String DEPLOYMENTAGENT_NULL = "agentcloud.CI.systemconfigurator.deploymentagent.null";
    public static final String CLOUDINTERFACEAGENT_NULL = "agentcloud.CI.systemconfigurator.cloudinterfaceagent.null";
    
    /**
    * WP4 Cloud Platform Re-work related code.
    * 
    * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
    * @author Valerio Gentile <valerio.gentile@we-plus.eu>
    */
    
    /**
     * Code for message describing a successful recipe creation operation call.
     */
    public static final String CREATENEWRECIPE_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.createnewrecipe.call.success";
    
    /**
     * Code for message describing a successful skill creation operation call.
     */
    public static final String CREATENEWSKILL_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.createnewskill.call.success";
    
    /**
     * Code for message describing a successful finished product operation call.
     */
    public static final String FINISHEDPRODUCT_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.finishedproduct.call.success";
    
    /**
     * Code for message describing a successful order removal operation call.
     */
    public static final String ORDERREMOVAL_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.orderremoval.call.success";
    public static String PUTRECIPES_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.putrecipes.call.success";
    public static String STARTEDPRODUCT_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.startedproduct.call.success";
    public static String UPDATEDPRODUCT_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.updatedproduct.call.success";
    public static String UPDATEDPRODUCT_CALL_ERROR = "agentcloud.CI.systemconfigurator.updatedproduct.call.failure";
    public static String NEWRECIPEEXECUTIONDATA_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.newrecipeexecutiondata.call.success";
    public static String CHANGE_SYSTEM_STATUS_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.changesystemstatus.call.success";
    public static String CHANGE_SUBSYSTEM_STATUS_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.changesubsystemstatus.call.success";

    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Constructor.
     * 
     * @param className   Java class name
     * @param operationName   Operation, or java method name
     * @param code   Message code
     * @param description    Message description
     */
    public ServiceCallStatus(String className, String operationName, String code, String description) {
        this.code = code;
        this.description = description;
        this.className = className;
        this.operationName = operationName;
    }

    
    /**
     * Default constructor, for reflection purpose.
     */
    public ServiceCallStatus() {
    }

    private String code;
    private String description;
    private String className;
    private String operationName;
          

}
