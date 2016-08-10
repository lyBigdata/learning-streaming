import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/25
 * Time: 14:29
 * </pre>
 *
 * @author liuyu
 */
public class HiveSqlTest {
    public static  void main(String[] args){
        SparkConf sc=new SparkConf().setMaster("local").setAppName("HiveSqlTest");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sc);

        HiveContext hiveContext = new HiveContext(javaSparkContext.sc());

        //创建表
       /* try{
            hiveContext.sql("CREATE TABLE IF NOT EXISTS LYTEST(KEY STRING,VALUE STRING)ROW FORMAT DELIMITED FIELDS TERMINATED BY '_'");

        }catch (Exception e){
            System.err.println(e.getMessage());
        }

        hiveContext.sql("LOAD DATA LOCAL INPATH 'F:/Work/learning-streaming/src/main/resources/test.txt' OVERWRITE INTO TABLE LYTEST");
        */
        // Queries are expressed in HiveQL.
        Row[] results = hiveContext.sql("SELECT KEY, VALUE FROM LYTEST").collect();

        for(Row row : results){
            System.out.println(row);
        }
    }
}
