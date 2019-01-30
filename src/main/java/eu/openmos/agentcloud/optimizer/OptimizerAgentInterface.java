/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.optimizer;

import eu.openmos.agentcloud.utilities.OptimizationParameter;
import java.util.List;

/**
 * The OptimizerAgentInterface describes the main methods that an optimizer should implement in order to support a custom optimization algorithm
 * 
 * @author Luis Ribeiro<luis.ribeiro@liu.se>
 */
public interface OptimizerAgentInterface {

    /**
     * The setOptimizationAlgorithm method updates the optimization algorithm to be used by the optimizer agent interface implementation
     * 
     * @param optimizationAlgorithm the algorithm to be considered
     */
    public void setOptimizationAlgorithm(PluggableOptimizerInterface optimizationAlgorithm);

    /**
     * The initializeOptimizer method initializes the optimization algorithm to be used by the optimizer agent interface implementation
     * 
     * @return true if the optimization succeeded and false otherwise
     */
    public boolean initializeOptimizer();

    /**
     * The stopOptimizer method stops the optimization algorithm to be used by the optimizer agent interface implementation
     * 
     * @return true if the stop succeeded and false otherwise
     */
    public boolean stopOptimizer();

    /**
     * The resetOptimizer method resets the optimization algorithm to be used by the optimizer agent interface implementation
     * 
     * @return true if the reset succeeded and false otherwise
     */
    public boolean resetOptimizer();

    /**
     * The reparametrizeOptimizer method resets the optimization algorithm to be used by the optimizer agent interface implementation
     * 
     * @param newParameters the new parameters to be used by the optimization algorithm. These parameters do not refer to system data that form the base of the optimization but rather to algorithm specific parameters.
     * @return true if the re-parametrization succeeded and false otherwise
     */
    @Deprecated
    public boolean reparametrizeOptimizer(List<OptimizationParameter> newParameters);

    /**
     * The isOptimizable method test if the system can be optimized given its current state
     * @return true if the system is optimizable and false otherwise
     */
    public boolean isOptimizable();

}
