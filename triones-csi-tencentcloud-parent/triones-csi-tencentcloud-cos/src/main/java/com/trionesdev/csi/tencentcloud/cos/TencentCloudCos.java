package com.trionesdev.csi.tencentcloud.cos;

import com.trionesdev.csi.api.oss.OssException;
import com.trionesdev.csi.api.oss.OssTemplate;
import com.trionesdev.csi.api.oss.request.*;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;
import com.trionesdev.csi.api.oss.util.OssUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import org.apache.commons.lang3.StringUtils;

public class TencentCloudCos implements OssTemplate {

    private final COSClient cosClient;
    private final TencentCloudCosConfig tencentCloudCosProperties;

    public TencentCloudCos(COSClient cosClient, TencentCloudCosConfig tencentCloudCosProperties) {
        this.cosClient = cosClient;
        this.tencentCloudCosProperties = tencentCloudCosProperties;
    }


    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        return null;
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        try {
            String bucketName = bucketName(request.getBucketName());
            String urlPrefix = urlPrefix(request.getUrlPrefix());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, request.getObjectName(), request.getInputStream(), new ObjectMetadata());
            cosClient.putObject(putObjectRequest);
            return OssPutObjectResponse.builder().url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
        } catch (Exception ex) {
            throw new OssException(ex);
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
        return StringUtils.isNotBlank(bucketName) ? bucketName : tencentCloudCosProperties.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : tencentCloudCosProperties.getUrlPrefix();
    }
}
