package ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * <pre>
 * User: liuyu
 * Date: 2017/1/22
 * Time: 22:23
 * </pre>
 *
 * @author liuyu
 */
public class FtpUtil {
    /**
     * 上传文件
     * @param hostname ftp服务器地址
     * @param port ftp服务器端口号
     * @param username ftp登录账号
     * @param password ftp登录密码
     * @param pathname ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public static boolean uploadFile(String hostname, int port, String username,
                                     String password, String pathname, String fileName, InputStream inputStream){
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try{
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器

            ftpClient.enterLocalPassiveMode();

            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if(!FTPReply.isPositiveCompletion(replyCode)){
                return flag;
            }
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    /** * 上传文件（可对文件进行重命名） *
     * @param hostname FTP服务器地址 *
     * @param port FTP服务器端口号 *
     * @param username FTP登录帐号 *
     * @param password FTP登录密码 *
     * @param pathname FTP服务器保存目录 *
     * @param filename 上传到FTP服务器后的文件名称 *
     * @param originfilename 待上传文件的名称（绝对地址） *
     * @return */
    public static boolean uploadFileFromProduction(String hostname, int port, String username,
                                                   String password, String pathname, String filename, String originfilename){
        boolean flag = false;
        try {
            InputStream inputStream = new FileInputStream(new File(originfilename));
            flag = uploadFile(hostname, port, username, password, pathname, filename, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /** 上传文件（不可以进行文件的重命名操作） *
     * @param hostname FTP服务器地址 *
     * @param port FTP服务器端口号 *
     * @param username FTP登录帐号 *
     * @param password FTP登录密码 *
     * @param pathname FTP服务器保存目录 *
     * @param originfilename 待上传文件的名称（绝对地址） *
     * @return */
    public static boolean uploadFileFromProduction(String hostname, int port, String username,
                                                   String password, String pathname, String originfilename){
        boolean flag = false;
        try {
            String fileName = new File(originfilename).getName();
            InputStream inputStream = new FileInputStream(new File(originfilename));
            flag = uploadFile(hostname, port, username, password, pathname, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String[] args) throws Exception{
        FtpUtil.uploadFileFromProduction("202.101.104.126", 60021,"yunchou","yunchou@2017","/datahub","C:\\Users\\ly\\Desktop\\README\\README.md");
    }
}
