import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/7
 * Time: 14:02
 * </pre>
 *
 * @author liuyu
 */

public class SparkStreamingLearning {
    public static  void  main(String[] args){
        //初始化SparkConf实例
        SparkConf conf=new SparkConf().setMaster("local[2]").setAppName("ly-Streaming");
        //创建JavaStreamingContext实例，接收流数据，每隔1秒，将流数据切分[流数据处理的上下文，应用程序入口]
        JavaStreamingContext jsc=new JavaStreamingContext(conf, Durations.seconds(1));

        //监听端口产生的数据，创建DStream
        JavaReceiverInputDStream<String> lines=jsc.socketTextStream("192.168.1.101",9999);

        //单词分割
        JavaDStream<String> words=lines.flatMap(new FlatMapFunction<String, String>() {
            public Iterable<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" "));
            }
        });

        //单词计数
        JavaPairDStream<String, Integer> word=words.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s,1);
            }
        });


        //统计单词数
        JavaPairDStream<String,Integer>  wordCont=word.reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1+v2;
            }
        });


        //打印
        wordCont.print();

        jsc.start();

        jsc.awaitTermination();
    }

}
