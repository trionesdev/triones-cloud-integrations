package com.moensun.cloud.integration.api.oss;

import com.moensun.cloud.integration.api.oss.request.*;
import com.moensun.cloud.integration.api.oss.response.OssGetObjectResponse;
import com.moensun.cloud.integration.api.oss.response.OssListObjectsResponse;
import com.moensun.cloud.integration.api.oss.response.OssPutObjectResponse;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface OssTemplate {

    OssGetObjectResponse getObject(OssGetObjectRequest request);

    OssPutObjectResponse putObject(OssPutObjectRequest request);

    void removeObjects(OssRemoveObjectsRequest request);

    String getObjectUrl(OssGetObjectUrlRequest request);

    String getObjectName(OssGetObjectNameRequest request);

    OssListObjectsResponse listObjects(OssListObjectsRequest request);
}
