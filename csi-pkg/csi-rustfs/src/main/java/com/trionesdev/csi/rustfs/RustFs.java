package com.trionesdev.csi.rustfs;

import com.trionesdev.csi.api.oss.OssException;
import com.trionesdev.csi.api.oss.OssTemplate;
import com.trionesdev.csi.api.oss.request.*;
import com.trionesdev.csi.api.oss.response.OssGetObjectResponse;
import com.trionesdev.csi.api.oss.response.OssListObjectsResponse;
import com.trionesdev.csi.api.oss.response.OssPutObjectResponse;
import com.trionesdev.csi.api.oss.util.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RustFs implements OssTemplate {
    private final S3Client s3Client;
    private final RustFsConfig config;

    public RustFs(RustFsConfig config) {
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(config.getEndpoint()))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(config.getAccessKeyId(), config.getSecretAccessKey())
                        )
                )
                .forcePathStyle(true)
                .build();
        this.config = config;
    }

    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(request.getObjectName())
                .build();
        try {
            ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(getObjectRequest);
            GetObjectResponse response = stream.response();
            return OssGetObjectResponse.builder()
                    .in(stream)
                    .contentType(response.contentType())
                    .contentLength(response.contentLength())
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new OssException(ex);
        }
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(request.getObjectName())
                .contentType(request.getContentType())
                .build();
        try {
            long contentLength = request.getInputStream().available();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(request.getInputStream(), contentLength));
            return OssPutObjectResponse.builder()
                    .url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix + "/" + bucketName))
                    .build();
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
        List<ObjectIdentifier> objectIdentifiers = request.getObjectNames().stream()
                .map(name -> ObjectIdentifier.builder().key(name).build())
                .collect(Collectors.toList());

        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder().objects(objectIdentifiers).build())
                .build();

        try {
            DeleteObjectsResponse response = s3Client.deleteObjects(deleteRequest);
            if (CollectionUtils.isNotEmpty(response.errors())) {
                for (S3Error error : response.errors()) {
                    log.error("Error in deleting object {}: {}", error.key(), error.message());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
    }

    @Override
    public String getObjectUrl(OssGetObjectUrlRequest request) {
        return OssUtils.joinPrefix(request.getObjectName(), rustFsUrlPrefix(request.getUrlPrefix(), request.getBucketName()));
    }

    @Override
    public String getObjectName(OssGetObjectNameRequest request) {
        return OssUtils.removePrefix(request.getObjectUrl(), rustFsUrlPrefix(request.getUrlPrefix(), request.getBucketName()));
    }

    @Override
    public OssListObjectsResponse listObjects(OssListObjectsRequest request) {
        String bucketName = bucketName(request.getBucketName());
        ListObjectsV2Request.Builder builder = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(request.getPrefix())
                .startAfter(request.getStartAfter());
        if (request.getMaxKeys() != null) {
            builder.maxKeys(request.getMaxKeys());
        }
        if (StringUtils.isNotBlank(request.getDelimiter())) {
            builder.delimiter(request.getDelimiter());
        }

        try {
            ListObjectsV2Response response = s3Client.listObjectsV2(builder.build());
            List<OssListObjectsResponse.ObjectSummary> objectSummaries = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(response.contents())) {
                for (S3Object s3Object : response.contents()) {
                    objectSummaries.add(OssListObjectsResponse.ObjectSummary.builder()
                            .objectName(s3Object.key())
                            .eTag(s3Object.eTag())
                            .size(s3Object.size())
                            .lastModified(s3Object.lastModified())
                            .storageClass(s3Object.storageClassAsString())
                            .build());
                }
            }

            if (CollectionUtils.isNotEmpty(response.commonPrefixes())) {
                for (CommonPrefix commonPrefix : response.commonPrefixes()) {
                    objectSummaries.add(OssListObjectsResponse.ObjectSummary.builder()
                            .objectName(commonPrefix.prefix())
                            .build());
                }
            }

            return OssListObjectsResponse.builder().objectSummaries(objectSummaries).build();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new OssException(ex);
        }
    }

    @Override
    public Boolean objectExist(OssObjectExistRequest request) {
        String bucketName = bucketName(request.getBucketName());
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(request.getObjectName())
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
    }

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : config.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : config.getUrlPrefix();
    }

    private String rustFsUrlPrefix(String urlPrefix, String bucketName) {
        return urlPrefix(urlPrefix) + "/" + bucketName(bucketName);
    }
}
