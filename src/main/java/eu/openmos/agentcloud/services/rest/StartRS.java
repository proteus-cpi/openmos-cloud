package eu.openmos.agentcloud.services.rest;

import com.sun.net.httpserver.HttpServer;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.apache.log4j.Logger;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author valerio.gentile
 */
public class StartRS {
    
    private final static int port = 9993;
    private final static String host="http://0.0.0.0/";

    private static final Logger logger = Logger.getLogger(StartRS.class.getName());

public static void main(String[] args)
    {
        
        logger.info("StartRS - starting...");
/*        
        URI baseUri = UriBuilder.fromUri(host).port(port).build();
        
//                .register(new Resource(new Core(), configuration)) // create instance of Resource and dynamically register        
        ResourceConfig resourceConfig = new ResourceConfig()
                .register(EquipmentObservationController.class)
                .register(new CORSFilter());
        
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, resourceConfig);    
*/
        StartRS.start();
        
        logger.info("StartRS - started succesfully");        
    }

public static void start()
    {
        logger.info("StartRS - starting...");
        URI baseUri = UriBuilder.fromUri(host).port(port).build();
        
//                .register(new Resource(new Core(), configuration)) // create instance of Resource and dynamically register        
        ResourceConfig resourceConfig = new ResourceConfig()
                .register(EquipmentObservationController.class)
                .register(EquipmentObservationRel2Controller.class)
                .register(EquipmentAssessmentController.class)
                .register(RecipeExecutionDataController.class)
                .register(OrderInstanceController.class)
                .register(ProcessAssessmentController.class)
                .register(PhysicalAdjustmentController.class)
                .register(SubSystemController.class)
                .register(TriggersController.class)
                .register(new CORSFilter());
        
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, resourceConfig);    
        logger.info("StartRS - started succesfully");        
    }
}
