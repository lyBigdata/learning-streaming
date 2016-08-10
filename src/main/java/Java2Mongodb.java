import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/28
 * Time: 13:44
 * </pre>
 *
 * @author liuyu
 */
public class Java2Mongodb {
    private String HOST;
    private String PORT;
    private String DB_NAME;
    private String COLLECTION;

    public  Java2Mongodb(){
        Properties pro =getProperties("mongodb.properties");
        HOST=pro.getProperty("mongo.host");
        PORT=pro.getProperty("mongo.port");
        DB_NAME=pro.getProperty("mongo.database");
        COLLECTION=pro.getProperty("mongo.collection");
    }

    public static Properties getProperties(String fileName){
        if(fileName.equals("") || fileName==null){
            return  null;
        }

        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if(classLoader == null){
            classLoader = Java2Mongodb.class.getClassLoader();
        }
        String file = classLoader.getResource(fileName).getFile();

        try{
            properties.load(new FileInputStream(file));
        }catch (IOException e){
            throw  new IllegalArgumentException();
        }

        return properties;
    }


    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getCOLLECTION() {
        return COLLECTION;
    }

    public void setCOLLECTION(String COLLECTION) {
        this.COLLECTION = COLLECTION;
    }

    public String getBD_NAME() {
        return DB_NAME;
    }

    public void setBD_NAME(String BD_NAME) {
        this.DB_NAME = BD_NAME;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }


    public static void main(String[] args) {

        //连接mongo服务
        Java2Mongodb mongodb = new Java2Mongodb();
        MongoClient mongoClient = new MongoClient(mongodb.HOST,Integer.parseInt(mongodb.PORT));
        //MongoIterable<String> allDatabases = mongoClient.listDatabaseNames();
        //System.out.println(allDatabases.first());
        //连接到数据库
        MongoDatabase database = mongoClient.getDatabase(mongodb.DB_NAME);
        //System.out.println(database.getName());
        //获取集合
        MongoCollection<Document> collection = database.getCollection(mongodb.COLLECTION);

        Document document = new Document("name", "liuyu").
                append("age","22").
                append("addr","陕西西安")
                .append("学历","本科");

        collection.insertOne(document);

        //获取迭代器FindIterable<Document>
        FindIterable<Document> documents = collection.find();
        //获取游标MongoCursor<Document>
        MongoCursor<Document> cursor = documents.iterator();

         while (cursor.hasNext()){
            System.out.println(cursor.next().toJson());
        }


        MongoClient mongo=new MongoClient(mongodb.HOST,Integer.parseInt(mongodb.PORT));
        DB db=mongo.getDB(mongodb.DB_NAME);
        Set<String> names = db.getCollectionNames();

        /*JavaSparkContext sc =new JavaSparkContext("local","test");
        Configuration config =new Configuration();
        //解释 主机：端口号/数据库名.Collection名
        config.set("mongo.input.uri","mongodb://192.168.1.115:8888/lydb.userInfo");
        config.set("mongo.output.uri", "mongodb://192.168.1.115:8888/zjy.user");
        //读取
        JavaPairRDD<Object, BSONObject> mongoRDD = sc.newAPIHadoopRDD(config, MongoInputFormat.class, Object.class, BSONObject.class);
        //BasonObject-> text
        mongoRDD.foreach(new VoidFunction<Tuple2<Object, BSONObject>>() {
            public void call(Tuple2<Object, BSONObject> objectBSONObjectTuple2) throws Exception {
                System.out.println(objectBSONObjectTuple2._2().toMap().toString());
            }
        });

        JavaRDD<Text> result = mongoRDD.map(
                new Function<Tuple2<Object, BSONObject>, Text>() {
                    public Text call(Tuple2<Object, BSONObject> v1) throws Exception {
                        String title = (String) v1._2().get("age");
                        return new Text(title);
                    }
                }
        );

        result.foreach(new VoidFunction<Text>() {
            public void call(Text text) throws Exception {
                System.out.println(text);
            }
        });

        //copy lang.sanlu to lang.output
        mongoRDD.saveAsNewAPIHadoopFile("file:///copy",Object.class, Object.class, MongoOutputFormat.class, config);*/
    }
}
