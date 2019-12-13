package cn.ezbuild.tools.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 功能描述
 * <p>
 *    文件操作工具类
 * </p>
 *
 * @author wandoupeas
 * @since 0.0.1
 */
@UtilityClass
public class FileUtils {
    /**
     * 功能描述
     * <p>
     *    把文件打成压缩包并保存在本地硬盘
     * </p>
     *
     * @param srcFiles 文件路径
     * @param zipFilePath 压缩路径
     * @param zipFileName 压缩文件名
     * @since 0.0.1
     */
    public static void saveZipFiles(List<String> srcFiles, String zipFilePath, String zipFileName) {
        try {
            // 创建文件夹
            File file = new File(zipFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            // 创建zip输出流
            @Cleanup
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath + File.separator + zipFileName));
            // 循环将源文件列表添加到zip文件中
            for (String filePath : srcFiles) {
                File inputFile = new File(filePath);
                zipFile(inputFile, "", zos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述
     * <p>
     *    把文件打成压缩包并输出到客户端浏览器中
     * <p>
     *
     * @param response http请求response
     * @param path 压缩路径
     * @param zipFileName 压缩文件名
     * @author wandoupeas
     * @since 0.0.1
     */
    public static void downloadPath(HttpServletResponse response, String path, String zipFileName) {
        // 设置读取数据缓存大小
        File zippedFile = ZipUtil.zip(path);
        try {
            downloadResponse(response, zipFileName);
            // --设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
            // 创建输入流读取文件
            @Cleanup
            InputStream inputStream = new BufferedInputStream(new FileInputStream(zippedFile));
            byte[] buffer = new byte[4096];

            @Cleanup
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            // 写入文件的方法，同上
            int size = 0;
            // 设置读取数据缓存大小
            while ((size = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileUtil.del(path);
        FileUtil.del(zippedFile);
    }

    /**
     * 功能描述
     * <p>
     *    把文件打成压缩包并输出到客户端浏览器中
     * <p>
     *
     * @param response http请求response
     * @param srcFiles 需要压缩的文件集合
     * @param zipFileName 压缩文件名
     * @since 0.0.1
     */
    public static void downloadZipFiles(HttpServletResponse response, List<String> srcFiles, String zipFileName) {
        try {
            downloadResponse(response, zipFileName);
            // --设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
            @Cleanup
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            for (String filePath : srcFiles) {
                File inputFile = new File(filePath);
                zipFile(inputFile, "", zos);
//                FilesUtil.delAllFile(filePath);
                FileUtil.del(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述
     * <p>
     *    压缩文件
     * </p>
     *
     * @param file 需要压缩的文件
     * @param parentPath 父级路径
     * @param zos 输出流
     * @since 0.0.1
     */
    private static void zipFile(File file, String parentPath, ZipOutputStream zos) {
        // 设置读取数据缓存大小
        byte[] buffer = new byte[4096];
        try {
            // 判断文件是否存在
            if (file.exists()) {
                // 判断是否属于文件，还是文件夹
                if (file.isFile()) {
                    // 创建输入流读取文件
                    @Cleanup
                    FileInputStream fis = new FileInputStream(file);
                    @Cleanup
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    // 将文件写入zip内，即将文件进行打包
                    zos.putNextEntry(new ZipEntry(parentPath + file.getName()));
                    // 写入文件的方法，同上
                    int size = 0;
                    // 设置读取数据缓存大小
                    while ((size = bis.read(buffer)) > 0) {
                        zos.write(buffer, 0, size);
                    }
                } else {
                    parentPath += file.getName() + File.separator;
                    // 如果是文件夹，则使用穷举的方法获取文件，写入zip
                    File[] files = file.listFiles();
                    if (ArrayUtil.isEmpty(files)) {
                        // 空文件夹的处理
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } else {
                        for (File child : files) {
                            zipFile(child, parentPath, zos);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件或文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return boolean
     * @since 0.0.1
     */
    public static boolean delAllFile(String path) {
        // boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            String[] fileList = file.list();
            String tempFilePath = null;
            for (String tempFileName : fileList) {
                if (path.endsWith(File.separator)) {
                    tempFilePath = path + tempFileName;
                } else {
                    tempFilePath = path + File.separator + tempFileName;
                }
                delAllFile(tempFilePath);
            }
            file.delete();
        }
        return true;
    }

    /**
     * 功能描述
     * <p>
     *    配置下载response
     * </p>
     *
     * @param response response
     * @param fileName 文件名
     * @since 0.0.1
     */
    private void downloadResponse(HttpServletResponse response, String fileName) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        // 对文件名进行编码处理中文问题
        fileName = new String(fileName.getBytes(), StandardCharsets.UTF_8);
        // inline在浏览器中直接显示，不提示用户下载
        // attachment弹出对话框，提示用户进行下载保存本地
        // 默认为inline方式
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }
}
