package eu.openmos.agentcloud.optimizer.databasegateway;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Updates.set;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.Part;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.ProductLeavingWorkstationOrTransportData;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.model.RawProductData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.UnexpectedProductData;
import eu.openmos.model.*;
import eu.openmos.model.utilities.SerializationConstants;
// import static eu.openmos.agentcloud.utilities.Constants.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import eu.openmos.model.utilities.DatabaseConstants;
import java.util.logging.Level;

/**
* Collection of methods to access database and store data.
* 
* @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
* @author Valerio Gentile <valerio.gentile@we-plus.eu>
*/
public class MongoDBInterface {
    protected com.mongodb.MongoClient syncClient;
    protected MongoDatabase db;
    private static final Logger logger = Logger.getLogger(MongoDBInterface.class.getName());

    public MongoDBInterface(String host) { 
        syncClient = new MongoClient();
        db = syncClient.getDatabase("openmos");
    }
    
    /***************************************************************/
    /* EXECUTION TABLE */
    /***************************************************************/
    
    /**
     * Execution table loaded given the unique identifier.
     * 
     * @param tableId
     * @return
     * @throws ParseException 
     */
    protected ExecutionTable loadExecutionTable(String tableId) throws ParseException {
        if (tableId == null) return null;
        
        if(!fieldExists(tableId, Constants.DB_COLLECTION_EXECUTION_TABLES))           
            return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, tableId), Constants.DB_COLLECTION_EXECUTION_TABLES);
        if (docs == null || docs.size() == 0) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        List<ExecutionTableRow> rows = new LinkedList<>();        
//        ((Document) doc.get("rows")).entrySet().forEach((entry) -> 
//                rows.add(loadExecutionTableRow(entry.getValue().toString())));
if (doc.get(DatabaseConstants.EXECUTION_TABLE_ROW_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.EXECUTION_TABLE_ROW_IDS)).stream().forEach((line) -> {
    rows.add(loadExecutionTableRow(line));
        });
    
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new ExecutionTable(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            rows,
            reg
        );
    }

    protected EquipmentObservationRel2 loadEquipmentObservationRel2(String equipmentObservationRel2Id) throws ParseException {
        if (equipmentObservationRel2Id == null) return null;
        
        if(!fieldExists(equipmentObservationRel2Id, Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2))           
            return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, equipmentObservationRel2Id), Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2);
        if (docs == null || docs.size() == 0) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        List<EquipmentObservationRel2Row> rows = new LinkedList<>();        
if (doc.get(DatabaseConstants.EQUIPMENT_OBSERVATION_REL2_ROW_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.EQUIPMENT_OBSERVATION_REL2_ROW_IDS)).stream().forEach((line) -> {
            try {
                rows.add(loadEquipmentObservationRel2Row(line));
            } catch (ParseException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new EquipmentObservationRel2(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
                doc.getString(DatabaseConstants.EQUIPMENT_ID),
                doc.getString(DatabaseConstants.EQUIPMENT_TYPE),
            rows,
                doc.getString(DatabaseConstants.USER_TEXT),
                doc.getString(DatabaseConstants.USER_NAME),
                doc.getString(DatabaseConstants.SYSTEM_STAGE),                
            reg
        );                
    }

    protected EquipmentAssessment loadEquipmentAssessment(String equipmentAssessmentId) throws ParseException {
        if (equipmentAssessmentId == null) return null;
        
        if(!fieldExists(equipmentAssessmentId, Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS))           
            return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, equipmentAssessmentId), Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS);
        if (docs == null || docs.size() == 0) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        List<EquipmentAssessmentRow> rows = new LinkedList<>();        
if (doc.get(DatabaseConstants.EQUIPMENT_ASSESSMENT_ROW_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.EQUIPMENT_ASSESSMENT_ROW_IDS)).stream().forEach((line) -> {
            try {
                rows.add(loadEquipmentAssessmentRow(line));
            } catch (ParseException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new EquipmentAssessment(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
                doc.getString(DatabaseConstants.EQUIPMENT_ID),
                doc.getString(DatabaseConstants.EQUIPMENT_TYPE),
            rows,
                doc.getString(DatabaseConstants.USER_TEXT),
                doc.getString(DatabaseConstants.USER_NAME),
                doc.getString(DatabaseConstants.SYSTEM_STAGE),
            reg
        );                
    }

    protected PhysicalAdjustment loadPhysicalAdjustment(String physicalAdjustmentId) throws ParseException {
        if (physicalAdjustmentId == null) return null;
        
        if(!fieldExists(physicalAdjustmentId, Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENTS))           
            return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, physicalAdjustmentId), Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENTS);
        if (docs == null || docs.size() == 0) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        PhysicalAdjustmentParameterSetting paps = null;
if (doc.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_SETTING_ID) != null)
            try {
                paps = loadPhysicalAdjustmentParameterSetting((String)doc.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_SETTING_ID));
            } catch (ParseException ex) {
                logger.error(ex.getLocalizedMessage());
            }
    
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new PhysicalAdjustment(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
                doc.getString(DatabaseConstants.EQUIPMENT_ID),
                doc.getString(DatabaseConstants.EQUIPMENT_TYPE),
            paps,
                doc.getString(DatabaseConstants.PHYSICAL_ADJUSTMENT_TYPE),
                doc.getString(DatabaseConstants.USER_TEXT),
                doc.getString(DatabaseConstants.USER_NAME),
                doc.getString(DatabaseConstants.SYSTEM_STAGE),
            reg
        );                
    }

    protected ProcessAssessment loadProcessAssessment(String processAssessmentId) throws ParseException {
        if (processAssessmentId == null) return null;
        
        if(!fieldExists(processAssessmentId, Constants.DB_COLLECTION_PROCESS_ASSESSMENTS))           
            return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, processAssessmentId), Constants.DB_COLLECTION_PROCESS_ASSESSMENTS);
        if (docs == null || docs.size() == 0) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        List<ProcessAssessmentRow> rows = new LinkedList<>();        
if (doc.get(DatabaseConstants.PROCESS_ASSESSMENT_ROW_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PROCESS_ASSESSMENT_ROW_IDS)).stream().forEach((line) -> {
            try {
                rows.add(loadProcessAssessmentRow(line));
            } catch (ParseException ex) {
                logger.error(ex.getLocalizedMessage());
            }
        });
    
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        // VaG - 07/11/2018
        // New field added in lboro integration meeting, 
        // try to manage cases when the field is missing (other demonstrators).
        //
        String skillId = null;
        if (doc.containsKey(DatabaseConstants.SKILL_ID))
            skillId = doc.getString(DatabaseConstants.SKILL_ID);
        
        return new ProcessAssessment(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
                doc.getString(DatabaseConstants.EQUIPMENT_ID),
                doc.getString(DatabaseConstants.EQUIPMENT_TYPE),
                doc.getString(DatabaseConstants.RECIPE_ID),
                skillId,
            rows,
                doc.getString(DatabaseConstants.USER_TEXT),
                doc.getString(DatabaseConstants.USER_NAME),
                doc.getString(DatabaseConstants.SYSTEM_STAGE),                
            reg
        );                
    }

    protected ExecutionTableRow loadExecutionTableRow(String rowId) {
        if (rowId == null) return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, rowId), Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS);
        if (docs == null || docs.size() == 0) return null;
        
        Document doc = docs.get(0);
        if (doc == null) return null;
      
        return ExecutionTableRow.fromBSON(doc);
    }
    
    protected EquipmentObservationRel2Row loadEquipmentObservationRel2Row(String rowId) throws ParseException {
        if (rowId == null) return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, rowId), Constants.DB_COLLECTION_EQUIPMENT_OBSERVATION_REL2_ROWS);
        if (docs == null || docs.size() == 0) return null;
        
        Document doc = docs.get(0);
        if (doc == null) return null;
      
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new EquipmentObservationRel2Row(rowId, 
                doc.getString(DatabaseConstants.EQUIPMENT_OBSERVATION_REL2_ID), 
                doc.getString(DatabaseConstants.TYPE),  
                doc.getString(DatabaseConstants.SUBTYPE),
                doc.getString(DatabaseConstants.UNIT),  
                doc.getDouble(DatabaseConstants.VALUE),
                reg
        );        
    }
    
    protected EquipmentAssessmentRow loadEquipmentAssessmentRow(String rowId) throws ParseException {
        if (rowId == null) return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, rowId), Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENT_ROWS);
        if (docs == null || docs.size() == 0) return null;
        
        Document doc = docs.get(0);
        if (doc == null) return null;
      
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new EquipmentAssessmentRow(rowId, 
                doc.getString(DatabaseConstants.EQUIPMENT_ASSESSMENT_ID), 
                doc.getString(DatabaseConstants.TYPE),  
                doc.getDouble(DatabaseConstants.RATING),
                reg
        );        
    }
    
    protected ProcessAssessmentRow loadProcessAssessmentRow(String rowId) throws ParseException {
        if (rowId == null) return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, rowId), Constants.DB_COLLECTION_PROCESS_ASSESSMENT_ROWS);
        if (docs == null || docs.size() == 0) return null;
        
        Document doc = docs.get(0);
        if (doc == null) return null;
      
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new ProcessAssessmentRow(rowId, 
                doc.getString(DatabaseConstants.PROCESS_ASSESSMENT_ID), 
                doc.getString(DatabaseConstants.TYPE),  
                doc.getDouble(DatabaseConstants.RATING),
                reg
        );        
    }
    
    /**
     * Creates the execution table master record into the database,
     * then loops and insert every row.
     * 
     * @param table ExecutionTable object
     * @return 
     */
    protected boolean newExecutionTableWithRows(ExecutionTable table) {
        if (table == null) return false;
        if (table.getRows() == null) return false;
        
        if(!fieldExists(table.getUniqueId(), Constants.DB_COLLECTION_EXECUTION_TABLES)) 
            db.getCollection(Constants.DB_COLLECTION_EXECUTION_TABLES).insertOne(table.toBSON());
           
        table.getRows().forEach((row) -> newExecutionTableRow(row));        
        return true;
    }    
    private boolean newExecutionTableRow(ExecutionTableRow row) {
        if (row == null) return false;
        
        if(!fieldExists(row.getUniqueId(), Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS)) db.getCollection(Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS).insertOne(row.toBSON());
        return true;
    } 
    
    private boolean removeExecutionTable(String executionTableId) {
        if (executionTableId == null) return false;
        
        // first delete rows, then master table
        if(!fieldExists(executionTableId, Constants.DB_COLLECTION_EXECUTION_TABLES))           
            return false;
        
        Document executionTableDocument;

        // load the execution table
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, executionTableId), Constants.DB_COLLECTION_EXECUTION_TABLES);
        if (docs == null || docs.isEmpty()) return false;
        executionTableDocument = docs.get(0);
        if (executionTableDocument == null) return false;
        
        // remove all rows
//        for(Entry<String, Object> entry : ((Document) executionTableDocument.get("rows")).entrySet())
if (executionTableDocument.get(DatabaseConstants.EXECUTION_TABLE_ROW_IDS) != null)
((ArrayList<String>) executionTableDocument.get(DatabaseConstants.EXECUTION_TABLE_ROW_IDS)).stream().forEach((line) -> {
    removeExecutionTableRow(line);
        });
    /*        for(Entry<String, Object> entry : ((Document) executionTableDocument.get("rows")).entrySet())
    removeExecutionTableRow(entry.getValue().toString());*/

        // remove master record
        db.getCollection(Constants.DB_COLLECTION_EXECUTION_TABLES).deleteOne(eq(DatabaseConstants.UNIQUE_ID, executionTableId));

        return true;        
    }

    protected boolean updateExecutionTable(ExecutionTable executionTable) {
        if (executionTable == null) return false;
        if (executionTable.getUniqueId() == null) return false;
        
        if(!fieldExists(executionTable.getUniqueId(), Constants.DB_COLLECTION_EXECUTION_TABLES))           
            return false;
        
        removeExecutionTable(executionTable.getUniqueId());
        newExecutionTableWithRows(executionTable);
        
        return true;        
    }

    private boolean removeExecutionTableRow(String executionTableRowId) {
        if (executionTableRowId == null) return false;
        
        if(!fieldExists(executionTableRowId, Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS))           
            return false;
        
        db.getCollection(Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, executionTableRowId));  
        return true;
    }

    /**
     * Removes the single row from the database and updates the execution table,
     * removing the row id from rows list.
     * 
     * @param executionTableRowId
     * @return 
     */
    private boolean removeSingleExecutionTableRow(Document executionTableRowDocument) {
        if (executionTableRowDocument == null) return false;
        

        // no need of loading the given row, as it comes as a parameter
//        if(!fieldExists(executionTableRowId, Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS))           
//            return false;
//        Document executionTableRowDocument = readData(eq("id", executionTableRowId), Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS).get(0);

        String executionTableRowId = executionTableRowDocument.get(DatabaseConstants.UNIQUE_ID).toString();
        
        // get the execution table master table record 
        // SYNTAX TO BE CHECKED!
        String fieldName = DatabaseConstants.EXECUTION_TABLE_ROW_IDS + "." + DatabaseConstants.UNIQUE_ID;
        List<Document> docs = readData(eq(fieldName, executionTableRowId), Constants.DB_COLLECTION_EXECUTION_TABLES);
        if (docs == null || docs.isEmpty()) return false;
        Document executionTableDocument = docs.get(0);
        if (executionTableDocument == null) return false;

//        Document rowsOldDocument = (Document) executionTableDocument.get("rows");
//        Document rowsNewDocument = new Document();
List<String> newRows = new LinkedList<>();
//        for(Entry<String, Object> entry : rowsOldDocument.entrySet())
if (executionTableDocument.get(DatabaseConstants.EXECUTION_TABLE_ROW_IDS) != null)
((ArrayList<String>) executionTableDocument.get(DatabaseConstants.EXECUTION_TABLE_ROW_IDS)).stream().forEach((line) -> {
            if (!line.equalsIgnoreCase(executionTableRowId))
                newRows.add(line);
        });
//        {            
//            if (!entry.getValue().toString().equalsIgnoreCase(executionTableRowId))
//                rowsNewDocument.append(DatabaseConstants.UNIQUE_ID, entry.getValue());
//        }

        // update the execution table 
//        executionTableDocument.replace("rows", rowsNewDocument);
        executionTableDocument.replace(DatabaseConstants.EXECUTION_TABLE_ROW_IDS, newRows);

        db.getCollection(Constants.DB_COLLECTION_EXECUTION_TABLES).replaceOne(eq(DatabaseConstants.UNIQUE_ID, executionTableDocument.get(DatabaseConstants.UNIQUE_ID)), executionTableDocument);
        
        // remove the row from the database
        db.getCollection(Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, executionTableRowId));  
        return true;
    }

    /****************************************************************/
    
    protected boolean isOptimizable() {
        return !readData(eq(DatabaseConstants.OPTIMIZED, true), Constants.DB_COLLECTION_RECIPES).isEmpty();
    }
    
    protected List<Recipe> optimize() throws ParseException {
        List<Recipe> recipes = new LinkedList<>();
        List<Document> docs = readData(eq(DatabaseConstants.OPTIMIZED, true), Constants.DB_COLLECTION_RECIPES);
        if (docs == null || docs.isEmpty()) return null;
                
        for(Document bsonRecipe : docs)
            recipes.add(loadOptimizedRecipes(bsonRecipe));       
        return recipes;
    }
        
    protected Recipe loadOptimizedRecipes(Document recipe) throws ParseException {
        if (recipe == null) return null;
        
        return loadRecipe(recipe.getString(DatabaseConstants.UNIQUE_ID));
    }
    
    protected PhysicalPort loadPhysicalPort(String physicalPortId) throws ParseException {
        if (physicalPortId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, physicalPortId), Constants.DB_COLLECTION_PHYSICAL_PORTS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return PhysicalPort.fromBSON(doc);
/*        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new PhysicalPort(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.TYPE),
            doc.getString(DatabaseConstants.DIRECTION),
            reg 
        );
*/        
    }
        
    // equipment id not being used at all!!!!
//     protected boolean processFinishedProduct(String equipmentId, String productId, Date timestamp) {
    protected boolean processFinishedProduct(FinishedProductInfo finishedProductInfo) {
        if (finishedProductInfo == null) return false;
        
        if(fieldExists(finishedProductInfo.getProductInstanceId(), Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS)) 
            db.getCollection(Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS).
                    updateOne(eq(DatabaseConstants.UNIQUE_ID, finishedProductInfo.getProductInstanceId()), 
                            Updates.combine(
                                    set(DatabaseConstants.FINISHED, true), 
                                    set(DatabaseConstants.STATUS, ProductInstanceStatus.PRODUCED.toString()),
                                    set(DatabaseConstants.FINISHED_TIME, finishedProductInfo.getFinishedTime())
                            ));
//        logger.debug("DB INTERFACE - PROCESS FINISHED PRODUCT: " + equipmentId + " - " + productId + " - " + timestamp.toString()); 
        logger.debug("DB INTERFACE - PROCESS FINISHED PRODUCT: " + finishedProductInfo); 
        return true;
    }  
    
    protected boolean processStartedProduct(StartedProductInfo startedProductInfo) {
        if (startedProductInfo == null) return false;
        
        if(fieldExists(startedProductInfo.getProductInstanceId(), Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS)) 
            db.getCollection(Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS).
                    updateOne(eq(DatabaseConstants.UNIQUE_ID, startedProductInfo.getProductInstanceId()), 
                            Updates.combine(
                                    set(DatabaseConstants.STATUS, ProductInstanceStatus.PRODUCING.toString()),
                                    set(DatabaseConstants.STARTED_PRODUCTION_TIME, startedProductInfo.getStartedTime())
                            ));
        logger.debug("DB INTERFACE - PROCESS STARTED PRODUCT: " + startedProductInfo); 
        return true;
    }  
    
    protected boolean removeOrderInstance(String orderId, Date timestamp) {
        if (orderId == null) return false;
        if (timestamp == null) return false;
        
        
        if(fieldExists(orderId, Constants.DB_COLLECTION_ORDER_INSTANCES)) {
            db.getCollection(Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS).deleteMany(eq(DatabaseConstants.ORDER_ID, orderId));
            db.getCollection(Constants.DB_COLLECTION_ORDER_INSTANCES).updateOne(eq(DatabaseConstants.UNIQUE_ID, 
                    orderId), 
                    Updates.combine(set(DatabaseConstants.REMOVED, true), 
                            set(DatabaseConstants.REMOVED_TIME, timestamp)));
        }
        logger.debug("DB INTERFACE - PROCESS REMOVE ORDER INSTANCE: " + orderId + " - " + timestamp.toString()); 
        return true;
    }  
    
    protected boolean newOrderInstance(OrderInstance order) {
        if (order == null) return false;
        
        if(!fieldExists(order.getUniqueId(), Constants.DB_COLLECTION_ORDER_INSTANCES)) {
            logger.debug("MONGODBINTERFACE: ORDERINSTANCE.TOBSON = [" + order.toBSON() + "]");
            
            db.getCollection(Constants.DB_COLLECTION_ORDER_INSTANCES).insertOne(order.toBSON());
            order.getProductInstances().forEach((product) -> {
                newProductInstance(product);
                if (product.getPartInstances() != null)
                product.getPartInstances().forEach((partInstance) -> newPartInstance(partInstance));
//                product.getSkillRequirements().forEach((requirement) -> newSkillRequirement(requirement));
            });
        } 
        logger.debug("DB INTERFACE - PROCESS NEW ORDER INSTANCE: " + order.toString()); 
        return true;
    }
    
    protected boolean newOrder(Order order) {
        if (order == null) return false;
        
        if(!fieldExists(order.getUniqueId(), Constants.DB_COLLECTION_ORDERS)) {
            logger.debug("MONGODBINTERFACE: ORDER.TOBSON = [" + order.toBSON() + "]");
            
            db.getCollection(Constants.DB_COLLECTION_ORDERS).insertOne(order.toBSON());
            order.getOrderLines().forEach((orderLine) -> {
                newOrderLine(orderLine);
            });
        } 
        logger.debug("DB INTERFACE - PROCESS NEW ORDER: " + order.toString()); 
        return true;
    }
    
    protected boolean newRawEquipmentData(RawEquipmentData data) {
        if (data == null) return false;
        
        if(fieldExists(data.getEquipmentId(), Constants.DB_COLLECTION_AGENTS)) db.getCollection(Constants.DB_COLLECTION_RAW_EQUIPMENT_DATA).insertOne(data.toBSON());
        logger.debug("DB INTERFACE - PROCESS NEW RAW EQUIPMENT DATA: " + data.toString()); 
        return true;
    }
    
    protected boolean newRawProductData(RawProductData data) {
        if (data == null) return false;
        
        if(fieldExists(data.getProductId(), Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS)) db.getCollection(Constants.DB_COLLECTION_RAW_PRODUCT_DATA).insertOne(data.toBSON());
        logger.debug("DB INTERFACE - PROCESS NEW RAW PRODUCT DATA: " + data.toString()); 
        return true;
    }
    
    protected boolean newAgentWithMissingHeartBeat(String agentUniqueName) {
        if (agentUniqueName == null) return false;
        
        if(fieldExists(agentUniqueName, Constants.DB_COLLECTION_AGENTS))
            db.getCollection(Constants.DB_COLLECTION_AGENTS_WITH_MISSING_HB).insertOne(new Document(DatabaseConstants.UNIQUE_ID, agentUniqueName));
        logger.debug("DB INTERFACE - PROCESS NEW AGENT WITH MISSING HB: " + agentUniqueName); 
        return true;
    }
    
    protected boolean removeAgentInPlatform(String agentUniqueName) {
        if (agentUniqueName == null) return false;
        
        if(!fieldExists(agentUniqueName, Constants.DB_COLLECTION_AGENTS))
            return false;

        logger.trace("removeAgentInPlatform - subsystem found");
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, agentUniqueName), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return false;
        Document doc = docs.get(0);
        logger.trace("removeAgentInPlatform - susbsytem loaded");
        if (doc == null) return false;
            

        logger.trace("removeAgentInPlatform - execution table");
            String executionTableId = doc.getString(DatabaseConstants.EXECUTION_TABLE_ID);            
            if (executionTableId != null)
                removeExecutionTable(executionTableId);
            
            logger.trace("removeAgentInPlatform - recipes");
if (doc.get(DatabaseConstants.RECIPE_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
                removeRecipe(line);
        });
/*            for(Entry<String, Object> entry : ((Document) doc.get("recipeIds")).entrySet())
                removeRecipe(entry.getValue().toString());
*/

        logger.trace("removeAgentInPlatform - skills");
if (doc.get(DatabaseConstants.SKILL_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.SKILL_IDS)).stream().forEach((line) -> {
                removeSkill(line);
        });
//            for(Entry<String, Object> entry : ((Document) doc.get("skillIds")).entrySet())
//                removeSkill(entry.getValue().toString());
            

        logger.trace("removeAgentInPlatform - internal modules");
if (doc.get(DatabaseConstants.INTERNAL_MODULE_IDS) != null)
    ((ArrayList<String>) doc.get(DatabaseConstants.INTERNAL_MODULE_IDS)).stream().forEach((line) -> {
                removeModule(line);
        });
//            for(Entry<String, Object> entry : ((Document) doc.get("internalModuleIds")).entrySet())
//                removeModule(entry.getValue().toString());

        logger.trace("removeAgentInPlatform - physical ports");
if (doc.get(DatabaseConstants.PHYSICAL_PORT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PHYSICAL_PORT_IDS)).stream().forEach((line) -> {
                removePhysicalPort(line);
        });
//            for(Entry<String, Object> entry : ((Document) doc.get("physicalPortIds")).entrySet())
//                removePhysicalPort(entry.getValue().toString());

        logger.trace("removeAgentInPlatform - the agent!");
            db.getCollection(Constants.DB_COLLECTION_AGENTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, agentUniqueName));
        
        logger.debug("DB INTERFACE - PROCESS REMOVE AGENT IN PLATFORM: " + agentUniqueName); 
        return true;
    }
    
    protected boolean newAgentInPlatform(SubSystem cpad) {
        if (cpad == null) return false;
        
        if(fieldExists(cpad.getName(), Constants.DB_COLLECTION_AGENTS)) {
            logger.debug("newAgentInPlatform - agent already existing");            
            removeAgentInPlatform(cpad.getUniqueId());
            logger.debug("newAgentInPlatform - agent removed... going to insert new record");            
        }

        logger.trace("newAgentInPlatform - 2");
        db.getCollection(Constants.DB_COLLECTION_AGENTS).insertOne(cpad.toBSON());

        logger.trace("newAgentInPlatform - 3");
        if (cpad.getExecutionTable() != null)
            newExecutionTableWithRows(cpad.getExecutionTable());

        logger.trace("newAgentInPlatform - 4");
        if (cpad.getSkills() != null)
            cpad.getSkills().forEach((skill) -> newSkill(skill));

        logger.trace("newAgentInPlatform - 5");
        if (cpad.getRecipes() != null)
            cpad.getRecipes().forEach((recipe) -> newRecipe(recipe));

        logger.trace("newAgentInPlatform - 6");
        if (cpad.getInternalModules() != null)
            newModules(cpad.getInternalModules());

        logger.trace("newAgentInPlatform - 7");
        if (cpad.getPhysicalPorts() != null)
            cpad.getPhysicalPorts().forEach((port) -> newPhysicalPort(port));

        logger.trace("newAgentInPlatform - 8");
        if (cpad.getPhysicalAdjustmentParameters() != null)
            cpad.getPhysicalAdjustmentParameters().forEach((physicalAdjustmentParameter) -> newPhysicalAdjustmentParameter(physicalAdjustmentParameter));
            
        logger.debug("DB INTERFACE - PROCESS NEW AGENT IN PLATFORM: " + cpad.toString()); 
        return true;
    }   
    
    protected boolean newComponent(Part component) {
        if (component == null) return false;
        
        if(!fieldExists(component.getUniqueId(), Constants.DB_COLLECTION_COMPONENTS)) 
            db.getCollection(Constants.DB_COLLECTION_COMPONENTS).insertOne(component.toBSON());
        return true;
    }
    
    protected boolean removeComponent(String componentId) {
        if (componentId == null) return false;
        
        if(fieldExists(componentId, Constants.DB_COLLECTION_COMPONENTS)) 
            db.getCollection(Constants.DB_COLLECTION_COMPONENTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, componentId));
        return true;
    }
    
    protected boolean removeParameter(String parameterId) {
        if (parameterId == null) return false;
        
        if(fieldExists(parameterId, Constants.DB_COLLECTION_PARAMETERS)) 
            db.getCollection(Constants.DB_COLLECTION_PARAMETERS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, parameterId));
        return true;
    }
    
    protected boolean updateProduct(ProductInstance productInstance) {
        if (productInstance == null) return false;
        if (productInstance.getUniqueId() == null) return false;
        if (productInstance.getStatus() == null) return false;
        
        db.getCollection(Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, productInstance.getUniqueId()), productInstance.toBSON());

        return true;
    }

    protected boolean updateUnfinishedProducts(Document old, ProductInstance newProduct) {
        if (old == null) return false;
        if (newProduct == null) return false;        
        
//        ((Document) old.get("components")).entrySet().forEach((entry) -> removeComponent(entry.getValue().toString()));
if (old.get(DatabaseConstants.PART_INSTANCE_IDS) != null)
((ArrayList<String>) old.get(DatabaseConstants.PART_INSTANCE_IDS)).stream().forEach((line) -> {
                removeComponent(line);
        });

//        ((Document) old.get("skillRequirements")).entrySet().forEach((entry) -> removeSkillRequirement(entry.getValue().toString()));
/*
if (old.get(DatabaseConstants.SKILL_REQUIREMENT_IDS) != null)
((ArrayList<String>) old.get(DatabaseConstants.SKILL_REQUIREMENT_IDS)).stream().forEach((line) -> {
                removeSkillRequirement(line);
        });
*/

        db.getCollection(Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, newProduct.getUniqueId()), newProduct.toBSON());

        if (newProduct.getPartInstances() != null)
            newProduct.getPartInstances().forEach((partInstance) -> newPartInstance(partInstance));
//        newProduct.getSkillRequirements().forEach((requirement) -> newSkillRequirement(requirement));  

        return true;
    }
    
    protected boolean updateOrder(OrderInstance order) {
        if (order == null) return false;
        
        // TODO: we have to take care of those product description records that 
        // were into the original list but are not present into the updated list.
        // As we are doing now, we are only considering the updated list.
        // 
        if(fieldExists(order.getUniqueId(), Constants.DB_COLLECTION_ORDER_INSTANCES)) {            
            db.getCollection(Constants.DB_COLLECTION_ORDER_INSTANCES).replaceOne(eq(DatabaseConstants.UNIQUE_ID, order.getUniqueId()), order.toBSON()); // updates priorities and product descriptions ids
            
            if (order.getProductInstances() != null)
            order.getProductInstances().forEach((product) -> {
                if(fieldExists(product.getUniqueId(), Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS)) { // if the product description exists
                    List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, product.getUniqueId()), Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS);
                    if (docs != null && !docs.isEmpty())
                    {
                        Document current = docs.get(0);
                        if (current != null)
                        if(!current.getBoolean(DatabaseConstants.FINISHED)) 
                                        updateUnfinishedProducts(current, product);  // if is not finished, update it
                    }
                } else newProductInstance(product); // add unexisting product description
                        });    
        } 
        // VaG - 16/11/2017 - we have a bug somewhere! to be fixed
        else
            newOrderInstance(order);
        
        logger.debug("DB INTERFACE - PROCESS UPDATE ORDER: " + order.toString()); 
        return true;
    }
    
    protected boolean trackUnexpectedProductData(UnexpectedProductData unexpectedProductData) {
        if (unexpectedProductData == null) return false;
        
        db.getCollection(Constants.DB_COLLECTION_UNEXPECTED_PRODUCT_DATA).insertOne(unexpectedProductData.toBSON());
        logger.debug("DB INTERFACE - UNEXPECTED PRODUCT DATA: " + unexpectedProductData.toString()); 
        return true;
    }

    protected boolean trackProductLeavingWorkstationOrTransportData(ProductLeavingWorkstationOrTransportData productLeavingWorkstationOrTransportData) {
        if (productLeavingWorkstationOrTransportData == null) return false;
        
        db.getCollection(Constants.DB_COLLECTION_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA).insertOne(productLeavingWorkstationOrTransportData.toBSON());
        logger.debug("DB INTERFACE - PRODUCT LEAVING WORKSTATION OR TRANSPORT DATA: " + productLeavingWorkstationOrTransportData.toString()); 
        return true;
    }

    protected boolean newRecipeExecutionData(RecipeExecutionData recipeExecutionData) {
        if (recipeExecutionData == null) return false;
        
        db.getCollection(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA).insertOne(recipeExecutionData.toBSON());

            if (recipeExecutionData.getKpiSettings() != null)
                recipeExecutionData.getKpiSettings().forEach((setting) -> 
                {
                    setting.setRegistered(recipeExecutionData.getRegistered());
                    newREDKPISetting(setting);
                        
                    updateKPISetting(setting);
                }
                );
            
            if (recipeExecutionData.getParameterSettings() != null)
                recipeExecutionData.getParameterSettings().forEach((setting) -> 
                {
                    setting.setRegistered(recipeExecutionData.getRegistered());
                    newREDParameterSetting(setting);
                    
                    updateParameterSetting(setting);
                }
                );
            
        logger.debug("DB INTERFACE - RECIPE EXECUTION DATA: " + recipeExecutionData.toString()); 
        return true;
    }

//    protected boolean updateWorkstation(CyberPhysicalAgentDescription newCpad) 
    protected boolean updateWorkstation(SubSystem newCpad) 
    {
        if (newCpad == null) return false;
        
        // if can't load the old record, go to insert a new one and return
//        if(!fieldExists(newCpad.getEquipmentId(), Constants.DB_COLLECTION_AGENTS))           
        if(!fieldExists(newCpad.getName(), Constants.DB_COLLECTION_AGENTS))           
            return newAgentInPlatform(newCpad);
        
        Document oldCpadDocument;

        // load the old document
//        oldCpadDocument = readData(eq("id", newCpad.getEquipmentId()), Constants.DB_COLLECTION_AGENTS).get(0);
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, newCpad.getName()), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return false;
        oldCpadDocument = docs.get(0);
        if (oldCpadDocument == null) return false;
        
        // remove all related objects (into the cpad document we only store ids of external objects)
        if (oldCpadDocument.get(DatabaseConstants.EXECUTION_TABLE_ID) != null)
        removeExecutionTable(oldCpadDocument.get(DatabaseConstants.EXECUTION_TABLE_ID).toString());
        
        if (oldCpadDocument.get(DatabaseConstants.SKILL_IDS) != null)
((ArrayList<String>) oldCpadDocument.get(DatabaseConstants.SKILL_IDS)).stream().forEach((line) -> {
                removeSkill(line);
        });
//        for(Entry<String, Object> entry : ((Document) oldCpadDocument.get("skills")).entrySet())
//            removeSkill(entry.getValue().toString());
        if (oldCpadDocument.get(DatabaseConstants.RECIPE_IDS) != null)
((ArrayList<String>) oldCpadDocument.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
                removeRecipe(line);
        });
//        for(Entry<String, Object> entry : ((Document) oldCpadDocument.get("recipes")).entrySet())
//            removeRecipe(entry.getValue().toString());
        if (oldCpadDocument.get(DatabaseConstants.INTERNAL_MODULE_IDS) != null)
((ArrayList<String>) oldCpadDocument.get(DatabaseConstants.INTERNAL_MODULE_IDS)).stream().forEach((line) -> {
                removeModule(line);
        });
//        for(Entry<String, Object> entry : ((Document) oldCpadDocument.get("equipments")).entrySet())
//            removeEquipment(entry.getValue().toString());

        if (oldCpadDocument.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS) != null)
((ArrayList<String>) oldCpadDocument.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS)).stream().forEach((line) -> {
                removePhysicalAdjustmentParameter(line);
        });

        // update the cpad document
//        db.getCollection(Constants.DB_COLLECTION_AGENTS).replaceOne(eq("id", newCpad.getEquipmentId()), newCpad.toBSON()); 
        db.getCollection(Constants.DB_COLLECTION_AGENTS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, newCpad.getName()), newCpad.toBSON()); 

        // insert all details previously removed
        if (newCpad.getExecutionTable() != null)
        newExecutionTableWithRows(newCpad.getExecutionTable());
        // execution table rows should be managed by the processNewExecutionTable() method
        // newCpad.getExecutionTable().getRows().forEach((row) -> processNewExecutionTableRow(row));
        
        if (newCpad.getSkills() != null)
        newCpad.getSkills().forEach((skill) -> newSkill(skill));
        
        if (newCpad.getRecipes() != null)
        newCpad.getRecipes().forEach((recipe) -> newRecipe(recipe));
        
//        newEquipments(newCpad.getEquipments());
if (newCpad.getInternalModules() != null)
        newModules(newCpad.getInternalModules());
        
        logger.debug("DB INTERFACE - WORKSTATION UPDATE: " + newCpad.toString()); 
        return true;
    }
    
    private boolean fieldExists(String id, String collection) {
        return !readData(Filters.eq(DatabaseConstants.UNIQUE_ID, id), collection).isEmpty();
    }
    
    public List<Document> readData(Bson filter, String collection) {
        List<Document> entries = new LinkedList<>();
        db.getCollection(collection).find(filter).into(entries);
        return entries;
    }

    public List<Document> readList(String collection) {
        List<Document> entries = new LinkedList<>();
        db.getCollection(collection).find().into(entries);
        return entries;
    }
/*
    private boolean removeEquipment(String equipmentId) {
        // first delete child records, then master record
        if(!fieldExists(equipmentId, Constants.DB_COLLECTION_EQUIPMENTS))           
            return false;
        
        Document equipmentDocument;

        // load the old document
        equipmentDocument = readData(eq(DatabaseConstants.UNIQUE_ID, equipmentId), Constants.DB_COLLECTION_EQUIPMENTS).get(0);
        
        // remove all child records
        // TBV execution table needs to be removed? YES
        String executionTableId = equipmentDocument.get("executionTable").toString();
        removeExecutionTable(executionTableId);
        // skills needs to be removed? dont know yet TBV would say "no"        

        // remove master record
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, equipmentId));

        return true;        
    }
*/
    /**
     * remove skill from agent's skills list
     * 
     * @param equipmentId
     * @param skillId
     * @return 
     */
    private boolean removeSkillFromAgent(String agentId, String skillId) {
        if (agentId == null) return false;
        if (skillId == null) return false;
                
        if(!fieldExists(agentId, Constants.DB_COLLECTION_AGENTS))           
            return false;
        
        Document agentDocument;

//        Document skills = new Document();        
        List<String> skills = new LinkedList<>();

        // load the equipment document
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, agentId), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return false;
        agentDocument = docs.get(0);
        if (agentDocument == null) return false;
        
        if (agentDocument.get(DatabaseConstants.SKILL_IDS) != null)
((ArrayList<String>) agentDocument.get(DatabaseConstants.SKILL_IDS)).stream().forEach((line) -> {
            String currentSkillId = line;
            if (!currentSkillId.equalsIgnoreCase(skillId))
                // skills.append(DatabaseConstants.UNIQUE_ID, currentSkillId);
                skills.add(line);
        });
        
//        for(Entry<String, Object> entry : ((Document) agentDocument.get("skills")).entrySet())
//        {
//            String currentSkillId = entry.getValue().toString();
//            if (!currentSkillId.equalsIgnoreCase(skillId))
//                skills.append(DatabaseConstants.UNIQUE_ID, currentSkillId);
//        }

        agentDocument.replace(DatabaseConstants.SKILL_IDS, skills);
        
        db.getCollection(Constants.DB_COLLECTION_AGENTS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, agentId), agentDocument);

        return true;        
    }

    /**
     * remove recipe from agent's recipes list
     * @param agentId
     * @param recipeId
     * @return 
     */
    private boolean removeRecipeFromAgent(String agentId, String recipeId) {
        if (agentId == null) return false;
        if (recipeId == null) return false;
                
        if(!fieldExists(agentId, Constants.DB_COLLECTION_AGENTS))           
            return false;
        
        Document agentDocument;

        // Document recipes = new Document();        
        List<String> recipes = new LinkedList<>();

        // load the equipment document
        // agentDocument = readData(eq(DatabaseConstants.UNIQUE_ID, agentId), Constants.DB_COLLECTION_AGENTS).get(0);
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, agentId), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return false;
        agentDocument = docs.get(0);
        if (agentDocument == null) return false;
        
        if (agentDocument.get(DatabaseConstants.RECIPE_IDS) != null)
((ArrayList<String>) agentDocument.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
            String currentRecipeId = line;
            if (!currentRecipeId.equalsIgnoreCase(recipeId))
                recipes.add(currentRecipeId);
        });
//        for(Entry<String, Object> entry : ((Document) agentDocument.get("recipes")).entrySet())
//        {
//            String currentRecipeId = entry.getValue().toString();
//            if (!currentRecipeId.equalsIgnoreCase(recipeId))
//                recipes.append("id", currentRecipeId);
//        }

        agentDocument.replace(DatabaseConstants.RECIPE_IDS, recipes);
        
        db.getCollection(Constants.DB_COLLECTION_AGENTS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, agentId), agentDocument);

        return true;        
    }

    private boolean removeRecipeFromSkill(String skillId, String recipeId) {
        if (skillId == null) return false;
        if (recipeId == null) return false;        
        
        if(!fieldExists(skillId, Constants.DB_COLLECTION_SKILLS))           
            return false;
        
        Document skillDocument;

        // Document recipes = new Document();        
        List<String> recipes = new LinkedList<>();

        // load the equipment document
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, skillId), Constants.DB_COLLECTION_SKILLS);
        if (docs == null || docs.isEmpty()) return false;
        skillDocument = docs.get(0);
        if (skillDocument == null) return false;
        
        if (skillDocument.get(DatabaseConstants.RECIPE_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
            String currentRecipeId = line;
            if (!currentRecipeId.equalsIgnoreCase(recipeId))
                recipes.add(currentRecipeId);
        });
//        for(Entry<String, Object> entry : ((Document) skillDocument.get("recipes")).entrySet())
//        {
//            String currentRecipeId = entry.getValue().toString();
//            if (!currentRecipeId.equalsIgnoreCase(recipeId))
//                recipes.append("id", currentRecipeId);
//        }

        skillDocument.replace(DatabaseConstants.RECIPE_IDS, recipes);
        
        // db.getCollection(Constants.DB_COLLECTION_SKILLS).updateOne(eq(DatabaseConstants.UNIQUE_ID, skillId), skillDocument);
        db.getCollection(Constants.DB_COLLECTION_SKILLS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, skillId), skillDocument);

        return true;        
    }

    private boolean removeParameterSetting(String parameterSettingId) {
        if (parameterSettingId == null) return false;
        
        // first delete child records, then master record
        if(!fieldExists(parameterSettingId, Constants.DB_COLLECTION_PARAMETER_SETTINGS))           
            return false;
        
        Document parameterSettingDocument;

        // load the old document
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, parameterSettingId), Constants.DB_COLLECTION_PARAMETER_SETTINGS);
        if (docs == null || docs.isEmpty()) return false;
        parameterSettingDocument = docs.get(0);
        if (parameterSettingDocument == null) return false;
        
        // remove all child records
        // TBV parameter needs to be removed? NO

        // remove master record
        db.getCollection(Constants.DB_COLLECTION_PARAMETER_SETTINGS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, parameterSettingId));

        return true;        
    }

    /*******************************************/
    /* SKILL */
    /*******************************************/
    protected Skill loadSkill(String skillId) throws ParseException {
        if (skillId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION); 
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, skillId), Constants.DB_COLLECTION_SKILLS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        List<KPI> kpis = new LinkedList<>();
        List<InformationPort> informationPorts = new LinkedList<>();
        List<Parameter> parameters = new LinkedList<>();
        List<ParameterPort> parameterPorts = new LinkedList<>();
        List<SkillRequirement> skillRequirements = new LinkedList<>();
        List<String> recipeIds = new LinkedList<>();
        List<ControlPort> controlPorts = new LinkedList<>();
        
//        ((Document) doc.get("kpis")).entrySet().forEach((entry) -> kpis.add(loadKPI(entry.getValue().toString())));
if (doc.get(DatabaseConstants.KPI_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.KPI_IDS)).stream().forEach((line) -> {
            kpis.add(loadKPI(line));
        });
        
//        for(Entry<String, Object> entry : ((Document) doc.get("informationPortIds")).entrySet())
//            informationPorts.add(loadInformationPort(entry.getValue().toString()));
if (doc.get(DatabaseConstants.INFORMATION_PORT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.INFORMATION_PORT_IDS)).stream().forEach((line) -> {
            try {
                informationPorts.add(loadInformationPort(line));
            } catch (ParseException ex) {
                logger.error("loadSkill - informationPortIds: " + ex);
            }
        });

if (doc.get(DatabaseConstants.PARAMETER_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PARAMETER_IDS)).stream().forEach((line) -> {
            try {
                parameters.add(loadParameter(line));
            } catch (ParseException ex) {
                logger.error("loadSkill - parameters: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("parameters")).entrySet())
//            parameters.add(loadParameter(entry.getValue().toString()));

if (doc.get(DatabaseConstants.PARAMETER_PORT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PARAMETER_PORT_IDS)).stream().forEach((line) -> {
            try {
                parameterPorts.add(loadParameterPort(line));
            } catch (ParseException ex) {
                logger.error("loadSkill - parameterPortIds: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("parameterPortIds")).entrySet())
//            parameterPorts.add(loadParameterPort(entry.getValue().toString()));

if (doc.get(DatabaseConstants.SKILL_REQUIREMENT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.SKILL_REQUIREMENT_IDS)).stream().forEach((line) -> {
            try {
                List<Document> docums = readData(eq(DatabaseConstants.UNIQUE_ID, line), Constants.DB_COLLECTION_SKILL_REQUIREMENTS);
                if (docs != null && !docs.isEmpty())
                {
                    Document docum = docums.get(0);
                    if (docum != null)
                        skillRequirements.add(loadSkillRequirement(docum.toString()));
                }
            } catch (ParseException ex) {
                logger.error("loadSkill - skillRequirements: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("skillRequirements")).entrySet())
//            skillRequirements.add(loadSkillRequirement(readData(eq(DatabaseConstants.UNIQUE_ID, entry.getValue()), Constants.DB_COLLECTION_SKILL_REQUIREMENTS).get(0).toString()));
        
if (doc.get(DatabaseConstants.RECIPE_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
            recipeIds.add(line);
        });
//        ((Document) doc.get("recipesIds")).entrySet().forEach((entry) -> recipeIds.add(entry.getValue().toString()));

if (doc.get(DatabaseConstants.CONTROL_PORT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.CONTROL_PORT_IDS)).stream().forEach((line) -> {
            try {
                controlPorts.add(loadControlPort(line));
            } catch (ParseException ex) {
                logger.error("loadSkill - controlPortIds: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("controlPortIds")).entrySet())
//            controlPorts.add(loadControlPort(entry.getValue().toString()));
        
        SkillType st = null;
        if (doc.getString(DatabaseConstants.SKILL_TYPE_ID) != null)
            loadSkillType(doc.getString(DatabaseConstants.SKILL_TYPE_ID));
               
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new Skill(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            kpis,
                informationPorts,
            doc.getString(DatabaseConstants.NAME),
                doc.getString(DatabaseConstants.LABEL),
            parameters,
                parameterPorts,
            doc.getString(DatabaseConstants.TYPE),
                st,
            doc.getInteger(DatabaseConstants.CLASSIFICATION_TYPE),
            skillRequirements,
            recipeIds,
                controlPorts,
            doc.getString(DatabaseConstants.DESCRIPTION),
            reg 
//            doc.getString("registered")
        );
    }
    
    protected boolean newSkill(Skill skill) {
        if (skill == null) return false;
        
        if(!fieldExists(skill.getUniqueId(), Constants.DB_COLLECTION_SKILLS)) {
            db.getCollection(Constants.DB_COLLECTION_SKILLS).insertOne(skill.toBSON());
            
            if (skill.getKpis() != null)
                skill.getKpis().forEach((kpi) -> newKPI(kpi));
            
            if (skill.getParameters() != null)
                skill.getParameters().forEach((p) -> newParameter(p));
            
            if (skill.getSkillRequirements()!= null)
                skill.getSkillRequirements().forEach((sr) -> newSkillRequirement(sr));
            
            if (skill.getControlPorts() != null)
                skill.getControlPorts().forEach((controlPort) -> newControlPort(controlPort));
            
            if (skill.getParameterPorts() != null)
                skill.getParameterPorts().forEach((parameterPort) -> newParameterPort(parameterPort));

            if (skill.getInformationPorts() != null)
                skill.getInformationPorts().forEach((informationPort) -> newInformationPort(informationPort));
                                
            if (skill.getSkillType() != null)
                newSkillType(skill.getSkillType());
        } 
        logger.debug("DB INTERFACE - PROCESS NEW SKILL: " + skill.toString()); 
        return true;
    }
    
    protected boolean newSkillRequirement(SkillRequirement requirement) {
        if (requirement == null) return false;
        
//        if(!fieldExists(requirement.getName(), Constants.DB_COLLECTION_SKILL_REQUIREMENTS)) 
        if(!fieldExists(requirement.getUniqueId(), Constants.DB_COLLECTION_SKILL_REQUIREMENTS)) 
            db.getCollection(Constants.DB_COLLECTION_SKILL_REQUIREMENTS).insertOne(requirement.toBSON());
        
        // precedents already exist? don'care, just save the id into the skillreq json
/*        
        for (SkillRequirement precedent : requirement.getPrecedents())
            newSkillRequirement(precedent);
*/
        // required part already exist?
        if (requirement.getRequiresPart() != null)
            newPart(requirement.getRequiresPart());
        
        // skilltype?       
        if (requirement.getSkillType() != null)
            newSkillType(requirement.getSkillType());
        
        return true;
    }
    
    protected boolean removeSkillRequirement(String requirementId) {
        if (requirementId == null) return false;
        
        if(fieldExists(requirementId, Constants.DB_COLLECTION_SKILL_REQUIREMENTS)) 
            db.getCollection(Constants.DB_COLLECTION_SKILL_REQUIREMENTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, requirementId));
        
        // skill type not to be deleted
        // required part not to be deleted
        // skill req precedents not to be deleted
        
        return true;
    }

    private boolean removeSkill(String skillId) {
        if (skillId == null) return false;
        
        // first delete child records, then master record
        if(!fieldExists(skillId, Constants.DB_COLLECTION_SKILLS))           
            return false;
        
        Document skillDocument;

        // load the old document
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, skillId), Constants.DB_COLLECTION_SKILLS);
        if (docs == null || docs.isEmpty()) return false;        
        skillDocument = docs.get(0);
        if (skillDocument == null) return false;
        
        // remove all child records
//        for(Entry<String, Object> entry : ((Document) skillDocument.get("kpis")).entrySet())
//            removeKPI(entry.getValue().toString());
if (skillDocument.get(DatabaseConstants.KPI_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.KPI_IDS)).stream().forEach((line) -> {
            removeKPI(line);
        });
        
//        for(Entry<String, Object> entry : ((Document) skillDocument.get("parameters")).entrySet())
//            removeParameter(entry.getValue().toString());
if (skillDocument.get(DatabaseConstants.PARAMETER_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.PARAMETER_IDS)).stream().forEach((line) -> {
            removeParameter(line);
        });

//        for(Entry<String, Object> entry : ((Document) skillDocument.get("skillRequirements")).entrySet())
//            removeSkillRequirement(entry.getValue().toString());
if (skillDocument.get(DatabaseConstants.SKILL_REQUIREMENT_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.SKILL_REQUIREMENT_IDS)).stream().forEach((line) -> {
            removeSkillRequirement(line);
        });

//        for(Entry<String, Object> entry : ((Document) skillDocument.get("recipes")).entrySet())
//            removeRecipe(entry.getValue().toString());
if (skillDocument.get(DatabaseConstants.RECIPE_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
            removeRecipe(line);
        });
        
//        for(Entry<String, Object> entry : ((Document) skillDocument.get("controlPortIds")).entrySet())
//            removeControlPort(entry.getValue().toString());
if (skillDocument.get(DatabaseConstants.CONTROL_PORT_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.CONTROL_PORT_IDS)).stream().forEach((line) -> {
            removeControlPort(line);
        });

//        for(Entry<String, Object> entry : ((Document) skillDocument.get("informationPortIds")).entrySet())
//            removeInformationPort(entry.getValue().toString());
if (skillDocument.get(DatabaseConstants.INFORMATION_PORT_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.INFORMATION_PORT_IDS)).stream().forEach((line) -> {
            removeInformationPort(line);
        });

//        for(Entry<String, Object> entry : ((Document) skillDocument.get("parameterPortIds")).entrySet())
//            removeParameterPort(entry.getValue().toString());
if (skillDocument.get(DatabaseConstants.PARAMETER_PORT_IDS) != null)
((ArrayList<String>) skillDocument.get(DatabaseConstants.PARAMETER_PORT_IDS)).stream().forEach((line) -> {
            removeParameterPort(line);
        });
        
        
        // TBV equipment needs to be removed? 
        // NOT REALLY, just update the equipment skills removing the skill from its skills list
        String equipmentId = skillDocument.getString(DatabaseConstants.EQUIPMENT_ID);       
        if (equipmentId != null)
        removeSkillFromAgent(equipmentId, skillId);
        
        // remove master record
        db.getCollection(Constants.DB_COLLECTION_SKILLS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, skillId));

        return true;        
    }

    private SkillType loadSkillType(String skillTypeId) throws ParseException {
        if (skillTypeId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);        
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, skillTypeId), Constants.DB_COLLECTION_SKILL_TYPES);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return SkillType.fromBSON(doc);
/*        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new SkillType(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getBoolean(DatabaseConstants.DECISION),
            reg 
        );
*/        
    }

    protected boolean newSkillType(SkillType skillType) {
        if (skillType == null) return false;
        
        if(!fieldExists(skillType.getUniqueId(), Constants.DB_COLLECTION_SKILL_TYPES)) {
            db.getCollection(Constants.DB_COLLECTION_SKILL_TYPES).insertOne(skillType.toBSON());            
        } 
        logger.debug("DB INTERFACE - PROCESS NEW SKILL TYPE: " + skillType.toString()); 
        return true;
    }
    
        /******************************************************/
        /* PORTS */
        /* 
           will not store port cause i guess they are strictly related to their equipment
           and are not reusable by any other 
        */
        /******************************************************/
    protected InformationPort loadInformationPort(String informationPortId) throws ParseException {
        if (informationPortId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);   
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, informationPortId), Constants.DB_COLLECTION_INFORMATION_PORTS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;

        List<KPI> kpis = new LinkedList<>();

if (doc.get(DatabaseConstants.KPI_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.KPI_IDS)).stream().forEach((line) -> {
            kpis.add(loadKPI(line));
        });
//        ((Document) doc.get("kpiIds")).entrySet().forEach((entry) -> kpis.add(loadKPI(entry.getValue().toString())));
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new InformationPort(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.TYPE),
            doc.getString(DatabaseConstants.DIRECTION),
                kpis,
            reg 
        );
    }

    private boolean newPhysicalPort(PhysicalPort physicalPort) {
        if (physicalPort == null) return false;
        
        if(!fieldExists(physicalPort.getUniqueId(), Constants.DB_COLLECTION_PHYSICAL_PORTS))
            db.getCollection(Constants.DB_COLLECTION_PHYSICAL_PORTS).insertOne(physicalPort.toBSON());

            return true;
    }
    private boolean newControlPort(ControlPort controlPort) {
        if (controlPort == null) return false;
        
        if(!fieldExists(controlPort.getUniqueId(), Constants.DB_COLLECTION_CONTROL_PORTS))
            db.getCollection(Constants.DB_COLLECTION_CONTROL_PORTS).insertOne(controlPort.toBSON());

            return true;
    }
    private boolean newInformationPort(InformationPort informationPort) {
        if (informationPort == null) return false;
        
        if(!fieldExists(informationPort.getUniqueId(), Constants.DB_COLLECTION_INFORMATION_PORTS))
            db.getCollection(Constants.DB_COLLECTION_INFORMATION_PORTS).insertOne(informationPort.toBSON());
        
            if (informationPort.getKpis() != null)
                informationPort.getKpis().forEach((kpi) -> newKPI(kpi));
                    
            return true;
    }
    private boolean newParameterPort(ParameterPort parameterPort) {
        if (parameterPort == null) return false;
        
        if(!fieldExists(parameterPort.getUniqueId(), Constants.DB_COLLECTION_PARAMETER_PORTS))
            db.getCollection(Constants.DB_COLLECTION_PARAMETER_PORTS).insertOne(parameterPort.toBSON());

            return true;
    }


    protected ParameterPort loadParameterPort(String parameterPortId) throws ParseException {
        if (parameterPortId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);   
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, parameterPortId), Constants.DB_COLLECTION_PARAMETER_PORTS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;

        List<Parameter> parameters = new LinkedList<>();

if (doc.get(DatabaseConstants.PARAMETER_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PARAMETER_IDS)).stream().forEach((line) -> {
            try {
                parameters.add(loadParameter(line));
            } catch (ParseException ex) {
                logger.error("loadParameterPort - parameterIds: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("parameterIds")).entrySet())
//            parameters.add(loadParameter(entry.getValue().toString()));
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new ParameterPort(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.TYPE),
            doc.getString(DatabaseConstants.DIRECTION),
                parameters,
            reg 
        );
    }

    protected ControlPort loadControlPort(String controlPortId) throws ParseException {
        if (controlPortId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, controlPortId), Constants.DB_COLLECTION_CONTROL_PORTS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return ControlPort.fromBSON(doc);
/*        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new ControlPort(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.TYPE),
            doc.getString(DatabaseConstants.DIRECTION),
            reg 
        );
*/        
    }

    /*********************************************/
    /* KPI */ 
    /*********************************************/
    protected KPISetting loadKPISetting(String kpiSettingId) throws ParseException {
        if (kpiSettingId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, kpiSettingId), Constants.DB_COLLECTION_KPI_SETTINGS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new KPISetting(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            loadKPI(doc.getString(DatabaseConstants.KPI_ID)),
            doc.getString(DatabaseConstants.TYPE),
            doc.getString(DatabaseConstants.UNIT),
            doc.getString(DatabaseConstants.VALUE),
            reg
        );        
    }    
    protected KPISetting loadREDKPISetting(String kpiSettingId, String REDregistered) throws ParseException {
        if (kpiSettingId == null) return null;
        if (REDregistered == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        List<Document> docs = new LinkedList<>();
        Bson myFilter = com.mongodb.client.model.Filters.and
        (
                eq(DatabaseConstants.UNIQUE_ID, kpiSettingId),
                eq(DatabaseConstants.REGISTERED, REDregistered)                
        );
        db.getCollection(Constants.DB_COLLECTION_RED_KPI_SETTINGS).find(myFilter).into(docs);
        
        // List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, kpiSettingId), Constants.DB_COLLECTION_KPI_SETTINGS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new KPISetting(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            loadKPI(doc.getString(DatabaseConstants.KPI_ID)),
            doc.getString(DatabaseConstants.TYPE),
            doc.getString(DatabaseConstants.UNIT),
            doc.getString(DatabaseConstants.VALUE),
            reg
        );        
    }    
    protected ParameterSetting loadREDParameterSetting(String parameterSettingId, String REDregistered) throws ParseException {
        if (parameterSettingId == null) return null;
        if (REDregistered == null) return null;        
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);  
        
        List<Document> docs = new LinkedList<>();
        Bson myFilter = com.mongodb.client.model.Filters.and
        (
                eq(DatabaseConstants.UNIQUE_ID, parameterSettingId),
                eq(DatabaseConstants.REGISTERED, REDregistered)                
        );
        db.getCollection(Constants.DB_COLLECTION_RED_PARAMETER_SETTINGS).find(myFilter).into(docs);        
        
//        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, parameterSettingId), Constants.DB_COLLECTION_PARAMETER_SETTINGS);
        if (docs == null || docs.isEmpty()) return null;        
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new ParameterSetting(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.VALUE),
            loadParameter(doc.getString(DatabaseConstants.PARAMETER_ID)),
            reg 
//            doc.getString("registered")
        );
    }

    protected KPI loadKPI(String kpiId) {
        if (kpiId == null) return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, kpiId), Constants.DB_COLLECTION_KPIS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return KPI.fromBSON(doc);
    }
    
    protected TriggeredSkill loadTriggeredSkill(String triggeredSkillId) {
        if (triggeredSkillId == null) return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, triggeredSkillId), Constants.DB_COLLECTION_TRIGGERED_SKILLS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return TriggeredSkill.fromBSON(doc);
    }

    protected TriggeredRecipe loadTriggeredRecipe(String triggeredRecipeId) {
        if (triggeredRecipeId == null) return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, triggeredRecipeId), Constants.DB_COLLECTION_TRIGGERED_RECIPES);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return TriggeredRecipe.fromBSON(doc);
    }

    protected boolean newKPISetting(KPISetting setting) {
        if (setting == null) return false;
        
        if(!fieldExists(setting.getUniqueId(), Constants.DB_COLLECTION_KPI_SETTINGS)) 
        {
            db.getCollection(Constants.DB_COLLECTION_KPI_SETTINGS).insertOne(setting.toBSON());
        
            // create or at least check the related KPI
            if (setting.getKpi() != null)
                newKPI(setting.getKpi());

            return true;
        }
        else
            return false;
    }    
    protected boolean newREDKPISetting(KPISetting setting) {
        if (setting == null) return false;
        
        db.getCollection(Constants.DB_COLLECTION_RED_KPI_SETTINGS).insertOne(setting.toBSON());
        
        // kpi must exist for sure!
//        if (setting.getKpi() != null)
//            newKPI(setting.getKpi());
        
        return true;
    }    
    protected boolean newKPI(KPI kpi) {
        if (kpi == null) return false;
        
        if(!fieldExists(kpi.getUniqueId(), Constants.DB_COLLECTION_KPIS)) 
            db.getCollection(Constants.DB_COLLECTION_KPIS).insertOne(kpi.toBSON());
        return true;
    }
    
    protected boolean newTriggeredSkill(TriggeredSkill triggeredSkill) {
        if (triggeredSkill == null) return false;
        if (triggeredSkill.getUniqueId() == null || triggeredSkill.getUniqueId().length() == 0) return false;
        if (triggeredSkill.getSkillId() == null || triggeredSkill.getSkillId().length() == 0) return false;
        if (triggeredSkill.getRecipeId() == null || triggeredSkill.getRecipeId().length() == 0) return false;
        if (triggeredSkill.getProductInstanceId() == null || triggeredSkill.getProductInstanceId().length() == 0) return false;
        
        if(!fieldExists(triggeredSkill.getUniqueId(), Constants.DB_COLLECTION_TRIGGERED_SKILLS)) 
            db.getCollection(Constants.DB_COLLECTION_TRIGGERED_SKILLS).insertOne(triggeredSkill.toBSON());
        return true;
    }
    
    protected boolean newTriggeredRecipe(TriggeredRecipe triggeredRecipe) {
        if (triggeredRecipe == null) return false;
        if (triggeredRecipe.getUniqueId() == null || triggeredRecipe.getUniqueId().length() == 0) return false;
        if (triggeredRecipe.getRecipeId() == null || triggeredRecipe.getRecipeId().length() == 0) return false;
        if (triggeredRecipe.getProductInstanceId() == null || triggeredRecipe.getProductInstanceId().length() == 0) return false;
        
        if(!fieldExists(triggeredRecipe.getUniqueId(), Constants.DB_COLLECTION_TRIGGERED_RECIPES)) 
            db.getCollection(Constants.DB_COLLECTION_TRIGGERED_RECIPES).insertOne(triggeredRecipe.toBSON());
        return true;
    }
    
    protected boolean updateKPISetting(KPISetting kpiSetting) {
        if (kpiSetting == null) return false;
        
        if(fieldExists(kpiSetting.getUniqueId(), Constants.DB_COLLECTION_KPI_SETTINGS)) 
        {
            db.getCollection(Constants.DB_COLLECTION_KPI_SETTINGS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, kpiSetting.getUniqueId()), kpiSetting.toBSON());
        
            // update also related KPI
            updateKPI(kpiSetting.getKpi());

            return true;
        }
        else
            return false;
    }
    protected boolean updateParameterSetting(ParameterSetting parameterSetting) {
        if (parameterSetting == null) return false;
        
        if(fieldExists(parameterSetting.getUniqueId(), Constants.DB_COLLECTION_PARAMETER_SETTINGS)) 
        {
            db.getCollection(Constants.DB_COLLECTION_PARAMETER_SETTINGS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, parameterSetting.getUniqueId()), parameterSetting.toBSON());
        
            // update also related KPI
            updateParameter(parameterSetting.getParameter());

            return true;
        }
        else
            return false;
    }
    protected boolean updateKPI(KPI kpi) {
        if (kpi != null)
            if(fieldExists(kpi.getUniqueId(), Constants.DB_COLLECTION_KPIS)) 
            {
                db.getCollection(Constants.DB_COLLECTION_KPIS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, kpi.getUniqueId()), kpi.toBSON());
                return true;
            }
        
        return false;
    }
    
    private boolean removeKPI(String kpiId) {
        if (kpiId == null)
            return false;
        if(!fieldExists(kpiId, Constants.DB_COLLECTION_KPIS))           
            return false;
        
        // must delete also kpisettings related to this KPI
        List<Document> kpiSettingsRelated = readData(eq(DatabaseConstants.UNIQUE_ID, kpiId), Constants.DB_COLLECTION_KPI_SETTINGS);
        if (kpiSettingsRelated != null)
        for (Document kpiSettingRelated : kpiSettingsRelated)
        {
            String kpiSettingId = kpiSettingRelated.getString((DatabaseConstants.UNIQUE_ID));
            removeKPISetting(kpiSettingId);
        }
        
        db.getCollection(Constants.DB_COLLECTION_KPIS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, kpiId));
        return true;        
    }

    private boolean removeKPISetting(String kpiSettingId) {
        if (kpiSettingId == null)
            return false;
        // first delete child records, then master record
        if(!fieldExists(kpiSettingId, Constants.DB_COLLECTION_KPI_SETTINGS))           
            return false;
        
        Document kpiSettingDocument;

        // load the old document
///////////////////////////        kpiSettingDocument = readData(eq(DatabaseConstants.UNIQUE_ID, kpiSettingId), Constants.DB_COLLECTION_KPI_SETTINGS).get(0);
        
        // remove all child records
        // TBV kpi needs to be removed? NO
        
        // TODO must check which tables link to kpisettings and clean them as well

        // remove master record
        db.getCollection(Constants.DB_COLLECTION_KPI_SETTINGS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, kpiSettingId));

        return true;        
    }
    
    /************************************************/
    /* LOCATION */
    /************************************************/
    protected boolean newPhysicalLocation(PhysicalLocation physicalLocation) {
        if (physicalLocation == null)
            return false;
        
        db.getCollection(Constants.DB_COLLECTION_PHYSICAL_LOCATIONS).insertOne(physicalLocation.toBSON());
        logger.debug("DB INTERFACE - NEW PHYSICAL LOCATION: " + physicalLocation.toString()); 
        return true;
    }
    protected PhysicalLocation loadPhysicalLocation(String physicalLocationId) throws ParseException {
        if (physicalLocationId == null)
            return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, physicalLocationId), Constants.DB_COLLECTION_PHYSICAL_LOCATIONS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return (PhysicalLocation)PhysicalLocation.fromBSON2(doc, PhysicalLocation.class);        
    }    

    /*************************************************/
    /* MODULE */
    /*************************************************/
    protected Module loadModule(String moduleId) throws ParseException {
        if (moduleId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, moduleId), Constants.DB_COLLECTION_MODULES);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        List<Skill> skills = new LinkedList<>();
if (doc.get(DatabaseConstants.SKILL_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.SKILL_IDS)).stream().forEach((line) -> {
            try {
                skills.add(loadSkill(line));
            } catch (ParseException ex) {
                logger.error("loadModule - skillIds: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("skillIds")).entrySet())
//            skills.add(loadSkill(entry.getValue().toString()));

        List<PhysicalPort> ports = new LinkedList<>();        
if (doc.get(DatabaseConstants.PHYSICAL_PORT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PHYSICAL_PORT_IDS)).stream().forEach((line) -> {
            try {
                ports.add(loadPhysicalPort(line));
            } catch (ParseException ex) {
                logger.error("loadModule - portIds: " + ex);
            }
        });

        List<PhysicalAdjustmentParameter> physicalAdjustmentParameters = new LinkedList<>();        
if (doc.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS)).stream().forEach((line) -> {
            try {
                physicalAdjustmentParameters.add(loadPhysicalAdjustmentParameter(line));
            } catch (ParseException ex) {
                logger.error("loadModule - portIds: " + ex);
            }
        });

        List<Module> internalModules = new LinkedList<>();    
if (doc.get(DatabaseConstants.INTERNAL_MODULE_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.INTERNAL_MODULE_IDS)).stream().forEach((line) -> {
            try {
                internalModules.add(loadModule(line));
            } catch (ParseException ex) {
                logger.error("loadModule - moduleIds: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("moduleIds")).entrySet())
//            internalModules.add(loadModule(entry.getValue().toString()));

        List<Recipe> recipes = new LinkedList<>();    
if (doc.get(DatabaseConstants.RECIPE_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((recipeId) -> {
            try {
                recipes.add(loadRecipe(recipeId));
            } catch (ParseException ex) {
                logger.error("loadRecipe - recipeIds: " + ex);
            }
        });

        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new Module(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getBoolean(DatabaseConstants.CONNECTED).booleanValue(),
            skills,
                ports,
                physicalAdjustmentParameters,
                internalModules,
                recipes,
                doc.getString(DatabaseConstants.ADDRESS),
                doc.getString(DatabaseConstants.STATUS),
                doc.getString(DatabaseConstants.MANUFACTURER),
                doc.getString(DatabaseConstants.PARENT_ID),
                doc.getString(DatabaseConstants.PARENT_TYPE),
            reg
        );
    }    
    protected boolean newModules(List<Module> modules) {
        if (modules == null)
            return false;
        
        modules.forEach((module) -> {
            newModule(module);
        });
        return true;
    }
    protected boolean newModule(Module module) {
        if (module == null)
            return false;
        
        if(!fieldExists(module.getUniqueId(), Constants.DB_COLLECTION_MODULES)) {
            db.getCollection(Constants.DB_COLLECTION_MODULES).insertOne(module.toBSON());

            if (module.getSkills() != null)
                module.getSkills().forEach((skill) -> newSkill(skill));
            
            if (module.getRecipes() != null)
                module.getRecipes().forEach((recipe) -> newRecipe(recipe));
            
            if (module.getPhysicalPorts() != null)
                module.getPhysicalPorts().forEach((physicalPort) -> newPhysicalPort(physicalPort));
            
            if (module.getPhysicalAdjustmentParameters() != null)
                module.getPhysicalAdjustmentParameters().forEach((physicalAdjustmentParameter) -> newPhysicalAdjustmentParameter(physicalAdjustmentParameter));
            
            if (module.getInternalModules() != null)
                module.getInternalModules().forEach((internalModule) -> newModule(internalModule));
        }
        return true;
    }

    // TODO implement module removal
    

    /*********************************************/
    /* PARAMETER */
    /*********************************************/
    protected ParameterSetting loadParameterSetting(String parameterSettingId) throws ParseException {
        if (parameterSettingId == null)
            return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);  
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, parameterSettingId), Constants.DB_COLLECTION_PARAMETER_SETTINGS);
        if (docs == null || docs.isEmpty()) return null;        
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new ParameterSetting(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.VALUE),
            loadParameter(doc.getString(DatabaseConstants.PARAMETER_ID)),
            reg 
//            doc.getString("registered")
        );
    }

    protected PhysicalAdjustmentParameterSetting loadPhysicalAdjustmentParameterSetting(String physicalAdjustmentParameterSettingId) throws ParseException {
        if (physicalAdjustmentParameterSettingId == null)
            return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);  
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, physicalAdjustmentParameterSettingId), Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETER_SETTINGS);
        if (docs == null || docs.isEmpty()) return null;        
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new PhysicalAdjustmentParameterSetting(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.VALUE),
            loadPhysicalAdjustmentParameter(doc.getString(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_ID)),
            reg 
        );
    }

    protected boolean newParameterSetting(ParameterSetting setting) {
        if (setting == null)
            return false;
        
        if(!fieldExists(setting.getUniqueId(), Constants.DB_COLLECTION_PARAMETER_SETTINGS)) 
        {
            db.getCollection(Constants.DB_COLLECTION_PARAMETER_SETTINGS).insertOne(setting.toBSON());
        
            // try to insert the parameter
            if (setting.getParameter() != null)
                newParameter(setting.getParameter());

            return true;
        }
        else
            return false;
    } 
    protected boolean newPhysicalAdjustmentParameterSetting(PhysicalAdjustmentParameterSetting setting) {
        if (setting == null)
            return false;
        
        if(!fieldExists(setting.getUniqueId(), Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETER_SETTINGS)) 
        {
            db.getCollection(Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETER_SETTINGS).insertOne(setting.toBSON());
        
            // do not try to insert the adjustment parameter cause it has to exist
//            if (setting.getParameter() != null)
//                newParameter(setting.getParameter());

            return true;
        }
        else
            return false;
    } 
    
    protected boolean newREDParameterSetting(ParameterSetting setting) {
        if (setting == null)
            return false;
        
        db.getCollection(Constants.DB_COLLECTION_RED_PARAMETER_SETTINGS).insertOne(setting.toBSON());
        
        // parameter must exist for sure!
//        if (setting.getParameter() != null)
//            newParameter(setting.getParameter());

        return true;
    } 
    
    protected Parameter loadParameter(String parameterId) throws ParseException {
        if (parameterId == null)
            return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, parameterId), Constants.DB_COLLECTION_PARAMETERS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return Parameter.fromBSON(doc);
    }
    protected PhysicalAdjustmentParameter loadPhysicalAdjustmentParameter(String parameterId) throws ParseException {
        if (parameterId == null)
            return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, parameterId), Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETERS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return PhysicalAdjustmentParameter.fromBSON(doc);
    }

    protected boolean newParameter(Parameter p) {
        if (p == null) return false;
        
        if(!fieldExists(p.getUniqueId(), Constants.DB_COLLECTION_PARAMETERS)) db.getCollection(Constants.DB_COLLECTION_PARAMETERS).insertOne(p.toBSON());
        return true;
    }
    protected boolean newPhysicalAdjustmentParameter(PhysicalAdjustmentParameter p) {
        if (p == null) return false;
        
        if(!fieldExists(p.getUniqueId(), Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETERS)) db.getCollection(Constants.DB_COLLECTION_PARAMETERS).insertOne(p.toBSON());
        return true;
    }
    protected boolean updateParameter(Parameter parameter) {
        if (parameter == null) return false;
        
        if(fieldExists(parameter.getUniqueId(), Constants.DB_COLLECTION_PARAMETERS)) 
            db.getCollection(Constants.DB_COLLECTION_PARAMETERS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, parameter.getUniqueId()), parameter.toBSON());
        return true;
    }

    /********************************************/
    /* PRODUCT */
    /********************************************/
    protected boolean newProduct(Product product) {
        if (product == null) return false;
        
        if(!fieldExists(product.getUniqueId(), Constants.DB_COLLECTION_PRODUCTS)) 
            db.getCollection(Constants.DB_COLLECTION_PRODUCTS).insertOne(product.toBSON());

        if (product.getParts() != null)
        for (Part part : product.getParts())
            newPart(part);
        
        if (product.getSkillRequirements() != null)
        for (SkillRequirement skillRequirement : product.getSkillRequirements())
            newSkillRequirement(skillRequirement);

        return true;
    } 
    protected boolean newPart(Part part) {
        if (part == null) return false;
        
        if(!fieldExists(part.getUniqueId(), Constants.DB_COLLECTION_PARTS)) 
            db.getCollection(Constants.DB_COLLECTION_PARTS).insertOne(part.toBSON());
        
        return true;
    }

    protected boolean newProductInstance(ProductInstance productInstance) {
        if (productInstance == null)
            return false;
                    
        if(!fieldExists(productInstance.getUniqueId(), Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS)) 
            db.getCollection(Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS).insertOne(productInstance.toBSON());

        if (productInstance.getPartInstances() != null)
        for (PartInstance partInstance : productInstance.getPartInstances())
            newPartInstance(partInstance);
        
        return true;
    }
    protected boolean newPartInstance(PartInstance partInstance) {
        if (partInstance == null)
            return false;
        
        if(!fieldExists(partInstance.getUniqueId(), Constants.DB_COLLECTION_PART_INSTANCES)) 
            db.getCollection(Constants.DB_COLLECTION_PART_INSTANCES).insertOne(partInstance.toBSON());
        
        return true;
    }
    
    
    protected Product loadProduct(String productId) throws ParseException {        
        if (productId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);  

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, productId), Constants.DB_COLLECTION_PRODUCTS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        List<Part> parts = new LinkedList<>();    
if (doc.get(DatabaseConstants.PART_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.PART_IDS)).stream().forEach((line) -> {
                parts.add(loadPart(line));
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("partIds")).entrySet())
//            parts.add(loadPart(entry.getValue().toString()));
        
        List<SkillRequirement> skillRequirements = new LinkedList<>();    
if (doc.get(DatabaseConstants.SKILL_REQUIREMENT_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.SKILL_REQUIREMENT_IDS)).stream().forEach((line) -> {
            try {
                skillRequirements.add(loadSkillRequirement(line));
            } catch (ParseException ex) {
                logger.error("loadProduct - skillRequirementIds: " + ex);
            }
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("skillRequirementIds")).entrySet())
//            skillRequirements.add(loadSkillRequirement(entry.getValue().toString()));
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new Product(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            parts,
            skillRequirements,
            reg 
        );
    }
    private Part loadPart(String partId) {
        if (partId == null) return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, partId), Constants.DB_COLLECTION_PARTS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return (Part)Part.fromBSON2(doc, Part.class);
    }

    protected ProductInstance loadProductInstance(String productInstanceId) throws ParseException {
        if (productInstanceId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);  

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, productInstanceId), Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        List<PartInstance> partInstances = new LinkedList<>();     
if (doc.get(DatabaseConstants.PART_INSTANCE_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.PART_INSTANCE_IDS)).stream().forEach((line) -> {
                partInstances.add(loadPartInstance(line));
        });
//        for(Entry<String, Object> entry : ((Document) doc.get("partInstanceIds")).entrySet())
//            partInstances.add(loadPartInstance(entry.getValue().toString()));
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);

/*        
        Date finishedTime = null;
        String finishedTimeStr = doc.getString(DatabaseConstants.FINISHED_TIME);
        if (finishedTimeStr != null && !finishedTimeStr.equalsIgnoreCase("null"))
            finishedTime = sdf.parse(finishedTimeStr);
*/
        Date finishedTime = doc.getDate(DatabaseConstants.FINISHED_TIME);

/*        
        Date startedProductionTime = null;
        String startedProductionTimeStr = doc.getString(DatabaseConstants.STARTED_PRODUCTION_TIME);
        if (startedProductionTimeStr != null && !startedProductionTimeStr.equalsIgnoreCase("null"))
            startedProductionTime = sdf.parse(startedProductionTimeStr);
*/
        Date startedProductionTime = null;
        try
        {
            startedProductionTime = doc.getDate(DatabaseConstants.STARTED_PRODUCTION_TIME);
        }
        catch (Exception e)
        {
            logger.error("error in date parsing, please check startedProductionTime format for productInstanceId = " + productInstanceId);
        }

        ProductInstanceStatus status = null;
        String productInstanceStatus = doc.getString(DatabaseConstants.STATUS);
        if (productInstanceStatus != null)
            status = ProductInstanceStatus.valueOf(productInstanceStatus);
        
        return new ProductInstance(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.PRODUCT_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.ORDER_ID),
            partInstances,

            doc.getBoolean(DatabaseConstants.FINISHED),  // boolean finished,
            finishedTime,
            status,
            startedProductionTime,
                
            reg 
        );
    }
    private PartInstance loadPartInstance(String partInstanceId) {
        if (partInstanceId == null) return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, partInstanceId), Constants.DB_COLLECTION_PART_INSTANCES);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        return (PartInstance)PartInstance.fromBSON2(doc, PartInstance.class);
    }

    private SkillRequirement loadSkillRequirement(String skillRequirementId) throws ParseException {
        if (skillRequirementId == null) return null;
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, skillRequirementId), Constants.DB_COLLECTION_SKILL_REQUIREMENTS);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        List<SkillReqPrecedent> sqRecPrecedents = new LinkedList<>();

if (doc.get(DatabaseConstants.PRECEDENT_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.PRECEDENT_IDS)).stream().forEach((line) -> {
//        for(Entry<String, Object> entry : ((Document) doc.get("precedentIds")).entrySet())
//        {
            SkillReqPrecedent srp = new SkillReqPrecedent();
            
//            SkillRequirement sr = loadSkillRequirement(entry.getValue().toString());
            SkillRequirement sr;
            try {
                sr = loadSkillRequirement(line);
                srp.setUniqueId(sr.getUniqueId());
                srp.setName(sr.getName());
                srp.setRegistered(sr.getRegistered());
            } catch (ParseException ex) {
                logger.error("loadSkillRequirement - precedentIds: " + ex);
            }
            
            sqRecPrecedents.add(srp);
        });
         
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        SkillRequirement sr = new SkillRequirement(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME),
            doc.getString(DatabaseConstants.TYPE),
            loadSkillType(doc.getString(DatabaseConstants.SKILL_TYPE_ID)),
//            List<String> precedentIds,
            sqRecPrecedents,
            loadPart(doc.getString(DatabaseConstants.REQUIRES_PART_ID)),
            reg        
    );
      return sr;  
    }


    /*************************************************/
    /* RECIPE */ 
    /*************************************************/
    
    protected Recipe loadRecipe(String recipeId) throws ParseException {
        logger.debug("loadRecipe - 1");
        if (recipeId == null) return null;
        logger.debug("loadRecipe - 2");

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, recipeId), Constants.DB_COLLECTION_RECIPES);
        logger.debug("loadRecipe - 3");
        if (docs == null || docs.isEmpty()) return null;
        logger.debug("loadRecipe - 4");
        Document doc = docs.get(0);
        logger.debug("loadRecipe - 5");
        if (doc == null) return null;
                logger.debug("loadRecipe - 6");

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        logger.debug("loadRecipe - 17");
        
        List<KPISetting> kpiSettings = new LinkedList<>();
        List<ParameterSetting> parameterSettings = new LinkedList<>();
        List<SkillRequirement> skillRequirements = new LinkedList<>();
        List<String> equipmentIds = new LinkedList<>();

logger.trace("loadRecipe - id = " + recipeId + " - equipmentIds");
if (doc.get(DatabaseConstants.EQUIPMENT_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.EQUIPMENT_IDS)).stream().forEach((line) -> {
                equipmentIds.add(line);
});


logger.trace("loadRecipe - id = " + recipeId + " - kpisettings");
if (doc.get(DatabaseConstants.KPI_SETTING_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.KPI_SETTING_IDS)).stream().forEach((line) -> {
            try {
                kpiSettings.add(loadKPISetting(line));
            } catch (ParseException ex) {
                logger.error("loadRecipe - kpiSettings: " + ex);
            }
});
//        for(Entry<String, Object> entry : ((Document) doc.get("kpiSettings")).entrySet())
//            kpiSettings.add(loadKPISetting(entry.getValue().toString()));

logger.trace("loadRecipe - id = " + recipeId + " - parametersettings");
if (doc.get(DatabaseConstants.PARAMETER_SETTING_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PARAMETER_SETTING_IDS)).stream().forEach((line) -> {
            try {
                parameterSettings.add(loadParameterSetting(line));
            } catch (ParseException ex) {
                logger.error("loadRecipe - parameterSettings: " + ex);
            }
});
//        for(Entry<String, Object> entry : ((Document) doc.get("parameterSettings")).entrySet())
//            parameterSettings.add(loadParameterSetting(entry.getValue().toString()));

logger.trace("loadRecipe - id = " + recipeId + " - skillreqs");
if (doc.get(DatabaseConstants.SKILL_REQUIREMENT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.SKILL_REQUIREMENT_IDS)).stream().forEach((line) -> {
                //            for(Entry<String, Object> entry : ((Document) doc.get("skillRequirements")).entrySet())
//            skillRequirements.add(SkillRequirement.fromBSON(readData(eq("id", entry.getValue()), Constants.DB_COLLECTION_SKILL_REQUIREMENTS).get(0)));
                List<Document> docums = readData(
                        eq(DatabaseConstants.UNIQUE_ID, line), Constants.DB_COLLECTION_SKILL_REQUIREMENTS
                );
                if (docums != null && !docums.isEmpty())
                {
                    Document docum = docums.get(0);
                    if (docum != null)
                    {
                        String documString = docum.toString();
                        try {
                            skillRequirements.add(
                                    loadSkillRequirement(documString));
                        } catch (ParseException ex) {
                            logger.error("loadRecipe - skillRequirements: " + ex);
                        }
                    }
                }
            }
);
         
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
logger.debug("loadRecipe - registered = " + registered);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
logger.debug("loadRecipe - reg = " + reg);
        
logger.trace("loadRecipe - id = " + recipeId + " - recipe");
        Recipe r = new Recipe(
            doc.getString(DatabaseConstants.DESCRIPTION),
            doc.getString(DatabaseConstants.UNIQUE_ID),
            kpiSettings,
            doc.getString(DatabaseConstants.NAME),
            parameterSettings,
            doc.getString(DatabaseConstants.UNIQUE_AGENT_NAME),
            skillRequirements,
            loadSkill(doc.getString(DatabaseConstants.SKILL_ID)),
            loadControlPort(doc.getString(DatabaseConstants.EXECUTED_BY_SKILL_CONTROL_PORT_ID)),
//            loadEquipment(doc.getString("equipmentId")),
//            loadModule(doc.getString("equipmentId")),
//                loadSubSystem(doc.getString("equipmentId")),
//            doc.getString(DatabaseConstants.EQUIPMENT_ID),
                equipmentIds,
            doc.getString(DatabaseConstants.MSB_PROTOCOL_ENDPOINT),
                doc.getBoolean(DatabaseConstants.VALID),
            reg        
//            doc.getString("registered")        
        );
logger.trace("loadRecipe - id = " + recipeId + " - last two fields");        
        r.setOptimized(doc.getBoolean(DatabaseConstants.OPTIMIZED));
        
        String optimizedTime = doc.getString(DatabaseConstants.OPTIMIZED_TIME);
        if (optimizedTime != null 
                && !optimizedTime.equalsIgnoreCase("null"))
            r.setLastOptimizationTime(sdf.parse(optimizedTime));
        
logger.trace("loadRecipe - id = " + recipeId + " - recipes is : " + r);
        return r;
    }
    
    // We need to cater for what to do when recipe/skill/stuff already exists in the database    
    protected boolean newRecipe(Recipe recipe) {
        if (recipe == null) return false;
        
        if(!fieldExists(recipe.getUniqueId(), Constants.DB_COLLECTION_RECIPES)) {
            db.getCollection(Constants.DB_COLLECTION_RECIPES).insertOne(recipe.toBSON());
            
            if (recipe.getKpiSettings() != null)
                recipe.getKpiSettings().forEach((setting) -> newKPISetting(setting));
            
            if (recipe.getParameterSettings() != null)
                recipe.getParameterSettings().forEach((setting) -> newParameterSetting(setting));
            
            if (recipe.getSkillRequirements() != null)
                recipe.getSkillRequirements().forEach((requirement) -> newSkillRequirement(requirement));

            //
            if (recipe.getSkill() != null)
                newSkill(recipe.getSkill());
//            else
//                throw new RuntimeException("Recipe skill cannot be null!");
            
            if (recipe.getExecutedBySkillControlPort() != null)
                newControlPort(recipe.getExecutedBySkillControlPort());

            // VaG - 15/11/2017
            history_newRecipe(recipe);
        }
        logger.debug("DB INTERFACE - PROCESS NEW RECIPE: " + recipe.toString()); 
        return true;
    }  
    
    protected boolean removeRecipe(String recipeId) {
        if (recipeId == null) return false;
        
        // first delete child records, then master record
        if(!fieldExists(recipeId, Constants.DB_COLLECTION_RECIPES))           
            return false;
        
        Document recipeDocument;

        // load the old document
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, recipeId), Constants.DB_COLLECTION_RECIPES);
        if (docs == null || docs.isEmpty()) return false;
        recipeDocument = docs.get(0);
        if (recipeDocument == null) return false;
        
        // remove all child records
if (recipeDocument.get(DatabaseConstants.KPI_SETTING_IDS) != null)        
((ArrayList<String>) recipeDocument.get(DatabaseConstants.KPI_SETTING_IDS)).stream().forEach((line) -> {
    removeKPISetting(line);
});
//    for(Entry<String, Object> entry : ((Document) recipeDocument.get("kpiSettings")).entrySet())
//            removeKPISetting(entry.getValue().toString());

if (recipeDocument.get(DatabaseConstants.PARAMETER_SETTING_IDS) != null)
((ArrayList<String>) recipeDocument.get(DatabaseConstants.PARAMETER_SETTING_IDS)).stream().forEach((line) -> {
    removeParameterSetting(line);
});
//        for(Entry<String, Object> entry : ((Document) recipeDocument.get("parameterSettings")).entrySet())
//            removeParameterSetting(entry.getValue().toString());

if (recipeDocument.get(DatabaseConstants.SKILL_REQUIREMENT_IDS) != null)
((ArrayList<String>) recipeDocument.get(DatabaseConstants.SKILL_REQUIREMENT_IDS)).stream().forEach((line) -> {
    removeSkillRequirement(line);
});
//        for(Entry<String, Object> entry : ((Document) recipeDocument.get("skillRequirements")).entrySet())
//            removeSkillRequirement(entry.getValue().toString());

        // TBV skill needs to be removed? NO
        // TBV equipment needs to be removed? NO
        
        // need to advice that the recipe no longer exists:
        // 1) agents have recipes list, via the equipment id
/*
        String equipmentId = recipeDocument.getString(DatabaseConstants.EQUIPMENT_ID);    
        if (equipmentId != null)
        removeRecipeFromAgent(equipmentId, recipeId);
*/
if (recipeDocument.get(DatabaseConstants.EQUIPMENT_IDS) != null)
((ArrayList<String>) recipeDocument.get(DatabaseConstants.EQUIPMENT_IDS)).stream().forEach((line) -> {
    removeRecipeFromAgent(line, recipeId);
});


        // 2) execution table rows, need to remove lines regarding that recipe
        List<Document> docums = readData(eq(DatabaseConstants.RECIPE_ID, recipeId), Constants.DB_COLLECTION_EXECUTION_TABLE_ROWS);
        if (docums != null || !docums.isEmpty())
            docums.forEach((executionTableRowDocument) -> removeSingleExecutionTableRow(executionTableRowDocument));
        
        // 3) skills, have recipes list
        String skillId = recipeDocument.getString(DatabaseConstants.SKILL_ID);        
        removeRecipeFromSkill(skillId, recipeId);

        // remove master record
        db.getCollection(Constants.DB_COLLECTION_RECIPES).deleteOne(eq(DatabaseConstants.UNIQUE_ID, recipeId));

        return true;        
    }

    private boolean removeControlPort(String controlPortId) {
        if (controlPortId == null) return false;
        
        if(!fieldExists(controlPortId, Constants.DB_COLLECTION_CONTROL_PORTS))           
            return false;
        db.getCollection(Constants.DB_COLLECTION_CONTROL_PORTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, controlPortId));
        return true;        
    }

    private boolean removeInformationPort(String informationPortId) {
        if (informationPortId == null) return false;
        
        if(!fieldExists(informationPortId, Constants.DB_COLLECTION_INFORMATION_PORTS))           
            return false;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, informationPortId), Constants.DB_COLLECTION_INFORMATION_PORTS);
        if (docs == null || docs.isEmpty() ) return false;
        Document doc = docs.get(0);
        if (doc == null) return false;

if (doc.get(DatabaseConstants.KPI_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.KPI_IDS)).stream().forEach((line) -> {
            removeKPI(line);
});
//        for(Entry<String, Object> entry : ((Document) doc.get("kpiIds")).entrySet())
//            removeKPI(entry.getValue().toString());
        
        db.getCollection(Constants.DB_COLLECTION_INFORMATION_PORTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, informationPortId));
        return true;        
    }

    private boolean removeParameterPort(String parameterPortId) {
        if (parameterPortId == null) return false;
        
        if(!fieldExists(parameterPortId, Constants.DB_COLLECTION_PARAMETER_PORTS))           
            return false;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, parameterPortId), Constants.DB_COLLECTION_PARAMETER_PORTS);
        if (docs == null || docs.isEmpty()) return false;
        Document doc = docs.get(0);
        if (doc == null) return false;

if (doc.get(DatabaseConstants.PARAMETER_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.PARAMETER_IDS)).stream().forEach((line) -> {
            removeParameter(line);
});
//        for(Entry<String, Object> entry : ((Document) doc.get("parameterIds")).entrySet())
//            removeParameter(entry.getValue().toString());
        
        db.getCollection(Constants.DB_COLLECTION_PARAMETER_PORTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, parameterPortId));
        return true;        
    }

    boolean newEquipmentObservation(EquipmentObservation equipmentObservation) {
        if (equipmentObservation == null) return false;
        
        if(!fieldExists(equipmentObservation.getUniqueId(), Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS))
            db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS).insertOne(equipmentObservation.toBSON());

        logger.debug("DB INTERFACE - PROCESS NEW EQUIPMENT OBSERVATION: " + equipmentObservation.toString()); 
        return true;
    }

    boolean newEquipmentObservationRel2(EquipmentObservationRel2 equipmentObservationRel2) {
        if (equipmentObservationRel2 == null) return false;
        
        if(!fieldExists(equipmentObservationRel2.getUniqueId(), Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2))
            db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2).insertOne(equipmentObservationRel2.toBSON());

        for (EquipmentObservationRel2Row row : equipmentObservationRel2.getRows())
            newEquipmentObservationRel2Row(row);
        
        logger.debug("DB INTERFACE - PROCESS NEW EQUIPMENT OBSERVATION REL2: " + equipmentObservationRel2.toString()); 
        return true;
    }

    boolean newEquipmentAssessment(EquipmentAssessment equipmentAssessment) {
        if (equipmentAssessment == null) return false;
        
        if(!fieldExists(equipmentAssessment.getUniqueId(), Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS))
            db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS).insertOne(equipmentAssessment.toBSON());

        for (EquipmentAssessmentRow row : equipmentAssessment.getRows())
            newEquipmentAssessmentRow(row);
        
        logger.debug("DB INTERFACE - PROCESS NEW EQUIPMENT ASSESSMENT: " + equipmentAssessment.toString()); 
        return true;
    }

    boolean newProcessAssessment(ProcessAssessment processAssessment) {
        if (processAssessment == null) return false;
        
        if(!fieldExists(processAssessment.getUniqueId(), Constants.DB_COLLECTION_PROCESS_ASSESSMENTS))
            db.getCollection(Constants.DB_COLLECTION_PROCESS_ASSESSMENTS).insertOne(processAssessment.toBSON());

        for (ProcessAssessmentRow row : processAssessment.getRows())
            newProcessAssessmentRow(row);
        
        logger.debug("DB INTERFACE - PROCESS NEW PROCESS ASSESSMENT: " + processAssessment.toString()); 
        return true;
    }

    boolean newEquipmentObservationRel2Row(EquipmentObservationRel2Row equipmentObservationRel2Row) {
        if (equipmentObservationRel2Row == null) return false;
        
        if(!fieldExists(equipmentObservationRel2Row.getUniqueId(), Constants.DB_COLLECTION_EQUIPMENT_OBSERVATION_REL2_ROWS))
            db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATION_REL2_ROWS).insertOne(equipmentObservationRel2Row.toBSON());

        logger.debug("DB INTERFACE - PROCESS NEW EQUIPMENT OBSERVATION REL2 ROW: " + equipmentObservationRel2Row.toString()); 
        return true;
    }

    boolean newEquipmentAssessmentRow(EquipmentAssessmentRow equipmentAssessmentRow) {
        if (equipmentAssessmentRow == null) return false;
        
        if(!fieldExists(equipmentAssessmentRow.getUniqueId(), Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENT_ROWS))
            db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENT_ROWS).insertOne(equipmentAssessmentRow.toBSON());

        logger.debug("DB INTERFACE - PROCESS NEW EQUIPMENT ASSESSMENT ROW: " + equipmentAssessmentRow.toString()); 
        return true;
    }

    boolean newProcessAssessmentRow(ProcessAssessmentRow processAssessmentRow) {
        if (processAssessmentRow == null) return false;
        
        if(!fieldExists(processAssessmentRow.getUniqueId(), Constants.DB_COLLECTION_PROCESS_ASSESSMENT_ROWS))
            db.getCollection(Constants.DB_COLLECTION_PROCESS_ASSESSMENT_ROWS).insertOne(processAssessmentRow.toBSON());

        logger.debug("DB INTERFACE - PROCESS NEW PROCESS ASSESSMENT ROW: " + processAssessmentRow.toString()); 
        return true;
    }

    protected List<EquipmentObservation> listEquipmentObservations() throws ParseException 
    {
        List<EquipmentObservation> leo = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS);
        if (docList != null)
        for (Document doc : docList)
            leo.add(EquipmentObservation.fromBSON(doc));
        
        logger.debug(getClass().getName() + " - listEquipmentObservations -> " + leo);
        return leo;
    }

    protected List<EquipmentObservationRel2> listEquipmentObservationRel2s() throws ParseException 
    {
        List<EquipmentObservationRel2> leo = new LinkedList<>();
        // List<Document> docList = readList(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2);
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2)
                .find()
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        
        if (docList != null)
        for (Document doc : docList)
            leo.add(loadEquipmentObservationRel2(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listEquipmentObservationRel2s -> " + leo);
        return leo;
    }
    protected List<EquipmentObservationRel2> listSystemObservationRel2s() throws ParseException 
    {
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_TYPE, "subSystems"); // TODO maybe have a constant        
        logger.debug("listSystemObservationRel2s filter = [" + myFilter + "]");
        
        List<EquipmentObservationRel2> leo = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)            
        for (Document doc : docList)
            leo.add(loadEquipmentObservationRel2(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listSystemObservationRel2s -> " + leo);
        return leo;
    }
    protected List<EquipmentObservationRel2> listEquipmentObservationRel2s(String equipmentId) throws ParseException 
    {
/*        
        List<EquipmentObservationRel2> leo = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2);
        if (docList != null)
*/            
                        
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_TYPE, "subSystem"); // TODO maybe have a constant
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_ID, equipmentId);
        logger.debug("listEquipmentObservationRel2s filter = [" + myFilter + "]");
        
        List<EquipmentObservationRel2> leo = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_OBSERVATIONS_REL2)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)            
        for (Document doc : docList)
            leo.add(loadEquipmentObservationRel2(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listEquipmentObservationRel2s for subsystem -> " + leo);
        return leo;
    }

    protected List<EquipmentAssessment> listEquipmentAssessments() throws ParseException 
    {
        List<EquipmentAssessment> lea = new LinkedList<>();
        // List<Document> docList = readList(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS);
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS)
                .find()
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadEquipmentAssessment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listEquipmentAssessments -> " + lea);
        return lea;
    }
    protected List<EquipmentAssessment> listEquipmentAssessments(String equipmentId) throws ParseException 
    {
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_TYPE, "subSystem"); // TODO maybe have a constant
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_ID, equipmentId);
        logger.debug("listEquipmentAssessments filter = [" + myFilter + "]");
        
        List<EquipmentAssessment> lea = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadEquipmentAssessment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listEquipmentAssessments of subsystem -> " + lea);
        return lea;
    }
    protected List<EquipmentAssessment> listSystemAssessments() throws ParseException 
    {
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_TYPE, "subSystems"); // TODO maybe have a constant        
        logger.debug("listSystemAssessments filter = [" + myFilter + "]");
        
        List<EquipmentAssessment> lea = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadEquipmentAssessment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listSystemAssessments -> " + lea);
        return lea;
    }
    protected List<TriggeredSkill> listTriggeredSkills(String skillId) throws ParseException 
    {
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.SKILL_ID, skillId);
        logger.debug("listTriggeredSkills filter = [" + myFilter + "]");
        
        List<TriggeredSkill> lea = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        // docList = readList(Constants.DB_COLLECTION_TRIGGERED_SKILLS);
        db.getCollection(Constants.DB_COLLECTION_TRIGGERED_SKILLS)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadTriggeredSkill(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listTriggeredSkills for skillId " + skillId + " -> " + lea);
        return lea;
    }

    protected List<TriggeredRecipe> listTriggeredRecipes(String recipeId) throws ParseException 
    {
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.RECIPE_ID, recipeId);
        logger.debug("listTriggeredRecipes filter = [" + myFilter + "]");
        
        List<TriggeredRecipe> lea = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        // docList = readList(Constants.DB_COLLECTION_TRIGGERED_SKILLS);
        db.getCollection(Constants.DB_COLLECTION_TRIGGERED_RECIPES)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadTriggeredRecipe(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listTriggeredRecipes for recipeId " + recipeId + " -> " + lea);
        return lea;
    }

    protected int countSystemAssessmentsAfterDate(String afterDate) throws ParseException 
    {
        Document myFilter = new Document();
        Document myDateFilter = new Document();
        myDateFilter = myDateFilter.append("$gte", afterDate);
        myFilter = myFilter.append(DatabaseConstants.REGISTERED, myDateFilter);
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_TYPE, "subSystems"); // NTH a constant

        logger.debug("system assessments filter = [" + myFilter + "]");
        
        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS)
                .find(myFilter)
                .into(docList);
//                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, 1))

        if (docList != null)
            return docList.size();
        
        return 0;        
    }

    protected int countEquipmentAssessmentsAfterDate(String subSystemId, String afterDate) throws ParseException 
    {
        Document myFilter = new Document();
        Document myDateFilter = new Document();
        myDateFilter = myDateFilter.append("$gte", afterDate);
        myFilter = myFilter.append(DatabaseConstants.REGISTERED, myDateFilter);
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_ID, subSystemId);

        logger.debug("equipment assessments filter = [" + myFilter + "]");
        
        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS)
                .find(myFilter)
                .into(docList);
//                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, 1))

        if (docList != null)
            return docList.size();
        
        return 0;        
    }

    protected int countProcessAssessmentsAfterDate(String subSystemId, String afterDate) throws ParseException 
    {
        List<Recipe> rs = getRecipesBySubSystemId(subSystemId);
        List<String> recipesIds = new LinkedList<>();
        rs.stream().forEach((recipe) -> recipesIds.add(recipe.getUniqueId()));
        logger.debug("countProcessAssessmentsAfterDate -> " + recipesIds);
        
        Document myFilter = new Document();
        Document myDateFilter = new Document();
        myDateFilter = myDateFilter.append("$gte", afterDate);
        myFilter = myFilter.append(DatabaseConstants.REGISTERED, myDateFilter);
        
        Document myRecipesFilter = new Document();
        myRecipesFilter = myRecipesFilter.append("$in", recipesIds);
        myFilter = myFilter.append(DatabaseConstants.RECIPE_ID, myRecipesFilter);

        logger.debug("equipment assessments filter = [" + myFilter + "]");
        
        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_EQUIPMENT_ASSESSMENTS)
                .find(myFilter)
                .into(docList);
//                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, 1))

        if (docList != null)
            return docList.size();
        
        return 0;        
    }

    protected List<ProcessAssessment> listProcessAssessments() throws ParseException 
    {
        List<ProcessAssessment> lea = new LinkedList<>();
        // List<Document> docList = readList(Constants.DB_COLLECTION_PROCESS_ASSESSMENTS);
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_PROCESS_ASSESSMENTS)
                .find()
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadProcessAssessment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listProcessAssessments -> " + lea);
        return lea;
    }
    protected List<ProcessAssessment> listProcessAssessmentsForRecipe(String recipeId) throws ParseException 
    {
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.RECIPE_ID, recipeId);
        logger.debug("listProcessAssessments for recipe filter = [" + myFilter + "]");
        
        List<ProcessAssessment> lea = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_PROCESS_ASSESSMENTS)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)            
        for (Document doc : docList)
            lea.add(loadProcessAssessment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listProcessAssessments for recipe -> " + lea);
        return lea;
    }
    protected List<ProcessAssessment> listProcessAssessments(String recipeId) throws ParseException 
    {
        return listProcessAssessmentsForRecipe(recipeId);
    }
    protected List<ProcessAssessment> listProcessAssessmentsForSkill(String skillId) throws ParseException 
    {
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.SKILL_ID, skillId);
        logger.debug("listProcessAssessments for skill filter = [" + myFilter + "]");
        
        List<ProcessAssessment> lea = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_PROCESS_ASSESSMENTS)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)            
        for (Document doc : docList)
            lea.add(loadProcessAssessment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listProcessAssessments for skill -> " + lea);
        return lea;
    }
    
    private boolean newOrderLine(OrderLine orderLine) {
        if (orderLine == null) return false;
        
        if(!fieldExists(orderLine.getUniqueId(), Constants.DB_COLLECTION_ORDER_LINES)) {
            logger.debug("MONGODBINTERFACE: ORDERLINE.TOBSON = [" + orderLine.toBSON() + "]");            
            db.getCollection(Constants.DB_COLLECTION_ORDER_LINES).insertOne(orderLine.toBSON());
        } 
        return true;
    }

    private boolean removeModule(String moduleId) {
        if (moduleId == null) return false;
        
        if(fieldExists(moduleId, Constants.DB_COLLECTION_MODULES))
        {
            logger.trace("removeModule - module found");
            List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, moduleId), Constants.DB_COLLECTION_MODULES);
            if (docs == null || docs.isEmpty()) return false;
            Document doc = docs.get(0);
            if (doc == null) return false;
            
            logger.trace("removeModule - susbsytem loaded");
            
            logger.trace("removeModule - skills");
if (doc.get(DatabaseConstants.SKILL_IDS) != null)            
((ArrayList<String>) doc.get(DatabaseConstants.SKILL_IDS)).stream().forEach((line) -> {
            removeSkill(line);
});
//            for(Entry<String, Object> entry : ((Document) doc.get("skillIds")).entrySet())
//                removeSkill(entry.getValue().toString());
            
logger.trace("removeModule - internal modules");
if (doc.get(DatabaseConstants.INTERNAL_MODULE_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.INTERNAL_MODULE_IDS)).stream().forEach((line) -> {
            removeModule(line);
});
//            for(Entry<String, Object> entry : ((Document) doc.get("internalModuleIds")).entrySet())
//                removeModule(entry.getValue().toString());

logger.trace("removeModule - physical ports");
if (doc.get(DatabaseConstants.PHYSICAL_PORT_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PHYSICAL_PORT_IDS)).stream().forEach((line) -> {
            removePhysicalPort(line);
});
//            for(Entry<String, Object> entry : ((Document) doc.get("physicalPortIds")).entrySet())
//                removePhysicalPort(entry.getValue().toString());

logger.trace("removeModule - the module!");
            db.getCollection(Constants.DB_COLLECTION_MODULES).deleteOne(eq(DatabaseConstants.UNIQUE_ID, moduleId));
        }
        logger.debug("DB INTERFACE - PROCESS REMOVE MODULE IN PLATFORM: " + moduleId); 
        return true;
    }

    private boolean removePhysicalPort(String physicalPortId) {
        if (physicalPortId == null) return false;
        
        if(!fieldExists(physicalPortId, Constants.DB_COLLECTION_PHYSICAL_PORTS))           
            return false;
        db.getCollection(Constants.DB_COLLECTION_PHYSICAL_PORTS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, physicalPortId));
        return true;        
    }
    
    protected List<Recipe> getRecipes() throws ParseException 
    {
        List<Recipe> r = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_RECIPES);
        if (docList != null)
        for (Document doc : docList)
        {
            logger.debug("getRecipes: current doc = " + doc);
            String uniqueId = doc.getString(DatabaseConstants.UNIQUE_ID);
            logger.debug("getRecipes: uniqueId = " + uniqueId);
            Recipe rr = loadRecipe(uniqueId);
            logger.debug("getRecipes: rr = " + rr);
            r.add(rr);
            logger.debug("getRecipes: r added = ");
        }
        
        logger.debug(getClass().getName() + " - getRecipes -> " + r);
        return r;
    }

    protected List<Recipe> getRecipesBySubSystemId(String subSystemId) throws ParseException 
    {
        if (subSystemId == null) return null;
        
        if(!fieldExists(subSystemId, Constants.DB_COLLECTION_AGENTS))
            return null;

        logger.trace("getRecipesBySubSystemId - subsystem found");
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, subSystemId), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return null;
        
        Document doc = docs.get(0);
        logger.trace("getRecipesBySubSystemId - subsystem loaded");
        if (doc == null) return null;            
        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        logger.debug("loadRecipe - 17");

    List<Recipe> recipes = new LinkedList<>();
        
logger.trace("loadSubSystem - id = " + subSystemId + " - recipes");
if (doc.get(DatabaseConstants.RECIPE_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
            try {
                recipes.add(loadRecipe(line));
            } catch (ParseException ex) {
                logger.error("getRecipesBySubSystemId - recipes: " + ex);
            }
});

        logger.debug(getClass().getName() + " - getRecipesBySubSystemId -> " + recipes);
        return recipes;
    }

    protected boolean putRecipes(Recipe[] recipes) throws ParseException 
    {
        if (recipes != null)
        for (Recipe r : recipes)
        {
            if(fieldExists(r.getUniqueId(), Constants.DB_COLLECTION_RECIPES))           
                updateRecipe(r);
            else
                newRecipe(r);
        }
        
        logger.debug(getClass().getName() + " - putRecipes -> ended");
        return true;
    }

    protected boolean putRecipes_OLDVERSION(Recipe[] recipes) throws ParseException 
    {
        if (recipes != null)
        for (Recipe r : recipes)
        {
            if(fieldExists(r.getUniqueId(), Constants.DB_COLLECTION_RECIPES))           
                removeRecipe(r.getUniqueId());
            newRecipe(r);
        }
        
        logger.debug(getClass().getName() + " - putRecipes -> ended");
        return true;
    }

    protected boolean updateRecipe(Recipe recipe) throws ParseException 
    {
        if (recipe == null)
            return false;
        
        if(!fieldExists(recipe.getUniqueId(), Constants.DB_COLLECTION_RECIPES))           
            return false;
        
        Document recipeDocument;

        // load the old document
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, recipe.getUniqueId()), Constants.DB_COLLECTION_RECIPES);
        if (docs == null || docs.isEmpty()) return false;
        recipeDocument = docs.get(0);
        if (recipeDocument == null) return false;
        
        // remove all kpisettings related
        if (recipeDocument.get(DatabaseConstants.KPI_SETTING_IDS) != null)        
        ((ArrayList<String>) recipeDocument.get(DatabaseConstants.KPI_SETTING_IDS)).stream().forEach((line) -> {
            removeKPISetting(line);
        });
        // insert the new ones
        if (recipe.getKpiSettings() != null)
            recipe.getKpiSettings().forEach((setting) -> newKPISetting(setting));
            
        
        // remove all parameter settings related
        if (recipeDocument.get(DatabaseConstants.PARAMETER_SETTING_IDS) != null)
        ((ArrayList<String>) recipeDocument.get(DatabaseConstants.PARAMETER_SETTING_IDS)).stream().forEach((line) -> {
            removeParameterSetting(line);
        });
        // insert the new ones
        if (recipe.getParameterSettings() != null)
            recipe.getParameterSettings().forEach((setting) -> newParameterSetting(setting));

        
//        // remove all skill requirements related
//        if (recipeDocument.get(DatabaseConstants.SKILL_REQUIREMENT_IDS) != null)
//        ((ArrayList<String>) recipeDocument.get(DatabaseConstants.SKILL_REQUIREMENT_IDS)).stream().forEach((line) -> {
//            removeSkillRequirement(line);
//        });
//        // insert the new ones
//        if (recipe.getSkillRequirements() != null)
//            recipe.getSkillRequirements().forEach((requirement) -> newSkillRequirement(requirement));

        // TBV skill needs to be removed? NO
        // TBV equipment needs to be removed? NO
        

        // update recipe record
        db.getCollection(Constants.DB_COLLECTION_RECIPES).replaceOne(eq(DatabaseConstants.UNIQUE_ID, recipe.getUniqueId()), recipe.toBSON());

        history_newRecipe(recipe);
        
        logger.debug(getClass().getName() + " - updateRecipe -> ended");
        return true;
    }



    public static void main(String[]args)
    {
        MongoDBInterface m = new MongoDBInterface("localhost");
//        m.removeAgentInPlatform("device-server-2 opc-ua server");
SubSystem ss = (SubSystem)SubSystem.fromJSON2("{\"uniqueId\":\"device-server-2 opc-ua server\"\n," +
"\"name\":\"device-server-2 opc-ua server\"\n," +
"\"description\":\"device-server-2 opc-ua server\"\n," +
"\"executionTable\":{\"registered\":1507290049082,\"uniqueId\":\"4c5e1b69-8b54-4fc0-8d19-4ef944b343b3\",\"name\":\"Masmec_InstanceHierarchy/Transport1/ExecutionTable\",\"description\":null,\"rows\":[{\"registered\":1507290049082,\"uniqueId\":\"Line1\",\"productId\":\"AssemblyProduct\",\"recipeId\":\"SC1: Path_A1_Recipe\",\"nextRecipeId\":\"SC1: LeakTestSkill_Recipe\",\"possibleRecipeChoices\":null},{\"registered\":1507290049082,\"uniqueId\":\"Line2\",\"productId\":\"AssemblyProduct\",\"recipeId\":\"SC2: Path_A2_Recipe\",\"nextRecipeId\":\"SC1: LeakTestSkill_Recipe\",\"possibleRecipeChoices\":null}]}\n," +
"\"connected\":false\n," +
"\"skills\":{\"registered\":1507290049117,\"uniqueId\":\"61180444-be4b-47bf-906c-92e485bd7624\",\"name\":\"LinearTransportSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":null,\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null}§{\"registered\":1507290049117,\"uniqueId\":\"efaa60a1-8e80-42b6-a83a-db68d332c973\",\"name\":\"JunctionSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":null,\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null}§{\"registered\":1507290049117,\"uniqueId\":\"79da82f4-95a5-4a8d-a996-08f4e08ead1f\",\"name\":\"Route\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":[{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR1Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR2JunctionSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR3Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null}],\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null}§{\"registered\":1507290049117,\"uniqueId\":\"3d1337ce-1b9e-4956-b085-dfbba0625f00\",\"name\":\"LinearTransportSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":null,\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null}§{\"registered\":1507290049117,\"uniqueId\":\"5abac58b-574c-498a-aa21-6ce605e513c3\",\"name\":\"JunctionSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":null,\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null}§{\"registered\":1507290049117,\"uniqueId\":\"ff8696a5-de91-4370-873e-46d44739e102\",\"name\":\"Route\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":[{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR1Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR2JunctionSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR3Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null}],\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null}§\n," +
"\"physicalPorts\": \n," +
"\"recipes\":{\"registered\":1507290049112,\"uniqueId\":\"3ca244e0-5a20-4fb3-9c7d-346af99dea46\",\"name\":\"SC1: Path_A1_Recipe\",\"description\":\"0\",\"valid\":true,\"parameterSettings\":null,\"skillRequirements\":[{\"registered\":1507290049113,\"uniqueId\":\"0\",\"name\":\"0\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049113,\"uniqueId\":\"0\",\"name\":\"0\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049115,\"uniqueId\":\"0\",\"name\":\"0\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null}],\"kpiSettings\":[{\"registered\":1507290049113,\"uniqueId\":null,\"name\":\"processTime\",\"description\":null,\"kpi\":null,\"type\":null,\"unit\":null,\"value\":\"0.1\"},{\"registered\":1507290049113,\"uniqueId\":null,\"name\":\"meanProcessEnergyConsumption\",\"description\":null,\"kpi\":null,\"type\":null,\"unit\":null,\"value\":\"240\"}],\"skill\":{\"registered\":1507290049117,\"uniqueId\":\"79da82f4-95a5-4a8d-a996-08f4e08ead1f\",\"name\":\"Route\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":[{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR1Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR2JunctionSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR3Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null}],\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null},\"executedBySkillControlPort\":null,\"uniqueAgentName\":null,\"equipmentId\":null,\"optimized\":false,\"lastOptimizationTime\":null,\"msbProtocolEndpoint\":\"2:InvokeSkill/SC1: Path_A1_Recipe\"}»{\"registered\":1507290049115,\"uniqueId\":\"05ba3ec5-1318-4b7f-afef-7149b1f7944a\",\"name\":\"SC2: Path_A2_Recipe\",\"description\":\"0\",\"valid\":true,\"parameterSettings\":null,\"skillRequirements\":[{\"registered\":1507290049115,\"uniqueId\":\"0\",\"name\":\"0\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049115,\"uniqueId\":\"0\",\"name\":\"0\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049115,\"uniqueId\":\"0\",\"name\":\"0\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null}],\"kpiSettings\":[{\"registered\":1507290049115,\"uniqueId\":null,\"name\":\"processTime\",\"description\":null,\"kpi\":null,\"type\":null,\"unit\":null,\"value\":\"0.1\"},{\"registered\":1507290049115,\"uniqueId\":null,\"name\":\"meanProcessEnergyConsumption\",\"description\":null,\"kpi\":null,\"type\":null,\"unit\":null,\"value\":\"240\"}],\"skill\":{\"registered\":1507290049117,\"uniqueId\":\"79da82f4-95a5-4a8d-a996-08f4e08ead1f\",\"name\":\"Route\",\"description\":null,\"type\":null,\"skillType\":null,\"kpis\":null,\"informationPorts\":null,\"parameters\":null,\"parameterPorts\":null,\"classificationType\":0,\"skillRequirements\":[{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR1Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR2JunctionSkill\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null},{\"registered\":1507290049117,\"uniqueId\":null,\"name\":\"SR3Linear_Transport\",\"description\":null,\"type\":null,\"skillType\":null,\"precedents\":null,\"requiresPart\":null}],\"recipeIds\":null,\"controlPorts\":null,\"subSystemId\":null,\"label\":null},\"executedBySkillControlPort\":null,\"uniqueAgentName\":null,\"equipmentId\":null,\"optimized\":false,\"lastOptimizationTime\":null,\"msbProtocolEndpoint\":\"2:InvokeSkill/SC2: Path_A2_Recipe\"}»\n," +
"\"address\":\n," +
"\"status\":\n," +
"\"manufacturer\":\n," +
"\"physicalLocation\":\n," +
"\"logicalLocation\":\n," +
"\"type\":\"resource_df_service\"\n," +
"\"registed\":1507290049082\n" +
"}", SubSystem.class);

        m.newAgentInPlatform(ss);
    };

    protected List<SubSystem> getSubSystems() throws ParseException 
    {
        List<SubSystem> ls = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_AGENTS);
        if (docList != null)
        for (Document doc : docList)
        {
            logger.debug("getSubSystems: current doc = " + doc);
            String uniqueId = doc.getString(DatabaseConstants.UNIQUE_ID);
            logger.debug("getSubSystems: uniqueId = " + uniqueId);
            SubSystem s = loadSubSystem(uniqueId);
            logger.debug("getSubSystems: s = " + s);
            ls.add(s);
            logger.debug("getSubSystems: s added ");
        }
        
        logger.debug(getClass().getName() + " - getSubSystems -> " + ls);
        return ls;
    }

    protected SubSystem loadSubSystem(String subSystemId) throws ParseException {
        if (subSystemId == null) return null;
        
        if(!fieldExists(subSystemId, Constants.DB_COLLECTION_AGENTS))
            return null;

        logger.trace("loadSubSystem - subsystem found");
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, subSystemId), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return null;
        
        Document doc = docs.get(0);
        logger.trace("loadSubSystem - subsystem loaded");
        if (doc == null) return null;            
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        logger.debug("loadRecipe - 17");
        
        logger.trace("loadSubSystem - execution table");
        ExecutionTable et = null;
            String executionTableId = doc.getString(DatabaseConstants.EXECUTION_TABLE_ID);            
            if (executionTableId != null)
                try {
                    et = loadExecutionTable(executionTableId);
        } catch (ParseException ex) {
                logger.error("loadSubSystem - execution table: " + ex);
        }
                                        
        List<Recipe> recipes = new LinkedList<>();
        List<Skill> skills = new LinkedList<>();
        List<Module> modules = new LinkedList<>();
        List<PhysicalPort> physicalPorts = new LinkedList<>();
        List<PhysicalAdjustmentParameter> physicalAdjustmentParameters = new LinkedList<>();

logger.trace("loadSubSystem - id = " + subSystemId + " - recipes");
if (doc.get(DatabaseConstants.RECIPE_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.RECIPE_IDS)).stream().forEach((line) -> {
            try {
                recipes.add(loadRecipe(line));
            } catch (ParseException ex) {
                logger.error("loadSubSystem - recipes: " + ex);
            }
});

logger.trace("loadSubSystem - id = " + subSystemId + " - skills");
if (doc.get(DatabaseConstants.SKILL_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.SKILL_IDS)).stream().forEach((line) -> {
            try {
                skills.add(loadSkill(line));
            } catch (ParseException ex) {
                logger.error("loadSubSystem - skills: " + ex);
            }
});

logger.trace("loadSubSystem - id = " + subSystemId + " - modules");
if (doc.get(DatabaseConstants.INTERNAL_MODULE_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.INTERNAL_MODULE_IDS)).stream().forEach((line) -> {
            try {
                modules.add(loadModule(line));
            } catch (ParseException ex) {
                logger.error("loadSubSystem - modules: " + ex);
            }
});

logger.trace("loadSubSystem - id = " + subSystemId + " - physical ports");
if (doc.get(DatabaseConstants.PHYSICAL_PORT_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.PHYSICAL_PORT_IDS)).stream().forEach((line) -> {
            try {
                physicalPorts.add(loadPhysicalPort(line));
            } catch (ParseException ex) {
                logger.error("loadSubSystem - physical ports: " + ex);
            }
});

logger.trace("loadSubSystem - id = " + subSystemId + " - physical adjustment parameters");
if (doc.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS) != null)        
((ArrayList<String>) doc.get(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS)).stream().forEach((line) -> {
            try {
                physicalAdjustmentParameters.add(loadPhysicalAdjustmentParameter(line));
            } catch (ParseException ex) {
                logger.error("loadSubSystem - physical ports: " + ex);
            }
});

        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
logger.debug("loadSubSystem - registered = " + registered);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
logger.debug("loadSubSystem - reg = " + reg);
                        
    SubSystem s = new SubSystem(doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.NAME), 
            doc.getString(DatabaseConstants.DESCRIPTION), 
            et, 
            doc.getBoolean(DatabaseConstants.CONNECTED),  
            skills, 
            physicalPorts, 
            physicalAdjustmentParameters,
            recipes, 
            modules, 
            doc.getString(DatabaseConstants.ADDRESS), 
            doc.getString(DatabaseConstants.STATUS),  
            doc.getString(DatabaseConstants.MANUFACTURER),  
            null, 
            null, 
            doc.getString(DatabaseConstants.TYPE), 
            reg);
        
logger.trace("loadSubSystem - id = " + subSystemId + " - subSystem is : " + s);
        return s;        
    }

    protected ExecutionTable loadExecutionTableBySubSystemId(String subSystemId) throws ParseException {
        if (subSystemId == null) return null;
        
        if(!fieldExists(subSystemId, Constants.DB_COLLECTION_AGENTS))
            return null;

        logger.trace("loadSubSystem - subsystem found");
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, subSystemId), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return null;
        
        Document doc = docs.get(0);
        logger.trace("loadSubSystem - subsystem loaded");
        if (doc == null) return null;            
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        logger.debug("loadRecipe - 17");
        
        logger.trace("loadSubSystem - execution table");
        ExecutionTable et = null;
            String executionTableId = doc.getString(DatabaseConstants.EXECUTION_TABLE_ID);            
            if (executionTableId != null)
                try {
                    et = loadExecutionTable(executionTableId);
        } catch (ParseException ex) {
                logger.error("loadSubSystem - execution table: " + ex);
        }
                                        
        return et;
    }

    protected List<ExecutionTable> getExecutionTables() throws ParseException 
    {
        List<ExecutionTable> ls = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_EXECUTION_TABLES);
        if (docList != null)
        for (Document doc : docList)
        {
            logger.debug("getExecutionTables: current doc = " + doc);
            String uniqueId = doc.getString(DatabaseConstants.UNIQUE_ID);
            logger.debug("getExecutionTables: uniqueId = " + uniqueId);
            ExecutionTable et = loadExecutionTable(uniqueId);
            logger.debug("getExecutionTables: et = " + et);
            ls.add(et);
            logger.debug("getExecutionTables: et added ");
        }
        
        logger.debug(getClass().getName() + " - getExecutionTables -> " + ls);
        return ls;
    }

    protected boolean updateWorkstationRecipes(SubSystemRecipes subSystemRecipes) {
        if (subSystemRecipes == null) return false;
        
        if(!fieldExists(subSystemRecipes.getUniqueId(), Constants.DB_COLLECTION_AGENTS))           
            return false;
        
        Document oldCpadDocument;

        // load the old document
//        oldCpadDocument = readData(eq("id", newCpad.getEquipmentId()), Constants.DB_COLLECTION_AGENTS).get(0);
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, subSystemRecipes.getUniqueId()), Constants.DB_COLLECTION_AGENTS);
        if (docs == null || docs.isEmpty()) return false;
        oldCpadDocument = docs.get(0);
        if (oldCpadDocument == null) return false;

List<String> newRows = new LinkedList<>();
for (Recipe r : subSystemRecipes.getRecipes())
                newRows.add(r.getUniqueId());

        oldCpadDocument.replace(DatabaseConstants.RECIPE_IDS, newRows);

        db.getCollection(Constants.DB_COLLECTION_AGENTS).replaceOne(eq(DatabaseConstants.UNIQUE_ID, subSystemRecipes.getUniqueId()), oldCpadDocument); 

        logger.debug("DB INTERFACE - WORKSTATION RECIPES UPDATE: " + subSystemRecipes.toString()); 
        logger.debug("DB INTERFACE - WORKSTATION RECIPES UPDATE: " + oldCpadDocument); 
        
        return true;
    }

//    boolean newProductDefinition(Product product) {
//        if(!fieldExists(product.getUniqueId(), Constants.DB_COLLECTION_PRODUCTS)) 
//            db.getCollection(Constants.DB_COLLECTION_PRODUCTS).insertOne(product.toBSON());
//
//        if (product.getParts() != null)
//        for (Part part : product.getParts())
//            newPart(part);
//        
//        if (product.getSkillRequirements()!= null)
//        for (SkillRequirement skillRequirement : product.getSkillRequirements())
//            newSkillRequirement(skillRequirement);
//        
//        return true;
//    }

    protected List<RecipeExecutionData> getRecipeExecutionData() throws ParseException
    {
        List<RecipeExecutionData> r = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA);
        if (docList != null)
        for (Document doc : docList)
        {
            logger.debug("getRecipeExecutionData: current doc = " + doc);
            RecipeExecutionData red = loadRecipeExecutionData(doc);
            logger.debug("getRecipeExecutionData: red = " + red);
            r.add(red);
            logger.debug("getRecipeExecutionData: r added = ");
        }
        
        logger.debug(getClass().getName() + " - getRecipeExecutionData -> " + r);
        return r;
    }

    protected List<RecipeExecutionData> getRecipeExecutionData(RecipeExecutionDataFilter recipeExecutionDataFilter) throws ParseException
    {
        List<RecipeExecutionData> r = new LinkedList<>();
        
        // TODO use the filter!    
        Document myFilter = new Document();
        if (recipeExecutionDataFilter != null)
        {
            // date filter
            Document myDateFilter = new Document();
            boolean bDateFilter = false;
            if (recipeExecutionDataFilter.getStartInterval() != null)
            {
                myDateFilter = myDateFilter.append("$gte", recipeExecutionDataFilter.getStartInterval());
                bDateFilter = true;
            }
            if (recipeExecutionDataFilter.getStopInterval() != null)
            {
                myDateFilter = myDateFilter.append("$lte", recipeExecutionDataFilter.getStopInterval());
                bDateFilter = true;
            }
            if (bDateFilter)
//                myDateFilter = myFilter.append(DatabaseConstants.REGISTERED, myDateFilter);
                myFilter = myFilter.append(DatabaseConstants.REGISTERED, myDateFilter);

            if (recipeExecutionDataFilter.getRecipeId() != null)
//                myDateFilter = myFilter.append(DatabaseConstants.RECIPE_ID, recipeExecutionDataFilter.getRecipeId());
                myFilter = myFilter.append(DatabaseConstants.RECIPE_ID, recipeExecutionDataFilter.getRecipeId());
            if (recipeExecutionDataFilter.getProductInstanceId() != null)
            {
                myFilter = myFilter.append(DatabaseConstants.PRODUCT_INSTANCE_ID, recipeExecutionDataFilter.getProductInstanceId());
            }
            else
            {
                if (recipeExecutionDataFilter.isAlsoTriggerData() == false)
                {
                    Document myProductInstanceFilter = new Document();
                    myProductInstanceFilter = myProductInstanceFilter.append("$regex", "^((?!trg_).)*$");
                    myFilter = myFilter.append(DatabaseConstants.PRODUCT_INSTANCE_ID, myProductInstanceFilter);
/*                    
                    Document myProductInstanceFilter = new Document();
                    myProductInstanceFilter = myProductInstanceFilter.append("$regex", "^((?!trg_rcp_).)*$");
                    myProductInstanceFilter = myProductInstanceFilter.append("$regex", "^((?!trg_skill_).)*$");
                    logger.debug("getRecipeExecutionData: filter 2 = [" + myProductInstanceFilter + "]");
                    
                    Document myProductInstance2Filter = new Document();
                    myProductInstance2Filter.append("$and", myProductInstanceFilter);
                    logger.debug("getRecipeExecutionData: filter 3 = [" + myProductInstance2Filter + "]");
                    
                    Bson myNewRcpFilter = com.mongodb.client.model.Filters.regex(DatabaseConstants.PRODUCT_INSTANCE_ID, "^((?!trg_rcp_).)*$");                    
                    Bson myNewTrgFilter = com.mongodb.client.model.Filters.regex(DatabaseConstants.PRODUCT_INSTANCE_ID, "^((?!trg_skill_).)*$");
                    Bson myNewFilter = com.mongodb.client.model.Filters.and(myNewRcpFilter, myNewTrgFilter);       
                    logger.debug("getRecipeExecutionData: filter 4 = [" + myNewFilter + "]");
                    
                    // myFilter = myFilter.append(DatabaseConstants.PRODUCT_INSTANCE_ID, myNewFilter);
                    myFilter.append(DatabaseConstants.PRODUCT_INSTANCE_ID, myNewRcpFilter);
                    myFilter.append(DatabaseConstants.PRODUCT_INSTANCE_ID, myNewTrgFilter);
*/
                }
            }
        }

        logger.debug("getRecipeExecutionData: filter 1 = [" + myFilter + "]");
//        this.productInstanceId = super.generateRandomId('trg_rcp_' + this.recipe.uniqueId + "_trg_prodinst_" + this.getFormattedDate(new Date()) + "_");
//        productInstanceId = super.generateRandomId('trg_skill_' + this.selectedSkill.uniqueId + "_trg_prodinst_");
// db.collection.find({name:{'$regex' : '^string', '$options' : 'i'}})
// db.test.find({c: {$not: /ttt/}}

//        List<Document> docList = readList(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA);
        List<Document> docList = new LinkedList<>();
                
/*FindIterable<Document> iterable7 = db.getCollection("1dag").find(
            new Document().append("timestamp", new Document()
                            .append("$gte", startTime)
                            .append("$lte", endTime))
                    .append("id",10));
*/

        db.getCollection(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, 1))
                .into(docList);

        if (docList != null)
        for (Document doc : docList)
        {
            logger.debug("getRecipeExecutionData: current doc = " + doc);
            RecipeExecutionData red = loadRecipeExecutionData(doc);
            logger.debug("getRecipeExecutionData: red = " + red);
            
            if (recipeExecutionDataFilter == null ||
                    recipeExecutionDataFilter.getKpiSettingName() == null)
            {
                    r.add(red);
                    logger.debug("getRecipeExecutionData: r added = ");
            }
            else
            {
                boolean gotKPISet = false;
                
                List<KPISetting> kpisettings = red.getKpiSettings();
                for (KPISetting k : kpisettings)
                    if (k.getName().equalsIgnoreCase(recipeExecutionDataFilter.getKpiSettingName()))
                    {
                        gotKPISet = true;
                        break;
                    }
                
                if (gotKPISet)
                {
                    r.add(red);
                    logger.debug("kpisettings match: getRecipeExecutionData: r added = ");
                }
            }
                        
        }
        
        logger.debug(getClass().getName() + " - getRecipeExecutionData -> " + r);
        return r;
    }

    protected RecipeExecutionData loadRecipeExecutionData(Document doc) throws ParseException {
        if (doc == null) return null;
        
        logger.debug("loadRecipeExecutionData - 1");
        if (doc == null) return null;
        logger.debug("loadRecipeExecutionData - 2");

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        logger.debug("loadRecipeExecutionData - 17");
        
        List<KPISetting> kpiSettings = new LinkedList<>();
        List<ParameterSetting> parameterSettings = new LinkedList<>();

        String REGregistered = doc.getString(DatabaseConstants.REGISTERED);
        
        logger.trace("loadRecipeExecutionData - kpisettings");
        if (doc.get(DatabaseConstants.KPI_SETTING_IDS) != null)        
        ((ArrayList<String>) doc.get(DatabaseConstants.KPI_SETTING_IDS)).stream().forEach((line) -> {
                    try {
                        kpiSettings.add(loadREDKPISetting(line, REGregistered));
                    } catch (ParseException ex) {
                        logger.error("loadRecipeExecutionData - REDkpiSettings: " + ex);
                    }
        });

        logger.trace("loadRecipeExecutionData - parametersettings");
        if (doc.get(DatabaseConstants.PARAMETER_SETTING_IDS) != null)
        ((ArrayList<String>) doc.get(DatabaseConstants.PARAMETER_SETTING_IDS)).stream().forEach((line) -> {
                    try {
                        parameterSettings.add(loadREDParameterSetting(line, REGregistered));
                    } catch (ParseException ex) {
                        logger.error("loadRecipeExecutionData - REDparameterSettings: " + ex);
                    }
        });
         
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        logger.debug("loadRecipeExecutionData - registered = " + registered);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        logger.debug("loadRecipeExecutionData - reg = " + reg);
        
        logger.trace("loadRecipeExecutionData - recipeExecutionData");
        RecipeExecutionData red = new RecipeExecutionData(
            doc.getString(DatabaseConstants.PRODUCT_INSTANCE_ID),
            doc.getString(DatabaseConstants.RECIPE_ID),
            kpiSettings,
            parameterSettings,
            reg        
        );

        logger.trace("loadRecipeExecutionData - recipeExecutionData is : " + red);
        return red;
    }
    
    private boolean history_newRecipe(Recipe recipe) {
        if (recipe == null) return false;
        
        logger.debug("DB INTERFACE - PROCESS NEW HISTORY RECIPE: BEGIN: " + recipe.toString()); 

        int lastVersion = getLastRecipeVersion(recipe.getUniqueId());
        int currentVersion = lastVersion + 1;
        
        Document recipeDoc = recipe.toBSON();
        recipeDoc.append(DatabaseConstants.VERSION, currentVersion);
        
        db.getCollection(Constants.HISTORY_DB_COLLECTION_RECIPES).insertOne(recipeDoc);
            
            if (recipe.getKpiSettings() != null)
                recipe.getKpiSettings().forEach((setting) -> history_newKPISetting(setting, currentVersion));
            
            if (recipe.getParameterSettings() != null)
                recipe.getParameterSettings().forEach((setting) -> history_newParameterSetting(setting, currentVersion));
            
//            if (recipe.getSkillRequirements() != null)
//                recipe.getSkillRequirements().forEach((requirement) -> history_newSkillRequirement(requirement));

//            //
//            if (recipe.getSkill() != null)
//                newSkill(recipe.getSkill());
////            else
////                throw new RuntimeException("Recipe skill cannot be null!");
            
//            if (recipe.getExecutedBySkillControlPort() != null)
//                history_newControlPort(recipe.getExecutedBySkillControlPort());

        logger.debug("DB INTERFACE - PROCESS NEW HISTORY RECIPE: END: " + recipe.toString()); 

        return true;
    }  

    public int getLastRecipeVersion(String recipeId) {
        if (recipeId == null) return -1;
        
        List<Document> entries = new LinkedList<>();
        int lastVersion = -1;
        db.getCollection(Constants.HISTORY_DB_COLLECTION_RECIPES)
                .find(eq(DatabaseConstants.UNIQUE_ID, recipeId))
                .sort(new BasicDBObject(DatabaseConstants.VERSION, -1))
                .limit(1)
                .into(entries);
        if (entries == null)
            return -1;
        if (entries.isEmpty())
            return -1;
        Document doc = entries.get(0);
        if (doc == null)
            return -1;
        
        lastVersion = ((Integer) doc.get(DatabaseConstants.VERSION)).intValue();
        return lastVersion;
    }

    protected boolean history_newKPISetting(KPISetting setting, int version) {
        if (setting == null) return false;
        
        Document doc = setting.toBSON();
        doc.append(DatabaseConstants.VERSION, version);        
        db.getCollection(Constants.HISTORY_DB_COLLECTION_KPI_SETTINGS).insertOne(doc);        
        return true;
    }    
    protected boolean history_newParameterSetting(ParameterSetting setting, int version) {
        if (setting == null) return false;
        
        Document doc = setting.toBSON();
        doc.append(DatabaseConstants.VERSION, version);        
        db.getCollection(Constants.HISTORY_DB_COLLECTION_PARAMETER_SETTINGS).insertOne(doc);
        return true;
    } 

    protected OrderInstance loadOrderInstance(String orderInstanceId) throws ParseException  {
        if (orderInstanceId == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION); 
        
        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, orderInstanceId), Constants.DB_COLLECTION_ORDER_INSTANCES);
        if (docs == null || docs.isEmpty()) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        List<ProductInstance> productInstances = new LinkedList<>();
        
if (doc.get(DatabaseConstants.PRODUCT_INSTANCE_IDS) != null)
((ArrayList<String>) doc.get(DatabaseConstants.PRODUCT_INSTANCE_IDS)).stream().forEach((line) -> {
    try{
            productInstances.add(loadProductInstance(line));
    }
    catch (ParseException pe)
    {
        logger.error(pe.getLocalizedMessage());
    }
        });
        
        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new OrderInstance(
                doc.getString(DatabaseConstants.UNIQUE_ID),
                doc.getString(DatabaseConstants.NAME),
                doc.getString(DatabaseConstants.DESCRIPTION),
                doc.getInteger(DatabaseConstants.PRIORITY),
                productInstances, 
                reg);
    }

    protected List<ProductInstance> getProductInstancesPerRecipe(String recipeId) throws ParseException {
        List<ProductInstance> lpi = new LinkedList<>();
        
        List<String>productInstanceIds = new LinkedList<>();

        db.getCollection(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA)
                .distinct(DatabaseConstants.PRODUCT_INSTANCE_ID, eq(DatabaseConstants.RECIPE_ID, recipeId), String.class)
                .into(productInstanceIds);

        if (productInstanceIds != null)
        for (String productInstanceId : productInstanceIds)
        {
            logger.debug("getRecipeExecutionData: current product instance = " + productInstanceId);
            ProductInstance pi = loadProductInstance(productInstanceId);
            logger.debug("getRecipeExecutionData: productInstance = " + pi);    
            lpi.add(pi);
        }
        
        logger.debug(getClass().getName() + " - getProductInstancesPerRecipe -> " + lpi);
        return lpi;
    }

    protected List<Recipe> getRecipesPerProductInstance(String productInstanceId) throws ParseException {
        List<Recipe> lr = new LinkedList<>();
        
        List<String>recipeIds = new LinkedList<>();

        db.getCollection(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA)
                .distinct(DatabaseConstants.RECIPE_ID, eq(DatabaseConstants.PRODUCT_INSTANCE_ID, productInstanceId), String.class)
                .into(recipeIds);

        if (recipeIds != null)
        for (String recipeId : recipeIds)
        {
            logger.debug("getRecipeExecutionData: current recipe = " + recipeId);
            if (recipeId != null)
            {
                Recipe r = loadRecipe(recipeId);
                logger.debug("getRecipeExecutionData: recipe = " + r);  
                if (r != null)
                    lr.add(r);
            }
        }
        
        logger.debug(getClass().getName() + " - getRecipesPerProductInstance -> " + lr);
        return lr;
    }

    List<String> getKPISettingNamesPerRecipeExecutionData(RecipeExecutionDataFilter recipeExecutionDataFilter) 
            throws ParseException
    {
        List<String> kpiNames = new LinkedList<>();
        
        // TODO use the filter!    
        Document myFilter = new Document();
        if (recipeExecutionDataFilter != null)
        {
            // date filter
            Document myDateFilter = new Document();
            boolean bDateFilter = false;
            if (recipeExecutionDataFilter.getStartInterval() != null)
            {
                myDateFilter = myDateFilter.append("$gte", recipeExecutionDataFilter.getStartInterval());
                bDateFilter = true;
            }
            if (recipeExecutionDataFilter.getStopInterval() != null)
            {
                myDateFilter = myDateFilter.append("$lte", recipeExecutionDataFilter.getStopInterval());
                bDateFilter = true;
            }
            if (bDateFilter)
//                myDateFilter = myFilter.append(DatabaseConstants.REGISTERED, myDateFilter);
                myFilter = myFilter.append(DatabaseConstants.REGISTERED, myDateFilter);

            if (recipeExecutionDataFilter.getRecipeId() != null)
                myFilter = myFilter.append(DatabaseConstants.RECIPE_ID, recipeExecutionDataFilter.getRecipeId());
            if (recipeExecutionDataFilter.getProductInstanceId() != null)
                myFilter = myFilter.append(DatabaseConstants.PRODUCT_INSTANCE_ID, recipeExecutionDataFilter.getProductInstanceId());
        }
        logger.debug("getRecipeExecutionData: myfilter = [" + myFilter + "]");
        
//        List<Document> docList = readList(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA);
        List<Document> docList = new LinkedList<>();
                
/*FindIterable<Document> iterable7 = db.getCollection("1dag").find(
            new Document().append("timestamp", new Document()
                            .append("$gte", startTime)
                            .append("$lte", endTime))
                    .append("id",10));
*/

        db.getCollection(Constants.DB_COLLECTION_RECIPE_EXECUTION_DATA)
                .find(myFilter)
                .into(docList);


        if (docList != null)
        for (Document doc : docList)
        {
            logger.debug("getRecipeExecutionData: current doc = " + doc);
            RecipeExecutionData red = loadRecipeExecutionData(doc);
            logger.debug("getRecipeExecutionData: red = " + red);
            
            for (KPISetting kpiset : red.getKpiSettings())
            {
                if (!kpiNames.contains(kpiset.getName()))
                    kpiNames.add(kpiset.getName());
            }                        
        }
        
        logger.debug(getClass().getName() + " - getRecipeExecutionData -> " + kpiNames);
        return kpiNames;
    }

    protected List<OrderInstance> listOrderInstances() throws ParseException
    {
        List<OrderInstance> leo = new LinkedList<>();
        
        // List<Document> docList = readList(Constants.DB_COLLECTION_ORDER_INSTANCES);
        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_ORDER_INSTANCES)
                .find()
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
//                .sort(new BasicDBObject(DatabaseConstants.UNIQUE_ID, -1))
        if (docList == null)
            return null;
        if (docList.isEmpty())
            return null;
        
        if (docList != null)
        for (Document doc : docList)
            leo.add(loadOrderInstance(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listOrderInstances -> " + leo);
        return leo;
    }

    protected List<ProductInstance> listProductInstancesNotYetProduced() throws ParseException
    {
        List<ProductInstance> lpi = new LinkedList<>();
        
        Document myFilter = new Document();
        Document myNotFilter = new Document();
        myNotFilter = myNotFilter.append("$ne", "PRODUCED");
        myFilter = myFilter.append(DatabaseConstants.STATUS, myNotFilter);

        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_PRODUCT_DESCRIPTIONS)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.UNIQUE_ID, -1))
                .into(docList);
        if (docList == null)
            return null;
        if (docList.isEmpty())
            return null;
        
        if (docList != null)
        for (Document doc : docList)
            lpi.add(loadProductInstance(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listProductInstancesNotYetProduced -> " + lpi);
        return lpi;
    }

    private boolean removePhysicalAdjustmentParameter(String physicalAdjustmentParameterId) {
        if (physicalAdjustmentParameterId == null) return false;
        
        if(fieldExists(physicalAdjustmentParameterId, Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETERS)) 
            db.getCollection(Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENT_PARAMETERS).deleteOne(eq(DatabaseConstants.UNIQUE_ID, physicalAdjustmentParameterId));
        return true;
    }

    protected boolean newSystemStageChange(SystemChangeStage systemChangeStage) {
        if (systemChangeStage == null) return false;
        
        logger.debug("MONGODBINTERFACE: SYSTEMCHANGESTAGE.TOBSON = [" + systemChangeStage.toBSON() + "]");            
        db.getCollection(Constants.DB_COLLECTION_SYSTEM_STAGE_CHANGES).insertOne(systemChangeStage.toBSON());

        return true;
    }

    /**
     * Get the most recent system status change.
     * 
     * @return the corresponding SystemChangeStage object
     * @throws ParseException 
     */
    protected SystemChangeStage getLastSystemStageChange() throws ParseException 
    {
        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_SYSTEM_STAGE_CHANGES)
                .find()
                .sort(new BasicDBObject(DatabaseConstants.UNIQUE_ID, -1))
                .into(docList);
//        if (docList == null)
//            return null;
        if (docList.isEmpty())
            return null;
        
//        if (docList != null)
            return loadSystemChangeStage(docList.get(0).getString(DatabaseConstants.UNIQUE_ID));

//        return null;
    }

    protected List<SystemChangeStage> listSystemStageChanges() throws ParseException 
    {
        List<SystemChangeStage> lea = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_SYSTEM_STAGE_CHANGES);
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadSystemChangeStage(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listSystemStageChanges -> " + lea);
        return lea;
    }

    protected SystemChangeStage loadSystemChangeStage(String systemChangeStateId) throws ParseException {
        if (systemChangeStateId == null) return null;
        
        if(!fieldExists(systemChangeStateId, Constants.DB_COLLECTION_SYSTEM_STAGE_CHANGES))           
            return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, systemChangeStateId), Constants.DB_COLLECTION_SYSTEM_STAGE_CHANGES);
        if (docs == null || docs.size() == 0) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new SystemChangeStage(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.SYSTEM_STAGE),
            reg
        );                
    }

    boolean newPhysicalAdjustment(PhysicalAdjustment physicalAdjustment) {
        if (physicalAdjustment == null) return false;
        
        if(!fieldExists(physicalAdjustment.getUniqueId(), Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENTS))
            db.getCollection(Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENTS).insertOne(physicalAdjustment.toBSON());

        newPhysicalAdjustmentParameterSetting(physicalAdjustment.getPhysicalAdjustmentParameterSetting());
        
        logger.debug("DB INTERFACE - PROCESS NEW PHYSICAL ADJUSTMENT: " + physicalAdjustment.toString()); 
        return true;
    }

    protected List<PhysicalAdjustment> listPhysicalAdjustments() throws ParseException {
        List<PhysicalAdjustment> lea = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENTS);
        if (docList != null)
        for (Document doc : docList)
            lea.add(loadPhysicalAdjustment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listPhysicalAdjustments -> " + lea);
        return lea;
    }
    protected List<PhysicalAdjustment> listPhysicalAdjustments(String equipmentId) throws ParseException {
/*        
        List<PhysicalAdjustment> lea = new LinkedList<>();
        List<Document> docList = readList(Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENTS);
        if (docList != null)
*/
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_TYPE, "subSystem"); // TODO maybe have a constant
        myFilter = myFilter.append(DatabaseConstants.EQUIPMENT_ID, equipmentId);
        logger.debug("listPhysicalAdjustments for equipment filter = [" + myFilter + "]");
        
        List<PhysicalAdjustment> lea = new LinkedList<>();
        List<Document> docList = new LinkedList<>();
        
        db.getCollection(Constants.DB_COLLECTION_PHYSICAL_ADJUSTMENTS)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.REGISTERED, -1))
                .into(docList);
        if (docList != null)            
        for (Document doc : docList)
            lea.add(loadPhysicalAdjustment(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listPhysicalAdjustments for equipment -> " + lea);
        return lea;
    }

    boolean newSubSystemStageChange(SubSystemChangeStage subSystemChangeStage) {
        if (subSystemChangeStage == null) return false;
        
        logger.debug("MONGODBINTERFACE: SUBSYSTEMCHANGESTAGE.TOBSON = [" + subSystemChangeStage.toBSON() + "]");            
        db.getCollection(Constants.DB_COLLECTION_SUBSYSTEM_STAGE_CHANGES).insertOne(subSystemChangeStage.toBSON());

        return true;
    }

    /**
     * Get the most recent status change of a given subsystem.
     * 
     * @param subSystemId the given subsystem unique identifier
     * @return the corresponding SystemChangeStage object
     * @throws ParseException 
     */
    protected SubSystemChangeStage getLastSubSystemStageChange(String subSystemId) throws ParseException 
    {
        if (subSystemId == null)
            return null;
        
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.SUBSYSTEM_ID, subSystemId);
        logger.debug("getLastSubSystemStageChange: filter = [" + myFilter + "]");
        
        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_SUBSYSTEM_STAGE_CHANGES)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.UNIQUE_ID, -1))
                .into(docList);
        if (docList.isEmpty())
            return null;
        
        return loadSubSystemChangeStage(docList.get(0).getString(DatabaseConstants.UNIQUE_ID));
    }

    protected List<SubSystemChangeStage> listSubSystemStageChanges(String subSystemId) throws ParseException 
    {
        if (subSystemId == null)
            return null;
        
        Document myFilter = new Document();
        myFilter = myFilter.append(DatabaseConstants.SUBSYSTEM_ID, subSystemId);
        logger.debug("listSubSystemStageChanges: filter = [" + myFilter + "]");
        
        List<SubSystemChangeStage> lea = new LinkedList<>();

//        List<Document> docList = readList(Constants.DB_COLLECTION_SUBSYSTEM_STAGE_CHANGES);
        List<Document> docList = new LinkedList<>();
        db.getCollection(Constants.DB_COLLECTION_SUBSYSTEM_STAGE_CHANGES)
                .find(myFilter)
                .sort(new BasicDBObject(DatabaseConstants.UNIQUE_ID, 1))
                .into(docList);        

        if (docList != null)
        for (Document doc : docList)
            lea.add(loadSubSystemChangeStage(doc.getString(DatabaseConstants.UNIQUE_ID)));
        
        logger.debug(getClass().getName() + " - listSubSystemStageChanges -> " + lea);
        return lea;
    }

    protected SubSystemChangeStage loadSubSystemChangeStage(String subSystemChangeStateId) throws ParseException {
        if (subSystemChangeStateId == null) return null;
        
        if(!fieldExists(subSystemChangeStateId, Constants.DB_COLLECTION_SUBSYSTEM_STAGE_CHANGES))           
            return null;

        List<Document> docs = readData(eq(DatabaseConstants.UNIQUE_ID, subSystemChangeStateId), Constants.DB_COLLECTION_SUBSYSTEM_STAGE_CHANGES);
        if (docs == null || docs.size() == 0) return null;
        Document doc = docs.get(0);
        if (doc == null) return null;
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

        Date reg = null;
        String registered = doc.getString(DatabaseConstants.REGISTERED);
        if (registered != null && !registered.equalsIgnoreCase("null"))
            reg = sdf.parse(registered);
        
        return new SubSystemChangeStage(
            doc.getString(DatabaseConstants.UNIQUE_ID),
            doc.getString(DatabaseConstants.SUBSYSTEM_ID),
            doc.getString(DatabaseConstants.SUBSYSTEM_STAGE),
            reg
        );                
    }
}
