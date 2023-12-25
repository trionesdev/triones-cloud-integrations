package com.trionesdev.csi.googlecloud.storage;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.io.ByteStreams;
import com.trionesdev.csi.api.oss.OssException;
import com.trionesdev.csi.api.oss.OssTemplate;
import com.trionesdev.csi.api.oss.request.OssGetObjectNameRequest;
import com.trionesdev.csi.api.oss.request.OssGetObjectRequest;
import com.trionesdev.csi.api.oss.request.OssGetObjectUrlRequest;
import com.trionesdev.csi.api.oss.request.OssListObjectsRequest;
import com.trionesdev.csi.api.oss.request.OssPutObjectRequest;
import com.trionesdev.csi.api.oss.request.OssRemoveObjectsRequest;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;
import com.trionesdev.csi.api.oss.util.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Slf4j
public class GoogleStorage implements OssTemplate {

    private final Storage storage;
    private final GoogleStorageConfig googleStorageProperties;


    public GoogleStorage(Storage storage, GoogleStorageConfig googleStorageProperties) {
        this.storage = storage;
        this.googleStorageProperties = googleStorageProperties;
    }

    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        return null;
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        BlobId blobId = BlobId.of(bucketName, request.getObjectName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            storage.create(blobInfo, ByteStreams.toByteArray(request.getInputStream()));
            return OssPutObjectResponse.builder().url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new OssException();
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
        return StringUtils.isNotBlank(bucketName) ? bucketName : googleStorageProperties.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : googleStorageProperties.getUrlPrefix();
    }
}
