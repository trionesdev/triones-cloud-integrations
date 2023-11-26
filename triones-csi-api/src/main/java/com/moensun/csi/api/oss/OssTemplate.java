package com.moensun.csi.api.oss;

import com.moensun.csi.api.oss.request.*;
import com.moensun.csi.api.oss.response.OssGetObjectResponse;
import com.moensun.csi.api.oss.response.OssListObjectsResponse;
import com.moensun.csi.api.oss.response.OssPutObjectResponse;

public interface OssTemplate {

    OssGetObjectResponse getObject(OssGetObjectRequest request);

    OssPutObjectResponse putObject(OssPutObjectRequest request);

    void removeObjects(OssRemoveObjectsRequest request);

    String getObjectUrl(OssGetObjectUrlRequest request);

    String getObjectName(OssGetObjectNameRequest request);

    OssListObjectsResponse listObjects(OssListObjectsRequest request);
}
