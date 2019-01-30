/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cyberphysicalagent;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ProductLeavingWorkstationOrTransportData;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.UnexpectedProductData;
import eu.openmos.model.SubSystem;

/**
 *
 * @author valerio.gentile
 */
public interface WebSocketMessageHandler {
    
//    public void msgTypeLifeBeat(String msg);  // for both
    public void msgTypeLifeBeat(RawEquipmentData msg);  // for both
    
//    public void msgTypeExtractedData(String msg);  // for both
    public void msgTypeExtractedData(RawEquipmentData msg);  // for both
    
//    public void msgTypeAppliedRecipes(String msg);  // for resources
    public void msgTypeAppliedRecipes(RecipeExecutionData msg);  // for resources
    
    /**
     * WP4 semantic model alignment
     * Workstation got different product than what expected
     * Message for resource agents.
     */
//    public void msgTypeUnexpectedProduct(String msg);
    public void msgTypeUnexpectedProduct(UnexpectedProductData msg);
    /**
     * WP4 semantic model alignment
     * Product arrived at workstation / transport
     * Message for resource and transport agents.
     */
//    public void msgTypeProductArrived(String msg);
    public void msgTypeProductArrived(UnexpectedProductData msg);
    /**
     * WP4 semantic model alignment
     * Product leaving workstation / transport
     * Message for resource and transport agents.
     */
//    public void msgTypeProductLeavingWorkstationOrTransport(String msg);
    public void msgTypeProductLeavingWorkstationOrTransport(ProductLeavingWorkstationOrTransportData msg);
    /**
     * WP4 semantic model alignment
     * New product location (it covers the inotec tracker)
     * Message for product agents.
     */
    public void msgTypeNewLocation(String msg);
    /**
     * WP4 semantic model alignment
     * Recipe execution data 
     * TBV Message for ? agents.
     */
//    public void msgTypeRecipeExecutionData(String msg);
    public void msgTypeRecipeExecutionData(RecipeExecutionData msg);
    /**
     * WP4 semantic model alignment
     * Workstation update
     * TBV Message for resource agents.
     */
//    public void msgTypeWorkstationUpdate(String msg);
    public void msgTypeWorkstationUpdate(SubSystem msg);
    
    public void msgTypeExecutionTableUpdate(ExecutionTable msg);    
}
