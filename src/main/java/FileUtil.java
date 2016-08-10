import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * 文件操作工具类
 * <pre>
 * Created with IntelliJ IDEA.
 * User: zachary.
 * Date: 2014/3/31
 * Time: 15:36
 * PC：windows'IDEA in company
 * To change this template use File | Settings | File Templates.
 * </pre>
 *
 * @author zachary.
 */
public class FileUtil {
    /**
     * 根据文件名获取classpath下的proterties配置文件
     *
     * @param fileName 文件名 xxx.properties
     * @return
     */
    public static Properties getProperties(String fileName) {
        Properties prop = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = FileUtil.class.getClassLoader();
        }
        String sqlFile = classLoader.getResource(fileName).getFile();
        try {
            prop.load(new FileInputStream(sqlFile));
        } catch (IOException e) {
            throw new IllegalArgumentException("解析文件< " + fileName + " >错误,请检查文件是否存在或者格式是否正确。");
        }
        return prop;
    }


    /**
     * 获得文件读取reader
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedReader getFileReader(File file) throws FileNotFoundException, UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
    }

    /**
     * 把文件按照行读成一个list
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static List<String> readLines(String fileName) throws IOException {
        return FileUtils.readLines(new File(fileName), "UTF-8");
    }

    /**
     * 文件读成一个string
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readFileToString(String fileName) throws IOException {
        return FileUtils.readFileToString(new File(fileName), "UTF-8");
    }

    /**
     * 覆盖写文件，原有有数据将会被覆盖
     *
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void writeToFileOverwrite(String fileName, String data) throws IOException {
        FileUtils.writeStringToFile(new File(fileName), data, "UTF-8");
    }

    /**
     * 往原有文件内容后面追加
     *
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void writeToFileAppend(String fileName, String data) throws IOException {
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(fileName, true);
        WriteToFile(data, fileWriter);
        fileWriter.close();
    }

    /**
     * 写list到文件。可以指定分隔符。如果不指定，则为换行
     *
     * @param fileName
     * @param data
     * @param split
     * @throws IOException
     */
    public static void writeCollectionsToFile(String fileName, Collection<String> data, String split) throws IOException {
        FileUtils.writeLines(new File(fileName), data, split);
    }

    /**
     * 添加一行
     *
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void writeToFileAppendLine(String fileName, String data) throws IOException {
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(fileName, true);
        WriteToFile(data + "\n", fileWriter);
        fileWriter.close();

    }

    public static void WriteToFile(String context, FileWriter fileWriter) throws IOException {
        fileWriter.write(context);
        fileWriter.flush();
    }
}
