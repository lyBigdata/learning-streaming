import java.io.File;
import java.util.ArrayList;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
/**
 * <pre>
 * Created with IntelliJ IDEA.
 * User: ly
 * Date: 2017/1/17
 * Time: 14:19
 * To change this template use File | Settings | File Templates.
 * </pre>
 *
 * @author ly
 */
public class ZipFileTestApp {

    /**
     * 往zip文件中添加file
     */
    public void  addFilesWithStandardZipEncryption() {
        try {
            // Initiate ZipFile object with the path/name of the zip file.
            ZipFile zipFile = new ZipFile("f:\\addFilesWithStandardZipEncryption.zip");

            // Build the list of files to be added in the array list
            // Objects of type File have to be added to the ArrayList
            ArrayList filesToAdd = new ArrayList();
            filesToAdd.add(new File("F:\\迅雷下载\\baidumap_apiV2_offline\\map_demo\\demo\\1_0.html"));
            filesToAdd.add(new File("F:\\迅雷下载\\baidumap_apiV2_offline\\map_demo\\demo\\1_1.html"));
            filesToAdd.add(new File("F:\\迅雷下载\\baidumap_apiV2_offline\\map_demo\\demo\\1_2.html"));

            // Initiate Zip Parameters which define various properties such
            // as compression method, etc.
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to store compression

            // Set the compression level
            // Several predefined compression levels are available
            // DEFLATE_LEVEL_FASTEST - Lowest compression level but higher speed of compression
            // DEFLATE_LEVEL_FAST - Low compression level but higher speed of compression
            // DEFLATE_LEVEL_NORMAL - Optimal balance between compression level/speed
            // DEFLATE_LEVEL_MAXIMUM - High compression level with a compromise of speed
            // DEFLATE_LEVEL_ULTRA - Highest compression level but low speed
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            // Set the encryption flag to true
            // If this is set to false, then the rest of encryption properties are ignored
            parameters.setEncryptFiles(true);

            // Set the encryption method to Standard Zip Encryption
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);

            // Set password
            parameters.setPassword("test123!");

            // Now add files to the zip file
            // Note: To add a single file, the method addFile can be used
            // Note: If the zip file already exists and if this zip file is a split file
            // then this method throws an exception as Zip Format Specification does not
            // allow updating split zip files
            zipFile.addFiles(filesToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个目录下的文件添加到zip文件
     */
    public void AddFolder() {

        try {
            // Initiate ZipFile object with the path/name of the zip file.
            ZipFile zipFile = new ZipFile("f:\\addFilesWithStandardZipEncryption.zip");

            // Folder to add
            String folderToAdd = "f:\\unziptest";

            // Initiate Zip Parameters which define various properties such
            // as compression method, etc.
            ZipParameters parameters = new ZipParameters();

            // set compression method to store compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            // Set the compression level
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            // Add folder to the zip file
            zipFile.addFolder(folderToAdd, parameters);

        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ZipFileTestApp().addFilesWithStandardZipEncryption();
    }
}
