package com.trionesdev.csi.qiniu.kodo;

import com.trionesdev.csi.api.oss.OssException;
import com.trionesdev.csi.api.oss.OssTemplate;
import com.trionesdev.csi.api.oss.request.*;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;
import com.trionesdev.csi.util.OssUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class QiNiuKoDo implements OssTemplate {

    private final Auth auth;
    private final QiNiuKoDoConfig qiNiuKoDoProperties;
    private final UploadManager uploadManager;

    public QiNiuKoDo(Auth auth, UploadManager uploadManager, QiNiuKoDoConfig qiNiuKoDoProperties) {
        this.auth = auth;
        this.qiNiuKoDoProperties = qiNiuKoDoProperties;
        this.uploadManager = uploadManager;
    }

    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        return null;
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        String upToken = auth.uploadToken(bucketName);
        try {
            Response response = uploadManager.put(request.getInputStream(), request.getObjectName(), upToken, null, null);
            return OssPutObjectResponse.builder().url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
        } catch (QiniuException e) {
            throw new OssException(e);
        }
    }

    @Override
    public void removeObjects(OssRemoveObjectsRequest request) {

    }

    @Override
    public String getObjectUrl(OssGetObjectUrlRequest request) {
        return OssUtils.joinPrefix(request.getObjectName(), urlPrefix(request.getUrlPrefix()));
    }

    @Override
    public String getObjectName(OssGetObjectNameRequest request) {
        return OssUtils.removePrefix(request.getObjectUrl(), urlPrefix(request.getUrlPrefix()));
    }

    @Override
    public OssListObjectsResponse listObjects(OssListObjectsRequest request) {
        return null;
    }

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : qiNiuKoDoProperties.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : qiNiuKoDoProperties.getUrlPrefix();
    }
}
