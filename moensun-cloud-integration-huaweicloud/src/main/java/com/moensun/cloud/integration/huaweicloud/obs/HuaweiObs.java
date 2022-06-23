package com.moensun.cloud.integration.huaweicloud.obs;

import com.moensun.cloud.integration.api.oss.OssTemplate;
import com.moensun.cloud.integration.api.oss.request.*;
import com.moensun.cloud.integration.api.oss.response.OssGetObjectResponse;
import com.moensun.cloud.integration.api.oss.response.OssPutObjectResponse;
import com.moensun.commons.core.util.FilePathUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;

public class HuaweiObs implements OssTemplate {
    private final ObsClient obsClient;
    private final HuaweiObsConfig huaweiObsConfig;

    public HuaweiObs(ObsClient obsClient, HuaweiObsConfig huaweiObsConfig) {
        this.obsClient = obsClient;
        this.huaweiObsConfig = huaweiObsConfig;
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
        return OssPutObjectResponse.builder().url(FilePathUtils.joinPrefix(request.getObjectName(), urlPrefix)).build();
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

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : huaweiObsConfig.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : huaweiObsConfig.getUrlPrefix();
    }

}
