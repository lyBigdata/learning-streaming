import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/25
 * Time: 9:53
 * </pre>
 *
 * @author liuyu
 */
public class Streaming2Sql {
    private static  final Pattern reg = Pattern.compile(" ");

    public static  void main(String[] args){

        String host="";
        String port="";

        if(args.length < 2){
            System.err.println("参数个数不正确！");
            System.exit(1);
        }else {
          host = args[0];
          port = args[1];
        }

        //初始化SparkConf实例
        SparkConf  conf=new SparkConf().setMaster("local[1]").setAppName("Streaming2Sql");
        //Create the context with a 1 second batch size
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(conf, Durations.seconds(1L));

        JavaReceiverInputDStream<String> stringJavaReceiverInputDStream = javaStreamingContext.socketTextStream(host, Integer.parseInt(port), StorageLevel.MEMORY_ONLY());

        //单词切分
        JavaDStream<String> stringJavaDStream = stringJavaReceiverInputDStream.flatMap(new FlatMapFunction<String, String>() {
            public Iterable<String> call(String s) throws Exception {

                return Arrays.asList(reg.split(s));
            }
        });

        stringJavaDStream.foreachRDD(new Function2<JavaRDD<String>, Time, Void>() {

            public Void call(JavaRDD<String> rdd, Time time) throws Exception {
                SQLContext sql = SQLContext.getOrCreate(rdd.context());

                rdd.map(new Function<String, Object>() {
                    public Object call(String word) throws Exception {
                        JavaRow javaRow = new JavaRow();
                        javaRow.setWord(word);
                        return javaRow;
                    }
                });

                DataFrame dataFrame = sql.createDataFrame(rdd, JavaRow.class);
                dataFrame.registerTempTable("words");

                DataFrame sql1 = sql.sql("select word , count(*) as total from words group by word");

                sql1.show();

                return null;
            }
        });
    }
}



