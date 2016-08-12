import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;
import sun.nio.cs.StreamDecoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/7
 * Time: 16:55
 * </pre>
 *
 * @author liuyu
 */
public class KafkaData2Streaming {
    public static void  main(String[] args) {

        String brokers = "192.168.1.101:9092";
        //"liuyu-test-topic2";
        String topics = "liuyu-test-topic3";

        Map<String, String> kafkaParms = new HashMap<String, String>();
        kafkaParms.put("metadata.broker.list", brokers);

        HashSet<String> topicSet = new HashSet<String>(Arrays.asList(topics.split(",")));

        //JavaSparkContext jsc = new JavaSparkContext();
        SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafka").setMaster("local");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(1));


        JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParms,
                topicSet
            );

        JavaDStream<String> lines=messages.map(new Function<Tuple2<String,String>, String>() {
            public String call(Tuple2<String, String> tuple2) {
                return tuple2._2();
            }
        });

        lines.print();

        jssc.start();
        jssc.awaitTermination();


    }
 }
