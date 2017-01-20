
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

import java.io.*;


/**
 * <pre>
 * Created with IntelliJ IDEA.
 * User: ly
 * Date: 2017/1/20
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 * </pre>
 *
 * @author ly
 */
public class RarFilesTest {

    public static final String SEPARATOR = File.separator;

    public static void javaCmd(){
        String cmd = "ping www.baidu.com";

        Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                //获得命令执行后在控制台的输出信息
                System.out.println(lineStr);// 打印输出信息
            //检查命令是否执行失败。
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
                    System.err.println("命令执行失败!");
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int BUFFEREDSIZE = 1024;

    /**
     * 解压zip格式的压缩文件到当前文件夹
     * @param zipFileName
     * @throws Exception
     */
    public synchronized void unzipFile(String zipFileName) throws Exception {
        try {
            File f = new File(zipFileName);
            ZipFile zipFile = new ZipFile(zipFileName);
            if((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            String strPath, gbkPath, strtemp;
            File tempFile = new File(f.getParent());
            strPath = tempFile.getAbsolutePath();
            java.util.Enumeration e = zipFile.getEntries();
            while(e.hasMoreElements()){
                org.apache.tools.zip.ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath=zipEnt.getName();
                if(zipEnt.isDirectory()){
                    strtemp = strPath + "/" + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    //读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath=zipEnt.getName();
                    strtemp = strPath + "/" + gbkPath;

                    //建目录
                    String strsubdir = gbkPath;
                    for(int i = 0; i < strsubdir.length(); i++) {
                        if(strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + "/" + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if(!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 解压zip格式的压缩文件到指定位置
     * @param zipFileName 压缩文件
     * @param extPlace 解压目录
     * @throws Exception
     */
    public synchronized void unzip(String zipFileName, String extPlace) throws Exception {
        try {
            (new File(extPlace)).mkdirs();
            File f = new File(zipFileName);
            ZipFile zipFile = new ZipFile(zipFileName);
            if((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            String strPath, gbkPath, strtemp;
            File tempFile = new File(extPlace);
            strPath = tempFile.getAbsolutePath();
            java.util.Enumeration e = zipFile.getEntries();
            while(e.hasMoreElements()){
                org.apache.tools.zip.ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath=zipEnt.getName();
                if(zipEnt.isDirectory()){
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    //读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath=zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;

                    //建目录
                    String strsubdir = gbkPath;
                    for(int i = 0; i < strsubdir.length(); i++) {
                        if(strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if(!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 解压zip格式的压缩文件到指定位置
     * @param zipFileName 压缩文件
     * @param extPlace 解压目录
     * @throws Exception
     */
    public synchronized void unzip(String zipFileName, String extPlace,boolean whether) throws Exception {
        try {
            (new File(extPlace)).mkdirs();
            File f = new File(zipFileName);
            ZipFile zipFile = new ZipFile(zipFileName);
            if((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("要解压的文件不存在!");
            }
            String strPath, gbkPath, strtemp;
            File tempFile = new File(extPlace);
            strPath = tempFile.getAbsolutePath();
            java.util.Enumeration e = zipFile.getEntries();
            while(e.hasMoreElements()){
                org.apache.tools.zip.ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath=zipEnt.getName();
                if(zipEnt.isDirectory()){
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    //读写文件
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath=zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;

                    //建目录
                    String strsubdir = gbkPath;
                    for(int i = 0; i < strsubdir.length(); i++) {
                        if(strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if(!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 压缩zip格式的压缩文件
     * @param inputFilename 压缩的文件或文件夹及详细路径
     * @param zipFilename 输出文件名称及详细路径
     * @throws IOException
     */
    public synchronized void zip(String inputFilename, String zipFilename) throws IOException {
        zip(new File(inputFilename), zipFilename);
    }

    /**
     * 压缩zip格式的压缩文件
     * @param inputFile 需压缩文件
     * @param zipFilename 输出文件及详细路径
     * @throws IOException
     */
    public synchronized void zip(File inputFile, String zipFilename) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
        try {
            zip(inputFile, out, "");
        } catch (IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    /**
     * 压缩zip格式的压缩文件
     * @param inputFile 需压缩文件
     * @param out 输出压缩文件
     * @param base 结束标识
     * @throws IOException
     */
    private synchronized void zip(File inputFile, ZipOutputStream out, String base) throws IOException {
        if (inputFile.isDirectory()) {
            File[] inputFiles = inputFile.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                zip(inputFiles[i], out, base + inputFiles[i].getName());
            }
        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }
            FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                byte[] by = new byte[BUFFEREDSIZE];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                in.close();
            }
        }
    }


    /**
     * 解压tar格式的压缩文件到指定目录下
     * @param tarFileName 压缩文件
     * @param extPlace 解压目录
     * @throws Exception
     */
    public synchronized void untar(String tarFileName, String extPlace) throws Exception{

    }

    /**
     * 压缩tar格式的压缩文件
     * @param inputFilename 压缩文件
     * @param tarFilename 输出路径
     * @throws IOException
     */
    public synchronized void tar(String inputFilename, String tarFilename) throws IOException{
        tar(new File(inputFilename), tarFilename);
    }

    /**
     * 压缩tar格式的压缩文件
     * @param inputFile 压缩文件
     * @param tarFilename 输出路径
     * @throws IOException
     */
    public synchronized void tar(File inputFile, String tarFilename) throws IOException{
        TarOutputStream out = new TarOutputStream(new FileOutputStream(tarFilename));
        try {
            tar(inputFile, out, "");
        } catch (IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    /**
     * 压缩tar格式的压缩文件
     * @param inputFile 压缩文件
     * @param out 输出文件
     * @param base 结束标识
     * @throws IOException
     */
    private synchronized void tar(File inputFile, TarOutputStream out, String base) throws IOException {
        if (inputFile.isDirectory()) {
            File[] inputFiles = inputFile.listFiles();
            out.putNextEntry(new TarEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                tar(inputFiles[i], out, base + inputFiles[i].getName());
            }
        } else {
            if (base.length() > 0) {
                out.putNextEntry(new TarEntry(base));
            } else {
                out.putNextEntry(new TarEntry(inputFile.getName()));
            }
            FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                byte[] by = new byte[BUFFEREDSIZE];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                in.close();
            }
        }
    }

    /**
     * 解压指定RAR文件到当前文件夹
     * @param srcRar 指定解压
     *  @param password 压缩文件时设定的密码
     * @throws IOException
     */
    public static void unrar(String srcRar, String password) throws IOException {
        unrar(srcRar, null, password);
    }

    /**
     * 解压指定的RAR压缩文件到指定的目录中
     * @param srcRar 指定的RAR压缩文件
     * @param destPath 指定解压到的目录
     *  @param password 压缩文件时设定的密码
     * @throws IOException
     */
    public static void unrar(String srcRar, String destPath, String password) throws IOException {
        File srcFile = new File(srcRar);
        if (!srcFile.exists()) {
            return;
        }
        if (null == destPath || destPath.length() == 0) {
            unrar(srcFile, srcFile.getParent(), password);
            return;
        }
        unrar(srcFile,destPath, password);
    }

    /**
     * 解压指定RAR文件到当前文件夹
     * @param srcRarFile 解压文件
     * @param password 压缩文件时设定的密码
     * @throws IOException
     */
    public static void unrar(File srcRarFile, String password) throws IOException {
        if (null == srcRarFile || !srcRarFile.exists()) {
            throw new IOException("指定文件不存在.");
        }
        unrar(srcRarFile, srcRarFile.getParent(),password);
    }

    /**
     * 解压指定RAR文件到指定的路径
     * @param srcRarFile 需要解压RAR文件
     * @param destPath 指定解压路径
     * @param password 压缩文件时设定的密码
     * @throws IOException
     */
    public static void unrar(File srcRarFile, String destPath, String password) throws IOException {
        if (null == srcRarFile || !srcRarFile.exists()) {
            throw new IOException("指定压缩文件不存在.");
        }
        if (!destPath.endsWith(SEPARATOR)) {
            destPath += SEPARATOR;
        }
        Archive archive = null;
        OutputStream unOut = null;
        try {
            archive = new Archive(srcRarFile, password, false);

            System.out.println(archive.getFile());
            System.out.println(archive.getFileHeaders().size());

            FileHeader fileHeader = archive.nextFileHeader();

            //System.out.println(fileHeader.getFileNameString());
            while(null != fileHeader) {
                if (!fileHeader.isDirectory()) {
                    // 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    String destFileName = "";
                    String destDirName = "";
                    if (SEPARATOR.equals("/")) {  // 非windows系统
                        destFileName = (destPath + fileHeader.getFileNameW()).replaceAll("\\\\", "/");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                    } else {  // windows系统
                        destFileName = (destPath + fileHeader.getFileNameW()).replaceAll("/", "\\\\");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                    }
                    // 2创建文件夹
                    File dir = new File(destDirName);
                    if (!dir.exists() || !dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    // 抽取压缩文件
                    unOut = new FileOutputStream(new File(destFileName));
                    archive.extractFile(fileHeader, unOut);
                    unOut.flush();
                    unOut.close();
                }
                fileHeader = archive.nextFileHeader();
            }
            archive.close();
        } catch (RarException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(unOut);
        }
    }

    public static void rarFiles() {
        String password = "nowledgedata.com.cn";

        String fileName = "F:/图像数据";
        String destDir = "F:/model.rar";

        //* rarName 压缩后的压缩文件名(不包含后缀)
        //* fileName 需要压缩的文件名(必须包含路径)
        //* destDir 压缩后的压缩文件存放路径
        //* password 解压密码
        //rarCmd ="C:\\Program Files\\WinRAR\\WinRAR.exe a -hp" + password + " -ibck -r -k " + destDir + rarName + ".rar " + fileName;

        String rarCmd ="C:\\Program Files\\WinRAR\\WinRAR.exe a -hp" + password + " -ibck -r -k " + destDir + " " + fileName;
        Process p = null;
        int exitVal = 0;
        try {
            Runtime rt = Runtime.getRuntime();
            System.out.println(rarCmd);
            p = rt.exec(rarCmd);
            exitVal = p.waitFor();
            if (exitVal == 0)
                p.destroy();//结束程序占用
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (exitVal == 0)
            {
                p.destroy();//结束程序占用
            }
        }
    }

    public static void main(String[] args) throws Exception {
        unrar("C:/Users/ly/Desktop/test.rar","123123");
    }
}
