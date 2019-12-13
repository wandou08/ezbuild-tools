package cn.ezbuild.tools.utils;


import cn.hutool.core.io.FileUtil;
import com.jcraft.jsch.*;
import com.jcraft.jsch.Channel;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * FTP工具
 * @author wandoupeas
 * @since 0.0.1
 * @version 0.0.1
 */
public class FtpUtils {
    private ChannelSftp sftp = null;
    private Channel channel = null;
    private Session sshSession = null;
    private String host;
    private Integer port;
    private String username;
    private String password;

    public FtpUtils(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        init();
    }

    @SneakyThrows
    public void init() {
        JSch jsch = new JSch();
        jsch.getSession(username, host, port);
        sshSession = jsch.getSession(username, host, port);
        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        channel = sshSession.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
    }

    public void close() {
        System.out.println("Close - start");
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
        if (sshSession != null) {
            if (sshSession.isConnected()) {
                sshSession.disconnect();
            }
        }
        System.out.println("Close - end");
    }

    public static void main(String[] args) throws IOException {
        @Cleanup FtpUtils ftpUtils = new FtpUtils("47.110.46.83", 22, "root", "Qin201001");

        ftpUtils.uploadPath("F:\\wandoupeas\\templates\\html\\meipaly\\files", "www.chinaqua.cn", "/www/wwwroot");
//        listFileNames("47.110.46.83", 22, "root", "Qin201001", "/jar");

//        Ftp ftp = new Ftp("47.110.46.83", 22, "root", "Qin201001");
//        ftp.cd("/www/wwwroot");
//        List<String> ls = ftp.ls("/www/wwwroot");
//        ls.forEach(System.out::println);
    }

    private List<String> listFileNames(String host, int port, String username, final String password, String dir) {
        List<String> list = new ArrayList<String>();

        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            SftpATTRS sftpATTRS = sftp.lstat("/opt/upload");
            if (!sftpATTRS.isDir()) {
                sftp.mkdir("/opt/upload");
            }

            uploadPath("F:\\wandoupeas\\templates\\html\\meipaly\\files", "files", "/opt/upload");

            sftp.cd("/opt/upload");
            sftp.put(FileUtil.getInputStream("c:/Users/DX/Desktop/clear.png"), "clear.png");
            Vector<?> vector = sftp.ls(dir);
            for (Object item : vector) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) item;
                System.out.println(entry.getFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeChannel(sftp);
            closeChannel(channel);
            closeSession(sshSession);
        }
        return list;
    }

    private static void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private static void closeSession(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    @SneakyThrows
    private void uploadPath(String path, String parentPath, String uploadPath) {
        File[] files = FileUtil.ls(path);
        for (File file : files) {
            System.out.println(parentPath + "/" + file.getName());
            if (!isExistDir(uploadPath + "/" + parentPath)) {
                sftp.mkdir(uploadPath + "/" + parentPath);
            }
            if (FileUtil.isDirectory(file)) {
                uploadPath(file.getPath(), parentPath + "/" + file.getName(), uploadPath);
            } else {
                System.out.println(parentPath + "/" + file.getName());
                sftp.cd(uploadPath + "/" + parentPath);
                sftp.put(FileUtil.getInputStream(file.getPath()), file.getName());
            }
        }
    }

    public boolean isExistDir(String path) {
        boolean isExist = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(path);
            isExist = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isExist = false;
            }
        }
        return isExist;

    }
}
