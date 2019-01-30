/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.optimizer.productionoptimizer;

// import eu.openmos.datacloudservices.client.MessageTrackerWSClient;
import eu.openmos.agentcloud.utilities.OptimizationParameter;
import eu.openmos.agentcloud.optimizer.OptimizerAgent;
// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.model.Recipe;
import java.util.LinkedList;
import java.util.List;
import javax.jws.WebService;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
@WebService(endpointInterface = "eu.openmos.agentcloud.optimizer.productionoptimizer.ProductionOptimizer", serviceName = "ProductionOptimizer")
public class ProductionOptimizerImpl implements ProductionOptimizer 
{
    private OptimizerAgent a;

    private static final Logger logger = Logger.getLogger(ProductionOptimizerImpl.class.getName());
    
    public ProductionOptimizerImpl(OptimizerAgent a)
    {
        this.a = a;
    }

    public ProductionOptimizerImpl()
    {
    }

    @Override
    public List<Recipe> optimize() {
        logger.info("optimize");
        // MessageTrackerWSClient.track("optimize", "productionoptimizer", "");
        return new LinkedList(); // ProductionOptimizerResponseBean(ProductionOptimizerResponseBean.RESULT_CODE_OK, ProductionOptimizerResponseBean.DESCRIPTION_RESULT_CODE_OK);
    }

    @Override
    public ProductionOptimizerResponseBean initializeOptimizer() {
        logger.info("initializeOptimizer");
        // MessageTrackerWSClient.track("initializeOptimizer", "productionoptimizer", "");
        return new ProductionOptimizerResponseBean(ProductionOptimizerResponseBean.RESULT_CODE_OK, ProductionOptimizerResponseBean.DESCRIPTION_RESULT_CODE_OK);
    }

    @Override
    public ProductionOptimizerResponseBean stopOptimizer() {
        logger.info("stopOptimizer");
        // MessageTrackerWSClient.track("stopOptimizer", "productionoptimizer", "");
        return new ProductionOptimizerResponseBean(ProductionOptimizerResponseBean.RESULT_CODE_OK, ProductionOptimizerResponseBean.DESCRIPTION_RESULT_CODE_OK);
    }

    @Override
    public ProductionOptimizerResponseBean resetOptimizer() {
        logger.info("resetOptimizer");
        // MessageTrackerWSClient.track("resetOptimizer", "productionoptimizer", "");
        return new ProductionOptimizerResponseBean(ProductionOptimizerResponseBean.RESULT_CODE_OK, ProductionOptimizerResponseBean.DESCRIPTION_RESULT_CODE_OK);
    }

    @Override
    public ProductionOptimizerResponseBean reparametrizeOptimizer(List<OptimizationParameter> newParameters) {
        logger.info("reparametrizeOptimizer");
        // MessageTrackerWSClient.track("reparametrizeOptimizer", "productionoptimizer", "");
        return new ProductionOptimizerResponseBean(ProductionOptimizerResponseBean.RESULT_CODE_OK, ProductionOptimizerResponseBean.DESCRIPTION_RESULT_CODE_OK);
    }

    @Override
    public ProductionOptimizerResponseBean isOptimizable() {
        logger.info("isOptimizable");
        // MessageTrackerWSClient.track("isOptimizable", "productionoptimizer", "");
        return new ProductionOptimizerResponseBean(ProductionOptimizerResponseBean.RESULT_CODE_OK, ProductionOptimizerResponseBean.DESCRIPTION_RESULT_CODE_OK);
    }
    
}
