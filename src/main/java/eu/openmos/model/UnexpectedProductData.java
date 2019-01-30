package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

/**
 * Object that represents the data that resource agents receive via socket from the Manufacturing
 * Service Bus (related to workstations) and that then are passed on to the optimizer.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class UnexpectedProductData extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757402L;    

    private String workstationId;
    private String expectedProductId;
    private String receivedProductId;

//    private static final int FIELDS_COUNT = 4;
    
    // reflection stuff
    public UnexpectedProductData() { super();    }

    public UnexpectedProductData(
            String workstationId, 
            String expectedProductId, 
            String receivedProductId, 
            Date registeredTimestamp) {
        
        super(registeredTimestamp);

        this.workstationId = workstationId;
        this.expectedProductId = expectedProductId;
        this.receivedProductId = receivedProductId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getExpectedProductId() {
        return expectedProductId;
    }

    public void setExpectedProductId(String expectedProductId) {
        this.expectedProductId = expectedProductId;
    }

    public String getReceivedProductId() {
        return receivedProductId;
    }

    public void setReceivedProductId(String receivedProductId) {
        this.receivedProductId = receivedProductId;
    }

    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * workstationId
     * expectedProductId
     * receivedProductId
     * registeredTimestamp
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(workstationId);
//        
//        builder.append(SerializationConstants.TOKEN_UNEXPECTED_PRODUCT_DATA);
//        builder.append(expectedProductId);
//        
//        builder.append(SerializationConstants.TOKEN_UNEXPECTED_PRODUCT_DATA);
//        builder.append(receivedProductId);
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
//        builder.append(SerializationConstants.TOKEN_UNEXPECTED_PRODUCT_DATA);
//        builder.append(stringRegisteredTimestamp);
//        
//        return builder.toString();
//        
//    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * workstationId
     * expectedProductId
     * receivedProductId
     * registeredTimestamp
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
     */
//    public static UnexpectedProductData fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_UNEXPECTED_PRODUCT_DATA);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("UnexpectedProductData - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        return new UnexpectedProductData(
//                tokenizer.nextToken(),                                          // workstation id
//                tokenizer.nextToken(),                                          // expected product id           
//                tokenizer.nextToken(),                                          // received product id
//                sdf.parse(tokenizer.nextToken())                                // registeredTimestamp
//        );
//    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * workstationId
     * expectedProductId
     * receivedProductId
     * registeredTimestamp
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        Document doc = new Document();
//        
//        doc.append("workstationId", workstationId);
//        doc.append("expectedProductId", expectedProductId);
//        doc.append("receivedProductId", receivedProductId);
//        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
//        
//        return doc;        
    }
}
