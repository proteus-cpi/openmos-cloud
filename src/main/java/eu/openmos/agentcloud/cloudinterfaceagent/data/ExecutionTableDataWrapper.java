package eu.openmos.agentcloud.cloudinterfaceagent.data;

import eu.openmos.model.Base;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import java.io.Serializable;
import java.util.ArrayList;

public class ExecutionTableDataWrapper extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757703L;      
    
    private ExecutionTable[] executionTables;    
    private ArrayList<ExecutionTable> executionTablesArrayList;
    private ExecutionTable executionTable;
    private String executionTableId;
    private String subSystemId;
    
    /**
     * The ontology requested.
     */
    private String ontology;
    
    private String message;

    public ExecutionTable[] getExecutionTables() {
        return executionTables;
    }

    public void setExecutionTables(ExecutionTable[] executionTables) {
        this.executionTables = executionTables;
    }

    public ExecutionTable getExecutionTableById() {
        return executionTable;
    }

    public ExecutionTable getExecutionTableBySubSystemId() {
        return executionTable;
    }

    public ExecutionTable getExecutionTable() {
        return executionTable;
    }

    public void setExecutionTable(ExecutionTable executionTable) {
        this.executionTable = executionTable;
    }

    public String getExecutionTableId() {
        return executionTableId;
    }

    public void setExecutionTableId(String executionTableId) {
        this.executionTableId = executionTableId;
    }

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String subSystemId) {
        this.subSystemId = subSystemId;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }
    
    public ArrayList<ExecutionTable> getExecutionTablesArrayList() {
        return executionTablesArrayList;
    }

    public void setExecutionTablesArrayList(ArrayList<ExecutionTable> executionTablesArrayList) {
        this.executionTablesArrayList = executionTablesArrayList;
    }
}
