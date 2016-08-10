import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.hbase.AsyncHbaseEventSerializer;
import org.hbase.async.AtomicIncrementRequest;
import org.hbase.async.PutRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/8/1
 * Time: 15:10
 * </pre>
 *
 * @author liuyu
 */

public class SplittingHbaseSinkSerializer implements AsyncHbaseEventSerializer {
    private byte[] table;  //Hbase的表
    private byte[] colFam;   //
    private Event currentEvent;
    private byte[][] columnNames;
    private final List<PutRequest> puts = new ArrayList<PutRequest>();
    private final List<AtomicIncrementRequest> incs = new ArrayList<AtomicIncrementRequest>();
    private byte[] currentRowKey;
    private final byte[] eventCountCol = "eventCount".getBytes();


    public void initialize(byte[] table, byte[] cf) {
        this.table = table;
        this.colFam = cf;
    }

    public void setEvent(Event event) {
        // Set the event and verify that the rowKey is not present
        this.currentEvent = event;
        String rowKeyStr = currentEvent.getHeaders().get("rowKey");
        if (rowKeyStr == null) {
            throw new FlumeException("No row key found in headers!");
        }
        currentRowKey = rowKeyStr.getBytes();
    }


    public List<PutRequest> getActions() {
        // Split the event body and get the values for the columns
        String eventStr = new String(currentEvent.getBody());
        String[] cols = eventStr.split(",");
        puts.clear();
        for (int i = 0; i < cols.length; i++) {
            //Generate a PutRequest for each column.
            PutRequest req = new PutRequest(table, currentRowKey, colFam,
                    columnNames[i], cols[i].getBytes());
            puts.add(req);
        }
        return puts;
    }


    public List<AtomicIncrementRequest> getIncrements() {
        incs.clear();
        //Increment the number of events received
        incs.add(new AtomicIncrementRequest(table, "totalEvents".getBytes(), colFam, eventCountCol));
        return incs;
    }


    public void cleanUp() {
        table = null;
        colFam = null;
        currentEvent = null;
        columnNames = null;
        currentRowKey = null;
    }


    public void configure(Context context) {
        //Get the column names from the configuration
        String cols = new String(context.getString("columns"));
        String[] names = cols.split(",");
        byte[][] columnNames = new byte[names.length][];
        int i = 0;
        for(String name : names) {
            columnNames[i++] = name.getBytes();
        }
    }


    public void configure(ComponentConfiguration conf) {
    }

}


