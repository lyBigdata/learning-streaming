import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/25
 * Time: 11:52
 * </pre>
 *
 * @author liuyu
 */
public class SqlTest {

    public static  void main(String[] args){

        SparkConf conf= new SparkConf().setAppName("sqlTest").setMaster("local[1]");

        JavaSparkContext jsc=new JavaSparkContext(conf);

        SQLContext sqlContext = new SQLContext(jsc);

        DataFrame dataframe = sqlContext.read().json("F:/Work/learning-streaming/src/main/resources/json.json");

        dataframe.registerTempTable("peopleJson");

        dataframe.show();

        dataframe.printSchema();


        // SQL statements can be run by using the sql methods provided by sqlContext.
        DataFrame teenagers = sqlContext.sql("SELECT name FROM peopleJson WHERE age >= 13 AND age <= 19");

        teenagers.show();

        List<String> jsonData = Arrays.asList(
                "{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}");
        JavaRDD<String> anotherPeopleRDD = jsc.parallelize(jsonData);
        DataFrame anotherPeople = sqlContext.read().json(anotherPeopleRDD);

        anotherPeople.show();

    }
}
