package com.trionesdev.csi.azure.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.trionesdev.csi.api.oss.OssTemplate;
import com.trionesdev.csi.api.oss.request.*;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;
import com.trionesdev.csi.api.oss.util.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class AzureBlob implements OssTemplate {
    private final BlobContainerClient blobContainerClient;
    private final AzureBlobConfig azureBlobConfig;

    public AzureBlob(BlobContainerClient blobContainerClient, AzureBlobConfig azureBlobConfig) {
        this.blobContainerClient = blobContainerClient;
        this.azureBlobConfig = azureBlobConfig;
    }

    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        return null;
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        BlobClient blobClient = blobContainerClient.getBlobClient(request.getObjectName());
        blobClient.upload(request.getInputStream(), true);
        return OssPutObjectResponse.builder()
                .url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix))
                .build();
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

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? OssUtils.pathJoin(urlPrefix, blobContainerClient.getBlobContainerName()) : blobContainerClient.getBlobContainerUrl();
    }
}
