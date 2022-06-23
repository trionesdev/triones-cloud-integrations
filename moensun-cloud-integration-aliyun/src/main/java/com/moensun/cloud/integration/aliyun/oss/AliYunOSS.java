package com.moensun.cloud.integration.aliyun.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import com.moensun.cloud.integration.api.oss.OssException;
import com.moensun.cloud.integration.api.oss.OssTemplate;
import com.moensun.cloud.integration.api.oss.request.*;
import com.moensun.cloud.integration.api.oss.response.OssGetObjectResponse;
import com.moensun.cloud.integration.api.oss.response.OssPutObjectResponse;
import com.moensun.commons.core.util.FilePathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class AliYunOSS implements OssTemplate {

    private final OSS oss;
    private final AliYunOssConfig aliYunOssProperties;

    public AliYunOSS(OSS oss, AliYunOssConfig aliYunOssProperties) {
        this.oss = oss;
        this.aliYunOssProperties = aliYunOssProperties;
    }

    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        return null;
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        try {
            PutObjectResult result = oss.putObject(bucketName, request.getObjectName(), request.getInputStream());
            return OssPutObjectResponse.builder().url(FilePathUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
    }

    @Override
    public void removeObjects(OssRemoveObjectsRequest request) {

    }

    @Override
    public String getObjectUrl(OssGetObjectUrlRequest request) {
        return FilePathUtils.joinPrefix(request.getObjectName(),urlPrefix(request.getUrlPrefix()));
    }

    @Override
    public String getObjectName(OssGetObjectNameRequest request) {
        return FilePathUtils.joinPrefix(request.getObjectUrl(),urlPrefix(request.getUrlPrefix()));
    }

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : aliYunOssProperties.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : aliYunOssProperties.getUrlPrefix();
    }
}
