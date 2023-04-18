package com.moensun.csi.minio;


import com.moensun.csi.api.oss.OssException;
import com.moensun.csi.api.oss.OssTemplate;
import com.moensun.csi.api.oss.request.*;
import com.moensun.csi.api.oss.response.OssGetObjectResponse;
import com.moensun.csi.api.oss.response.OssListObjectsResponse;
import com.moensun.csi.api.oss.response.OssPutObjectResponse;
import com.moensun.commons.core.util.FilePathUtils;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Minio implements OssTemplate {

    private final MinioClient minioClient;
    private final MinioConfig minioProperties;

    public Minio(MinioClient minioClient, MinioConfig minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(request.getObjectName())
                .build();
        try {
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            return OssGetObjectResponse.builder().in(inputStream).build();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new OssException(ex);
        }
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(request.getObjectName())
                    .stream(request.getInputStream(), request.getInputStream().available(), -1)
                    .contentType(request.getContentType())
                    .build();
            ObjectWriteResponse response = minioClient.putObject(putObjectArgs);
            return OssPutObjectResponse.builder().url(FilePathUtils.joinPrefix(request.getObjectName(), urlPrefix + "/" + bucketName)).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
    }

    @Override
    public void removeObjects(OssRemoveObjectsRequest request) {
        if (CollectionUtils.isEmpty(request.getObjectNames())) {
            return;
        }

        String bucketName = bucketName(request.getBucketName());
        List<DeleteObject> deleteObjects = request.getObjectNames()
                .stream()
                .map(DeleteObject::new)
                .collect(Collectors.toList());

        RemoveObjectsArgs args = RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(deleteObjects)
                .build();

        try {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(args);
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("Error in deleting object " + error.objectName() + "; " + error.message());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
    }

    @Override
    public String getObjectUrl(OssGetObjectUrlRequest request) {
        return FilePathUtils.joinPrefix(request.getObjectName(), minioUrlPrefix(request.getUrlPrefix(), request.getBucketName()));
    }

    @Override
    public String getObjectName(OssGetObjectNameRequest request) {
        return FilePathUtils.removePrefix(request.getObjectUrl(), minioUrlPrefix(request.getUrlPrefix(), request.getBucketName()));
    }

    @Override
    public OssListObjectsResponse listObjects(OssListObjectsRequest request) {
        String bucketName = bucketName(request.getBucketName());
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucketName)
                .startAfter(request.getStartAfter())
                .prefix(request.getPrefix())
                .maxKeys(request.getMaxKeys())
                .build();
        Iterable<Result<Item>> results = minioClient.listObjects(args);
        List<OssListObjectsResponse.ObjectSummary> objectSummaries = new ArrayList<>();
        Iterator<Result<Item>> resultIterator = results.iterator();
        while (resultIterator.hasNext()) {
            Result<Item> resultItem = resultIterator.next();
            try {
                Item item = resultItem.get();
                OssListObjectsResponse.ObjectSummary objectSummary;
                if (item.isDir()) {
                    objectSummary = OssListObjectsResponse.ObjectSummary.builder()
                            .objectName(item.objectName())
                            .build();
                } else {
                    objectSummary = OssListObjectsResponse.ObjectSummary.builder()
                            .objectName(item.objectName()).eTag(item.etag()).size(item.size())
                            .lastModified(Optional.ofNullable(item.lastModified()).map(ChronoZonedDateTime::toInstant).orElse(null))
                            .storageClass(item.storageClass())
                            .build();
                }
                objectSummaries.add(objectSummary);
            } catch (Exception ex) {
                throw new OssException(ex);
            }
        }
        return OssListObjectsResponse.builder().objectSummaries(objectSummaries).build();
    }

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : minioProperties.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : minioProperties.getUrlPrefix();
    }

    private String minioUrlPrefix(String urlPrefix, String bucketName) {
        return urlPrefix(urlPrefix) + "/" + bucketName(bucketName);
    }

}
