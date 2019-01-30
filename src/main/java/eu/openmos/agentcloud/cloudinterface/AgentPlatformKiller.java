package eu.openmos.agentcloud.cloudinterface;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Agent Platform Killer, an internal service for graceful shutdown of the agents before the Jade platform is closed.
 * This interface exposes one shutdown method.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@WebService
public interface AgentPlatformKiller {

    /**
     * Service for graceful shutdown of the agents into the platform.
     * 
     * @return status of the operation
     */
    @WebMethod(operationName = "shutdown")
    @WebResult(name="shutdownStatus")
    public int shutdown();

}
