/**
 * <pre>
 * User: liuyu
 * Date: 2016/7/29
 * Time: 9:24
 * </pre>
 *
 * @author liuyu
 */

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class ZipFiles {


    private static final byte[] BUFFER = new byte[4096 * 1024];

    public static void copy(FileInputStream input, FileOutputStream output) throws IOException {
        int bytesRead = 0;
        while ((bytesRead = input.read(BUFFER))!= -1) {
            output.write(BUFFER, 0, bytesRead);
        }
    }

    public static void main(String[] args) throws Exception {

        ZipFile zipFile = new ZipFile("F:/Study/ndmp-etl2/ndmp-etl-client2.1/sources/RAW_TRAVEL_SCENIC_INFO.2016-06-14-887-test.zip");
        ZipOutputStream append = new ZipOutputStream(new FileOutputStream("F:/Study/ndmp-etl2/ndmp-etl-client2.1/sources/RAW_TRAVEL_SCENIC_INFO.zip"));

        InputStream inFile = null;

        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e1 = entries.nextElement();
            System.out.println("copy: " + e1.getName());

            append.putNextEntry(e1);

            try {
                inFile = zipFile.getInputStream(e1);

                if(inFile !=null){
                    int length = 0;// 读取的长度
                    while ((length = inFile.read(BUFFER)) != -1) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>"+length);
                        append.write(BUFFER, 0, length);
                    }
                    append.closeEntry();
                }

            } catch (IOException e) {

            } finally {
                // 当压缩完成,关闭流
                if(inFile != null){
                    try{
                        inFile.close();
                    } catch(IOException e){

                    }
                }
            }
        }


        File file1= new File("F:/Study/ndmp-etl2/ndmp-etl-client2.1/control/RAW_TRAVEL_SCENIC_INFO.xml");
        ZipEntry e2 = new ZipEntry(file1.getName());
        System.out.println("append: " + e2.getName());

        FileInputStream in=null;

        try {

            append.putNextEntry(e2);

            in = new FileInputStream(file1);

            int length = 0;// 读取的长度

            System.out.println("——————————————————————————————————————————");
            while ((length = in.read(BUFFER)) != -1) {
                System.out.println("<<<<<<<<<<<<<<<<<"+length);
                append.write(BUFFER, 0, length);
            }
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            // 当压缩完成,关闭流
            if(in != null){
                try{
                    in.close();
                } catch(IOException err){

                }
            }

        }

        zipFile.close();

    }
}