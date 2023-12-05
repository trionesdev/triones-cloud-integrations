package com.trionesdev.csi.aliyun.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.PutObjectResult;
import com.trionesdev.csi.api.oss.OssException;
import com.trionesdev.csi.api.oss.OssTemplate;
import com.trionesdev.csi.api.oss.request.*;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;
import com.trionesdev.csi.util.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            return OssPutObjectResponse.builder().url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
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
        return OssUtils.joinPrefix(request.getObjectName(), urlPrefix(request.getUrlPrefix()));
    }

    @Override
    public String getObjectName(OssGetObjectNameRequest request) {
        return OssUtils.joinPrefix(request.getObjectUrl(), urlPrefix(request.getUrlPrefix()));
    }

    @Override
    public OssListObjectsResponse listObjects(OssListObjectsRequest request) {
        String bucketName = bucketName(request.getBucketName());
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName);
        listObjectsV2Request.setPrefix(request.getPrefix());
        listObjectsV2Request.setStartAfter(request.getStartAfter());
        listObjectsV2Request.setMaxKeys(request.getMaxKeys());
        listObjectsV2Request.setDelimiter(request.getDelimiter());
        ListObjectsV2Result result = oss.listObjectsV2(listObjectsV2Request);
        List<OssListObjectsResponse.ObjectSummary> objectSummaries = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result.getObjectSummaries())) {
            objectSummaries = result.getObjectSummaries().stream().map(t -> OssListObjectsResponse.ObjectSummary.builder()
                    .bucketName(t.getBucketName()).key(t.getKey()).eTag(t.getETag()).size(t.getSize())
                    .lastModified(t.getLastModified().toInstant()).storageClass(t.getStorageClass()).type(t.getType())
                    .build()).collect(Collectors.toList());
        }
        return OssListObjectsResponse.builder().objectSummaries(objectSummaries).build();
    }

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : aliYunOssProperties.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : aliYunOssProperties.getUrlPrefix();
    }
}
