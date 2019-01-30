/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.optimizer.productionoptimizer;

/**
 *
 * @author valerio.gentile
 */
public class ProductionOptimizerResponseBean {
    
    public static final String RESULT_CODE_OK                                           = "success.openmos.agentcloud.cloudinterface.productionptimizer";
    public static final String RESULT_CODE_GENERIC_ERROR                                = "error.openmos.agentcloud.cloudinterface.productionoptimizer";
    public static final String DESCRIPTION_RESULT_CODE_OK                               = "OpenMOS agents cloudinterface production optimizer operation OK";
    public static final String DESCRIPTION_RESULT_CODE_GENERIC_ERROR                    = "OpenMOS agents cloudinterface production optimizer operation KO";

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

    public ProductionOptimizerResponseBean(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public ProductionOptimizerResponseBean() {
    }
    
    private String code;
    private String description;
    
}
