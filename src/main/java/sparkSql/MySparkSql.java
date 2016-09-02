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

        SparkConf conf = new SparkConf().setAppName("MySparkSql").setMaster("local[1]");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        JavaRDD<MyBean> javaRDD = jsc.textFile("file:///F:/Work/learning-streaming/src/main/resources/8.csv")
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

        //SQL statements can be run by using the sql methods provided by sqlContext.
        /*DataFrame countClient = sqlContext.sql("SELECT count(distinct(clientMac)) as countClient FROM mytable ");

        List<Row> rows = sqlContext.sql("SELECT distinct(wifiMac) FROM mytable ").collectAsList();

        for(Row row : rows){
            String str = row.get(0).toString();
            DataFrame sql = sqlContext.sql("SELECT wifiMac,count(distinct(clientMac) as count) FROM mytable WHERE wifiMac='"+str+"'");
            sql.show();
        }*/

       /*DataFrame count = sqlContext.sql("SELECT count(distinct(clientMac)) as countClient FROM mytable WHERE updateAt-firstAt >= 24*60*60*60*1000");
        count.show();*/

        //DataFrame sql = sqlContext.sql("SELECT count(clientMac) as countClient,clientMac FROM mytable  group by clientMac having countClient > 10 order by countClient asc");

        /*sqlContext.sql("SELECT count(clientMac) as countClient,clientMac FROM mytable  group by clientMac having countClient > 10 order by countClient asc").select("clientMac").show();
        System.out.println();*/

        DataFrame count1 = sqlContext.sql("SELECT wifiMac,clientMac,(updateAt-firstAt) as countClient FROM mytable having countClient < 24*60*60*1000");
        count1.registerTempTable("newtable");

        /*DataFrame count2= sqlContext.sql("SELECT clientMac,max(countClient) as countClient1 FROM newtable group by clientMac");
        count2.registerTempTable("table1");*/

        /**
         * 平均访问时长
         */
        /*DataFrame count3= sqlContext.sql("SELECT count(distinct(clientMac)),sum(countClient1) FROM table1");

        count3.show();*/


        /*LinkedList<DataFrame> list = new LinkedList<DataFrame>();
        *//**
         * 40分钟以内
         *//*
        DataFrame count5= sqlContext.sql("SELECT count(distinct(clientMac))as client ,sum(countClient1) as countdata FROM table1 where countClient1 <= 40*60*1000");
        list.add(count5);

        *//**
         * 41分钟到2.5小时之间
         *//*
        DataFrame count6= sqlContext.sql("SELECT count(distinct(clientMac))as client ,sum(countClient1) as countdata FROM table1 where countClient1 > 40*60*1000 and countClient1 < 2.5*60*60*1000");
        list.add(count6);

        *//**
         * 2.5小时以上
         *//*
        DataFrame count7= sqlContext.sql("SELECT count(distinct(clientMac))as client ,sum(countClient1) as countdata FROM table1 where countClient1 >= 2.5*60*60*1000");
        list.add(count7);

        for(DataFrame df : list){
            df.show();
        }*/

        DataFrame count8 = sqlContext.sql("SELECT wifiMac,clientMac,max(countClient) as countClient1 FROM newtable  group by wifiMac,clientMac ");
        count8.registerTempTable("table2");
        DataFrame sql1 = sqlContext.sql("SELECT wifiMac,count(distinct(clientMac))  as  client  FROM  table2 where countClient1 <= 40*60*1000 group by wifiMac ");

        DataFrame sql2 = sqlContext.sql("SELECT wifiMac,count(distinct(clientMac))  as  client  FROM  table2 where  countClient1 > 40*60*1000 and countClient1 < 2.5*60*60*1000  group by wifiMac ");

        DataFrame sql3 = sqlContext.sql("SELECT wifiMac,count(distinct(clientMac))  as  client  FROM  table2  where countClient1 >= 2.5*60*60*1000 group by wifiMac ");

        sql1.show();
        sql2.show();
        sql3.show();
    }
}
