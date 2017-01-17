package sparkSql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.LinkedList;
import java.util.List;


/**
 * <pre>
 * User: liuyu
 * Date: 2016/9/2
 * Time: 10:51
 * </pre>
 *
 * @author liuyu
 */

public class MySparkSql {
    public static  void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("MySparkSql").setMaster("local[3]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        JavaRDD<MyBean> javaRDD = jsc.textFile("file:///C:/Users/ly/Downloads/12/12.csv")
                .map(new Function<String, MyBean>() {
                    public MyBean call(String v1) throws Exception {
                        final String[] split = v1.split(",");
                        MyBean myBean = new MyBean();
                        myBean.setWifiMac(split[0]);
                        myBean.setClientMac(split[1]);
                        myBean.setFirstAt(Long.parseLong(split[2]));
                        myBean.setUpdateAt(Long.parseLong(split[3]));

                        return myBean;
                    }
                });

        final SQLContext sqlContext = new SQLContext(jsc);
        DataFrame dataframe = sqlContext.createDataFrame(javaRDD, MyBean.class);
        dataframe.registerTempTable("mytable");
        dataframe.printSchema();

       /* //query total count月访问总量
        sqlContext.sql("select count(distinct(clientMac)) from mytable").show();
        //sqlContext.sql("select count(clientMac) from mytable").show();
        //query every detector total  总览部分各区域访客
        sqlContext.sql("select wifiMac, count(distinct(clientMac)) as num from mytable  group by wifiMac order by num desc").show();
        //sqlContext.sql("select wifiMac, count(clientMac) as num from mytable  group by wifiMac order by num desc").show();*/


        DataFrame count1 = sqlContext.sql("SELECT wifiMac,clientMac,(updateAt-firstAt) as countClient FROM mytable having countClient < 24*60*60*1000");
        count1.registerTempTable("newtable");

        DataFrame count2= sqlContext.sql("SELECT clientMac,max(countClient) as countClient1 FROM newtable group by clientMac");
        count2.registerTempTable("table1");

        /*
         * 平均访问时长
         */
        /*DataFrame count3= sqlContext.sql("SELECT count(distinct(clientMac)),sum(countClient1) FROM table1");
        count3.show();*/

        /*
         * 40分钟以内
         */
        /*DataFrame count5= sqlContext.sql("SELECT count(distinct(clientMac))as client ,sum(countClient1) as countdata FROM table1 where countClient1 <= 40*60*1000");
        count5.show();*/

        /*
         * 41分钟到2.5小时之间
         */
        DataFrame count6= sqlContext.sql("SELECT count(distinct(clientMac))as client ,sum(countClient1) as countdata FROM table1 where countClient1 > 40*60*1000 and countClient1 < 2.5*60*60*1000");
        count6.show();


        //2.5小时以上
        DataFrame count7= sqlContext.sql("SELECT count(distinct(clientMac))as client ,sum(countClient1) as countdata FROM table1 where countClient1 >= 2.5*60*60*1000");
        count7.show();

       /* DataFrame count8 = sqlContext.sql("SELECT wifiMac,clientMac,max(countClient) as countClient1 FROM newtable  group by wifiMac,clientMac ");
        count8.registerTempTable("table2");*/
        //访问时长小于40分钟的各区域访问量
        /*DataFrame sql1 = sqlContext.sql("SELECT wifiMac,count(distinct(clientMac))  as  client  FROM  table2 where countClient1 <= 40*60*1000 group by wifiMac ");
        sql1.show();*/

        //访问时长大于40分钟而小于2.5小时的各区域访问量
        /*DataFrame sql2 = sqlContext.sql("SELECT wifiMac,count(distinct(clientMac))  as  client  FROM  table2 where  countClient1 > 40*60*1000 and countClient1 < 2.5*60*60*1000  group by wifiMac ");
        sql2.show();*/

        //访问时长大于2.5小时的各区域访问量
        /*DataFrame sql3 = sqlContext.sql("SELECT wifiMac,count(distinct(clientMac))  as  client  FROM  table2  where countClient1 >= 2.5*60*60*1000 group by wifiMac ");
        sql3.show();*/


        /**
         map.put("78:d3:8d:c3:c2:d5", "歇马台");
         map.put("78:d3:8d:ca:9a:cd", "游客中心");
         map.put("78:d3:8d:ca:9c:c9", "碾旁湾");
         map.put("78:d3:8d:cf:16:b4", "海潮寺");
         map.put("78:d3:8d:cf:16:b8", "万安关");
         map.put("78:d3:8d:cf:16:cc", "绣花楼");
         map.put("78:d3:8d:cf:16:d0", "飞龙关");
         map.put("78:d3:8d:cf:16:d4", "监控中心");
         */
    }
}
