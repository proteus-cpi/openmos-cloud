/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cyberphysicalagent;

import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.NetworkConfig;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.cyberphysicalagent.WebSocketMessageHandler;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.model.ProductLeavingWorkstationOrTransportData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.SubSystem;
import eu.openmos.model.UnexpectedProductData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Wrapper for vert.x websocket communication. 
 * Some messages / updates of status for resource and transport agents come from the Manifacturing Service Bus to the Agent Cloud in a peer-to-peer fashion,
 * not via the formal agent-cloud access point (web-services exposed by the cloud interface agent) but directly to the agent.
 * This happens via a channel created at time of agent creation, channel that has the same name of the agent.
 * 
 * Via one single channel, the agent can receive multiple types of messages: every message starts with a message-type-identifier, followed by the message data.
 * Message-types handled by the receiver are:
 * MSB_MESSAGE_TYPE_LIFEBEAT
 * MSB_MESSAGE_TYPE_EXTRACTEDDATA
 * MSB_MESSAGE_TYPE_NEWLOCATION
 * MSB_MESSAGE_TYPE_APPLIEDRECIPES
 * MSB_MESSAGE_TYPE_SUICIDE for closing the connection
 * MSB_MESSAGE_TYPE_UNKNOWN for messages of an unknown type.
 * 
 * This class holds an internal table with the time of last-received-message for every message type.
 * 
 * From a technical point-of-view, the websocket infrastructure is offered by Vert.X stack (http://vertx.io)
 * and the channel is a vertx verticle.
 * 
 * @see http://vertx.io
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class WebSocketReceiver extends AbstractVerticle 
{
    String topic;
    String agentName;
    WebSocketMessageHandler wsmh;
    private static final Logger logger = Logger.getLogger(WebSocketReceiver.class.getName());
    private Long notPreciseLastReceivedMessageTime;
    private HashMap<String, Long> notPreciseLastReceivedMessageTimeByType;
    
    /**
     * Constructor.
     * 
     * @param _topic The websocket channel name, e.g. the agent name
     * @param _wsmh Reference to the agent implementing eu.openmos.agentcloud.cyberphysicalagent.WebSocketMessageHandler interface, with callback methods for message handling.
     */
    public WebSocketReceiver(String _topic /* it matches the agent name */, WebSocketMessageHandler _wsmh /* resource or trasnport agent */ )
    {
        topic = _topic;
        agentName = _topic;  // they are the same
        wsmh = _wsmh;
        notPreciseLastReceivedMessageTime = null;
        notPreciseLastReceivedMessageTimeByType = new HashMap();
        notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_LIFEBEAT, null);
        notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA, null);
        notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_NEWLOCATION, null);
        notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES, null);
        notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_SUICIDE, null);        
        notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_UNKNOWN, null);        

        logger.info(getInfo() + " - web socket receiver initialized");
    }

    /**
     * It returns time of the last-received-message, regardless of the type of message.
     * 
     * @return last-received message time
     */
    public Long getNotPreciseLastMessageReceptionTime() {
        return notPreciseLastReceivedMessageTime;
    }
    
    /**
     * It returns time of the last-received-message given a message-type.
     * 
     * @param messageType Type of the message
     * @return last-received message time for the given type
     */
    public Long getNotPreciseLastMessageReceptionTime(String messageType) {
        return notPreciseLastReceivedMessageTimeByType.get(messageType);
    }
    
    /**
     * Method that starts the communication, listens for different messages, addresses them to the agent according with the message type,
     * saves time of last message for message-type, saves time for last message.
     * When a MSB_MESSAGE_TYPE_SUICIDE is received, it sends back a "STOP_SENDING" reply and it undeploys the verticle.
     * 
     * @throws Exception
     */
    @Override
  public void start() throws Exception 
  {                  
        logger.info(getClass().getName() + " - websocket start call [" + topic + "] for agent [" + agentName + "]");

      String myIP = ConfigurationLoader.getMandatoryProperty(Constants.PLATFORM_HOST_PARAMETER);
      logger.debug(getInfo() + " - myIP = [" + myIP + "]");

//////////////////////
Config hazelcastConfig = new Config();
NetworkConfig networkConfig = new NetworkConfig();
InterfacesConfig interfacesConfig = new InterfacesConfig();
interfacesConfig.addInterface(myIP);
interfacesConfig.setEnabled(true);
networkConfig.setInterfaces(interfacesConfig);
hazelcastConfig.setNetworkConfig(networkConfig);
ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
//////////////////////

    VertxOptions options = new VertxOptions();
    //
    options.setClusterManager(mgr);
    //
    ////////////////////////////////////////options.setClustered(true).setClusterHost(myIP);
    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        EventBus eventBus = vertx.eventBus();
        logger.debug(getInfo() + " - We now have a clustered event bus: " + eventBus);
        eventBus.consumer(topic, message -> {

          MultiMap messageHeaders =  message.headers();
          // look for message type header
          String messageType = messageHeaders.get("messageType");
          logger.info("messageType received = " + messageType);
          
          // String myMessageBody = (String)message.body();
          // logger.debug(getInfo() + " - Received message: " + myMessageBody);
          
          Long notPreciseMessageReceptionTime = Calendar.getInstance().getTimeInMillis();
          this.notPreciseLastReceivedMessageTime = notPreciseMessageReceptionTime;
          
/*
// Create a JsonObject from the fields of a Java object.
// Faster than calling `new JsonObject(Json.encode(obj))`.
public static JsonObject mapFrom(Object obj)

// Instantiate a Java object from a JsonObject.
// Faster than calling `Json.decodeValue(Json.encode(jsonObject), type)`.
public <T> T mapTo(Class<T> type)
                        */                        
          
          // based on myMessageBody format, do A or B or C
          // if (myMessageBody != null)
          if (messageType != null)
          {
//              if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_LIFEBEAT))
              if (messageType.equals(Constants.MSB_MESSAGE_TYPE_LIFEBEAT))
                {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_LIFEBEAT, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_LIFEBEAT);
//                    wsmh.msgTypeLifeBeat(myMessageBody);
                    JsonObject jo = (JsonObject)message.body();
                    RawEquipmentData red = jo.mapTo(RawEquipmentData.class);
                    wsmh.msgTypeLifeBeat(red);
                }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA);                  
//                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA, "");
//                    wsmh.msgTypeExtractedData(realMSBMessage);
//                    wsmh.msgTypeExtractedData((RawEquipmentData)message.body());
                    JsonObject jo = (JsonObject)message.body();
                    RawEquipmentData red = jo.mapTo(RawEquipmentData.class);
                    wsmh.msgTypeExtractedData(red);
              }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_NEWLOCATION))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_NEWLOCATION))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_NEWLOCATION, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_NEWLOCATION);                  
//                    String myMessageBody = (String)message.body();
//                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_NEWLOCATION, "");
//                    wsmh.msgTypeNewLocation(realMSBMessage);
                    JsonObject jo = (JsonObject)message.body();
                    String str = jo.mapTo(String.class);
                    wsmh.msgTypeNewLocation(str);
              }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES);                  
                    String myMessageBody = (String)message.body();
                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES, "");
                    // TBV wsmh.msgTypeNewLocation(realMSBMessage);
              }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA);                  
//                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA, "");
//                    wsmh.msgTypeUnexpectedProduct(realMSBMessage);
//                    wsmh.msgTypeUnexpectedProduct((UnexpectedProductData)message.body());
                    JsonObject jo = (JsonObject)message.body();
                    UnexpectedProductData str = jo.mapTo(UnexpectedProductData.class);
                    wsmh.msgTypeUnexpectedProduct(str);
              }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT);                  
//                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT, "");
//                    wsmh.msgTypeProductLeavingWorkstationOrTransport(realMSBMessage);
//                    wsmh.msgTypeProductLeavingWorkstationOrTransport((ProductLeavingWorkstationOrTransportData)message.body());
                    JsonObject jo = (JsonObject)message.body();
                    ProductLeavingWorkstationOrTransportData str = jo.mapTo(ProductLeavingWorkstationOrTransportData.class);
                    wsmh.msgTypeProductLeavingWorkstationOrTransport(str);
              }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA);                  
//                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA, "");
//                    wsmh.msgTypeRecipeExecutionData(realMSBMessage);
//                    wsmh.msgTypeRecipeExecutionData((RecipeExecutionData)message.body());
                    JsonObject jo = (JsonObject)message.body();
                    RecipeExecutionData str = jo.mapTo(RecipeExecutionData.class);
                    wsmh.msgTypeRecipeExecutionData(str);
              }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_WORKSTATION_UPDATE))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_WORKSTATION_UPDATE))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_WORKSTATION_UPDATE, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_WORKSTATION_UPDATE);                  
////                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_WORKSTATION_UPDATE, "");
////                    wsmh.msgTypeWorkstationUpdate(realMSBMessage);
//                    wsmh.msgTypeWorkstationUpdate((SubSystem)message.body());
                    JsonObject jo = (JsonObject)message.body();
                    SubSystem str = jo.mapTo(SubSystem.class);
                    wsmh.msgTypeWorkstationUpdate(str);
              }
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_EXECUTION_TABLE_UPDATE))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_EXECUTION_TABLE_UPDATE, notPreciseMessageReceptionTime);
                    message.reply("Found " + Constants.MSB_MESSAGE_TYPE_EXECUTION_TABLE_UPDATE);                  
////                    String realMSBMessage = myMessageBody.replace(Constants.MSB_MESSAGE_TYPE_WORKSTATION_UPDATE, "");
////                    wsmh.msgTypeWorkstationUpdate(realMSBMessage);
//                    wsmh.msgTypeWorkstationUpdate((SubSystem)message.body());
                    JsonObject jo = (JsonObject)message.body();
                    ExecutionTable et = jo.mapTo(ExecutionTable.class);
                    wsmh.msgTypeExecutionTableUpdate(et);
              }
//              else if (myMessageBody.startsWith(Constants.MSB_MESSAGE_TYPE_SUICIDE))
              else if (messageType.equals(Constants.MSB_MESSAGE_TYPE_SUICIDE))
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_SUICIDE, notPreciseMessageReceptionTime);
                    // message.reply("Found _SUICIDE_");                  
                    message.reply("STOP_SENDING");                  
                    
                    this.getVertx().undeploy(this.deploymentID(), res1 -> 
                    {
                        if (res1.succeeded()) {
                            logger.debug(getInfo() + " - WebSocket undeployment succedeed! "); // Deployment id for agent " + this.getLocalName() + " is [" + websocketsDeploymentId + "]");
                        } else {
                            logger.debug(getInfo() + " - WebSocket undeployment failed!"); // for agent " + this.getLocalName() + " FAILED");
                        }
                    }
                    );
                  
              }
              else 
              {
                    this.notPreciseLastReceivedMessageTimeByType.put(Constants.MSB_MESSAGE_TYPE_UNKNOWN, notPreciseMessageReceptionTime);
//                    message.reply(getInfo() + " - Other MESSAGE type found: [" + myMessageBody + "]");
                    message.reply(getInfo() + " - Other MESSAGE type found: [" + messageType + "]");
              }
          }
                    
          // Now send back reply
          // message.reply("pong!");
        });        
      } else {
        logger.debug(getInfo() + " - Failed: " + res.cause());
      }
    });
      
      
    logger.debug(getInfo() + " - Receiver ready!");
  }

    /**
     * Method called by the infrastructure at the verticle undeployment.
     * For debugging purpose.
     * 
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        // super.stop(); //To change body of generated methods, choose Tools | Templates.
         logger.info(getInfo() + " - websocket stop call [" + topic + "] for agent [" + agentName + "]");
   }
 
    /**
     * For debugging purpose.
     * 
     * @return class name, topic (channel) name, agent name
     */
   private String getInfo()
   {
       return getClass().getName() + " - " + topic + " - " + agentName;
   }

}