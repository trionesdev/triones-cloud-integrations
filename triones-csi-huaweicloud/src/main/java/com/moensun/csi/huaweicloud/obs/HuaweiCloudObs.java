package com.moensun.csi.huaweicloud.obs;

import com.moensun.csi.api.oss.OssTemplate;
import com.moensun.csi.api.oss.request.*;
import com.moensun.csi.api.oss.response.OssGetObjectResponse;
import com.moensun.csi.api.oss.response.OssListObjectsResponse;
import com.moensun.csi.api.oss.response.OssPutObjectResponse;
import com.moensun.csi.util.OssUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;

public class HuaweiCloudObs implements OssTemplate {
    private final ObsClient obsClient;
    private final HuaweiCloudObsConfig huaweiCloudObsConfig;

    public HuaweiCloudObs(ObsClient obsClient, HuaweiCloudObsConfig huaweiCloudObsConfig) {
        this.obsClient = obsClient;
        this.huaweiCloudObsConfig = huaweiCloudObsConfig;
    }


    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        return null;
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucketName(bucketName);
        putObjectRequest.setObjectKey(request.getObjectName());
        putObjectRequest.setInput(request.getInputStream());
        PutObjectResult result = obsClient.putObject(putObjectRequest);
        return OssPutObjectResponse.builder().url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
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
        return StringUtils.isNotBlank(bucketName) ? bucketName : huaweiCloudObsConfig.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : huaweiCloudObsConfig.getUrlPrefix();
    }

}
