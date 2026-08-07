package com.trionesdev.csi.tencentcloud.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.trionesdev.csi.api.oss.OssException;
import com.trionesdev.csi.api.oss.OssTemplate;
import com.trionesdev.csi.api.oss.request.*;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;
import com.trionesdev.csi.api.oss.util.OssUtils;
import org.apache.commons.lang3.StringUtils;

public class TencentCloudCos implements OssTemplate {

    private final COSClient cosClient;
    private final TencentCloudCosConfig tencentCloudCosProperties;

    public TencentCloudCos(TencentCloudCosConfig tencentCloudCosProperties) {
        COSCredentials cred = new BasicCOSCredentials(tencentCloudCosProperties.getAccessKey(), tencentCloudCosProperties.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(tencentCloudCosProperties.getRegion()));
        this.cosClient = new COSClient(cred, clientConfig);
        this.tencentCloudCosProperties = tencentCloudCosProperties;
    }


    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {

        String bucketName = bucketName(request.getBucketName());
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, request.getObjectName(), request.getVersion());
        COSObject cosObject = cosClient.getObject(getObjectRequest);

        return OssGetObjectResponse.builder()
                .in(cosObject.getObjectContent())
                .contentType(cosObject.getObjectMetadata().getContentType())
                .contentLength(cosObject.getObjectMetadata().getContentLength())
                .build();
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

    @Override
    public Boolean objectExist(OssObjectExistRequest request) {
        String bucketName = bucketName(request.getBucketName());
        return cosClient.doesObjectExist(bucketName, request.getObjectName());
    }

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : tencentCloudCosProperties.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : tencentCloudCosProperties.getUrlPrefix();
    }
}
