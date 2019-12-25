package cn.ezbuild.tools.utils;

import cn.hutool.core.io.FileUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * <p>七牛工具类</p>
 *
 * @author wandoupeas
 * @version 0.0.1
 * @since 1.0.4
 */
@UtilityClass
public class QiniuUtils {

    /**
     * 功能描述
     * <p>
     * 获取token
     * </p>
     *
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @param bucket bucket
     * @return java.lang.String
     * @author wandoupeas
     * @since 1.0.4
     */
    public String getToken(String accessKey, String secretKey, String bucket) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    /**
     * 功能描述
     * <p>
     *    上传文件
     * </p>
     *
     * @param file 文件
     * @param domain 域名
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @param bucket bucket
     * @throws FileNotFoundException fileNotFoundException
     * @return java.lang.String
     * @author wandoupeas
     * @since 1.0.4
     */
    public String upload(File file, String domain, String accessKey, String secretKey, String bucket) throws FileNotFoundException {
        return uploadWithName(file, file.getName(), domain, accessKey, secretKey, bucket);
    }

    /**
     * 功能描述
     * <p>
     *    上传文件
     * </p>
     *
     * @param file 文件
     * @param fileName 文件名
     * @param domain 域名
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @param bucket bucket
     * @throws FileNotFoundException fileNotFoundException
     * @return java.lang.String
     * @author wandoupeas
     * @since 1.0.4
     */
    public String uploadWithName(File file, String fileName, String domain, String accessKey, String secretKey, String bucket) throws FileNotFoundException {
        if (file != null) {
            InputStream fileInputStream = new FileInputStream(file);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Configuration cfg = new Configuration();
            UploadManager uploadManager = new UploadManager(cfg);
            try {
                @Cleanup
                Response response = uploadManager.put(fileInputStream, fileName, upToken, null, null);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return domain + "/" + putRet.key;
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
        return "fail";
    }

    /**
     * 功能描述
     * <p>
     *    上传文件
     * </p>
     *
     * @param fileInputStream 文件流
     * @param fileName 文件名
     * @param domain 域名
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @param bucket bucket
     * @return java.lang.String
     * @author wandoupeas
     * @since 1.0.4
     */
    public String uploadWithName(InputStream fileInputStream, String fileName, String domain, String accessKey, String secretKey, String bucket) {
        if (fileInputStream != null) {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Configuration cfg = new Configuration();
            UploadManager uploadManager = new UploadManager(cfg);
            try {
                @Cleanup
                Response response = uploadManager.put(fileInputStream, fileName, upToken, null, null);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return domain + "/" + putRet.key;
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
        return "fail";
    }
}
