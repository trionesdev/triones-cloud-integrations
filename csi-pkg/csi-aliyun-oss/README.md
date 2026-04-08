# Triones CSI Aliyun OSS

This module provides an implementation of the Triones CSI OSS (Object Storage Service) API for Alibaba Cloud OSS (Aliyun OSS).

## Dependencies

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.trionesdev.csi</groupId>
    <artifactId>csi-aliyun-oss</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

## Configuration

You need to provide the `AliYunOssConfig` to initialize the `AliYunOSS` client.

### Configuration Properties

| Property | Description | Required |
| --- | --- | --- |
| `accessKeyId` | Aliyun Access Key ID | Yes |
| `accessKeySecret` | Aliyun Access Key Secret | Yes |
| `endpoint` | OSS Endpoint (e.g., `oss-cn-hangzhou.aliyuncs.com`) | Yes |
| `bucket` | Default Bucket Name | No (Can be overridden in requests) |
| `urlPrefix` | URL Prefix for accessing objects (e.g., `https://my-bucket.oss-cn-hangzhou.aliyuncs.com/`) | No |
| `multi` | Enable multi-bucket support (Boolean) | No |

### Example Configuration

```java
AliYunOssConfig config = AliYunOssConfig.builder()
        .accessKeyId("your-access-key-id")
        .accessKeySecret("your-access-key-secret")
        .endpoint("oss-cn-hangzhou.aliyuncs.com")
        .bucket("your-default-bucket")
        .urlPrefix("https://your-domain.com/")
        .build();

AliYunOSS ossClient = new AliYunOSS(config);
```

## Usage

This module implements the `OssTemplate` interface.

### Upload Object

```java
OssPutObjectRequest request = OssPutObjectRequest.builder()
        .bucketName("your-bucket") // Optional if default bucket is set
        .objectName("path/to/file.txt")
        .inputStream(inputStream)
        .build();

OssPutObjectResponse response = ossClient.putObject(request);
System.out.println("Uploaded URL: " + response.getUrl());
```

### List Objects

```java
OssListObjectsRequest request = OssListObjectsRequest.builder()
        .bucketName("your-bucket")
        .prefix("path/to/")
        .build();

OssListObjectsResponse response = ossClient.listObjects(request);
response.getObjectSummaries().forEach(summary -> {
    System.out.println("Object: " + summary.getKey());
});
```

### Check Object Existence

```java
OssObjectExistRequest request = OssObjectExistRequest.builder()
        .bucketName("your-bucket")
        .objectName("path/to/file.txt")
        .build();

Boolean exists = ossClient.objectExist(request);
```

## Features

- **Put Object**: Upload files to OSS.
- **List Objects**: List files in a bucket with prefix support.
- **Object Existence**: Check if a file exists.
- **URL Generation**: Generate access URLs for objects.

## Notes

- `getObject` and `removeObjects` methods are currently not implemented.
