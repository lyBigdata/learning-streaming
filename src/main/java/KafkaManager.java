/**
 * <pre>
 * User: liuyu
 * Date: 2016/8/12
 * Time: 17:16
 * </pre>
 *
 * @author liuyu
 */
import kafka.common.TopicAndPartition;
import org.I0Itec.zkclient.ZkClient;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.OffsetRange;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class KafkaManager<T extends Serializable> implements Serializable {

    protected static final long serialVersionUID = 42L;

    private String zkRoot = "/spark-kafka";

    /**
     * topic
     */
    private String topic;

    private String zkPath;
    /**
     * 分区数
     */
    private Integer partitionCount;

    private transient ZkClient zkClient = null;

    public KafkaManager(String zkServers, String topic, int partitionCount) {
        this.zkClient = new ZkClient(zkServers, 50000);
        this.zkPath = zkRoot + "/" + topic;
        this.topic = topic;
        this.partitionCount = partitionCount;
    }

    public void updateZKOffsets(JavaRDD<T> rdd) {
        OffsetRange[] offsetsList = ((HasOffsetRanges) rdd.rdd()).offsetRanges();
        Map<TopicAndPartition, Long> fromOffsets = new HashMap<TopicAndPartition, Long>();

        for (OffsetRange offsets : offsetsList) {
            TopicAndPartition topicAndPartition = new TopicAndPartition(offsets.topic(), offsets.partition());
            fromOffsets.put(topicAndPartition, offsets.untilOffset());
        }
        zkClient.writeData(zkPath, fromOffsets);
    }

    public Map<TopicAndPartition, Long> readZKOffsets() {
        if (!zkClient.exists(zkRoot)) {
            zkClient.createPersistent(zkRoot);
        }
        if (!zkClient.exists(zkPath)) {
            /**
             * 初始化，从头开始读取。
             */
            Map<TopicAndPartition, Long> fromOffsets = new HashMap<TopicAndPartition, Long>();
            for (int i = 0; i < partitionCount; i++) {
                TopicAndPartition topicAndPartition = new TopicAndPartition(topic, i);
                fromOffsets.put(topicAndPartition, 0L);
            }
            zkClient.createPersistent(zkPath, fromOffsets);
            return fromOffsets;
        }
        return zkClient.readData(zkPath);
    }

}

