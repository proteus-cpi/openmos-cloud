/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.optimizer.productionoptimizer;

import eu.openmos.agentcloud.utilities.OptimizationParameter;
import eu.openmos.model.Recipe;
// import eu.openmos.agentcloud.data.recipe.Recipe;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @author valerio.gentile
 */
@WebService
public interface ProductionOptimizer 
{
    @WebMethod(operationName = "optimize")
    @WebResult(name="optimizedRecipes")
    public List<Recipe> optimize();

    @WebMethod(operationName = "initializeOptimizer")
    @WebResult(name="productionOptimizerResponseBean")
    public ProductionOptimizerResponseBean initializeOptimizer();

    @WebMethod(operationName = "stopOptimizer")
    @WebResult(name="productionOptimizerResponseBean")
    public ProductionOptimizerResponseBean stopOptimizer();

    @WebMethod(operationName = "resetOptimizer")
    @WebResult(name="productionOptimizerResponseBean")
    public ProductionOptimizerResponseBean resetOptimizer();

    @WebMethod(operationName = "reparametrizeOptimizer")
    @WebResult(name="productionOptimizerResponseBean")
    public ProductionOptimizerResponseBean reparametrizeOptimizer(@WebParam(name = "newParameters") List<OptimizationParameter> newParameters);

    @WebMethod(operationName = "isOptimizable")
    @WebResult(name="productionOptimizerResponseBean")
    public ProductionOptimizerResponseBean isOptimizable();
    
}
