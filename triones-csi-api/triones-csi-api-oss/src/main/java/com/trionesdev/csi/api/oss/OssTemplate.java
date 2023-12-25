package com.trionesdev.csi.api.oss;

import com.trionesdev.csi.api.oss.request.*;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;

public interface OssTemplate {

    OssGetObjectResponse getObject(OssGetObjectRequest request);

    OssPutObjectResponse putObject(OssPutObjectRequest request);

    void removeObjects(OssRemoveObjectsRequest request);

    String getObjectUrl(OssGetObjectUrlRequest request);

    String getObjectName(OssGetObjectNameRequest request);

    OssListObjectsResponse listObjects(OssListObjectsRequest request);
}
