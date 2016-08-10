import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/26
 * Time: 10:10
 * </pre>
 *
 * @author liuyu
 */
public class JavaConRedis {

    private Jedis redis;
    private JedisPool jedisPool;
    String  host = "192.168.1.115";
    String  port = "9999";

    @Before
    public void setup(){

        redis=new Jedis(host,Integer.parseInt(port));

        /*jedisPool = new JedisPool(new JedisPoolConfig(), host, Integer.parseInt(port), 1000);
        redis=jedisPool.getResource();*/
        redis.auth("123");
        redis.select(11);
    }





    @Test
    public void stringTest(){
        //redis.set("name","liuyu");
        String res=redis.get("name");
        System.out.println(res);

        redis.del("name");
        System.out.println(redis.del("name"));
        //jedisPool.close();

        redis.mset("name","liuyu","age","23","address","陕西西安");

        List<String> mget = redis.mget("name", "age", "address");

        for(String str: mget){
            System.out.println(str);
        }
    }

    @Test
    public void hashTest(){
      /*Map<String, String> map = new HashMap<String, String>();
        map.put("name","zjy");
        map.put("age","22");
        map.put("address","中国");
        map.put("xueli","本科");

        redis.hmset("user",map);*/

        Map<String, String> user = redis.hgetAll("user");

        for(Map.Entry<String,String> entry: user.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }



    @Test
    public void myTest(){
        try {
            final List<String> lines = FileUtil.readLines("F:/Work/learning-streaming/src/main/resources/test1.txt");
            for(Object line: lines){
                final String[] keyAndValue = line.toString().split("##");
                String key = keyAndValue[0];
                String[] value = keyAndValue[1].split("@@");
                StringBuilder res = new StringBuilder();
                value[3]= convertUTF8ToString(value[3].replace("\\x",""));
                //System.out.println(value[3]);
                //System.out.println(convertUTF8ToString("e58c97e4baac"));

                for(int i=0;i< value.length;i++){
                    if(i != (value.length-1)) {
                        res.append(value[i]).append("@@");
                    }
                }
                //System.out.println(res.toString());
                redis.set(key,res.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void readFile() {

        List<Object> all=readFile2AllLines("F:/Work/learning-streaming/src/main/resources/test1.txt");
        System.out.println(all.get(0));
    }


    /**
     * 16进制utf-8编码转换成中文
     * @param s="e58c97e4baac"
     * @return 北京
     */
    public static String convertUTF8ToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }

        try {
            s = s.toUpperCase();

            int total = s.length() / 2;
            int pos = 0;

            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {

                int start = i * 2;

                buffer[i] = (byte) Integer.parseInt(
                        s.substring(start, start + 2), 16);
                pos++;
            }

            return new String(buffer, 0, pos, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }


    public static List<Object> readFile2AllLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String tempString = null;
        List<Object>  allLines= new LinkedList<Object>();

        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                allLines.add(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return allLines;
    }
}
