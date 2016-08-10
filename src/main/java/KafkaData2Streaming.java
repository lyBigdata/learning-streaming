import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

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

        String brokers = args[0];
        String topics = args[1];

        Map<String, String> kafkaParms = new HashMap<String, String>();
        kafkaParms.put("metadata.broker.list", brokers);

        HashSet<String> topicSet = new HashSet<String>(Arrays.asList(topics.split(",")));

        JavaSparkContext jsc = new JavaSparkContext();
        JavaStreamingContext jssc = new JavaStreamingContext(jsc, Durations.seconds(1));


        /*JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                byte[].class,
                StreamDecoder.class,
                DefaultDecoder.class,
                kafkaParms,
                topicSet
            )；

        JavaDStream<String> lines=messages.map(new Function<Tuple2<String,String>, String>() {
            public String call(Tuple2<String, String> tuple2) {
                return tuple2._2();
            }
        });

        lines.print();*/

        jssc.start();
        jssc.awaitTermination();


    }
 }
