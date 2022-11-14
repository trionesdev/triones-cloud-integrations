package com.moensun.cloud.integration.qiniu.kodo;

import com.moensun.cloud.integration.api.oss.OssException;
import com.moensun.cloud.integration.api.oss.OssTemplate;
import com.moensun.cloud.integration.api.oss.request.*;
import com.moensun.cloud.integration.api.oss.response.OssGetObjectResponse;
import com.moensun.cloud.integration.api.oss.response.OssListObjectsResponse;
import com.moensun.cloud.integration.api.oss.response.OssPutObjectResponse;
import com.moensun.commons.core.util.FilePathUtils;
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
            return OssPutObjectResponse.builder().url(FilePathUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
        } catch (QiniuException e) {
            throw new OssException(e);
        }
    }

    @Override
    public void removeObjects(OssRemoveObjectsRequest request) {

    }

    @Override
    public String getObjectUrl(OssGetObjectUrlRequest request) {
        return FilePathUtils.joinPrefix(request.getObjectName(), urlPrefix(request.getUrlPrefix()));
    }

    @Override
    public String getObjectName(OssGetObjectNameRequest request) {
        return FilePathUtils.removePrefix(request.getObjectUrl(), urlPrefix(request.getUrlPrefix()));
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
