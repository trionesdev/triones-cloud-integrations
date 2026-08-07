package com.trionesdev.csi.localstorage;

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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class LocalStorage implements OssTemplate {
    private final LocalStorageConfig config;

    public LocalStorage(LocalStorageConfig config) {
        this.config = config;
    }

    @Override
    public OssGetObjectResponse getObject(OssGetObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        try {
            Path target = objectPath(bucketName, request.getObjectName());
            if (!Files.isRegularFile(target)) {
                throw new OssException("object not found: " + request.getObjectName());
            }
            InputStream inputStream = Files.newInputStream(target);
            String contentType = Files.probeContentType(target);
            return OssGetObjectResponse.builder().in(inputStream).contentType(contentType).contentLength(Files.size(target)).build();
        } catch (OssException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
    }

    @Override
    public OssPutObjectResponse putObject(OssPutObjectRequest request) {
        String bucketName = bucketName(request.getBucketName());
        String urlPrefix = urlPrefix(request.getUrlPrefix());
        try {
            Path target = objectPath(bucketName, request.getObjectName());
            Path parent = target.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.copy(request.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return OssPutObjectResponse.builder()
                    .url(OssUtils.joinPrefix(request.getObjectName(), urlPrefix))
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
        try {
            for (String objectName : request.getObjectNames()) {
                Path target = objectPath(bucketName, objectName);
                Files.deleteIfExists(target);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
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
        String bucketName = bucketName(request.getBucketName());
        Path bucketRoot = bucketPath(bucketName);
        if (!Files.isDirectory(bucketRoot)) {
            return OssListObjectsResponse.builder().objectSummaries(Collections.emptyList()).build();
        }

        String prefix = StringUtils.defaultString(request.getPrefix());
        String delimiter = request.getDelimiter();
        String startAfter = request.getStartAfter();
        Integer maxKeys = request.getMaxKeys();

        try {
            List<OssListObjectsResponse.ObjectSummary> objectSummaries;
            if (StringUtils.isNotBlank(delimiter)) {
                objectSummaries = listWithDelimiter(bucketRoot, bucketName, prefix, delimiter);
            } else {
                objectSummaries = listAllObjects(bucketRoot, bucketName, prefix);
            }

            objectSummaries = objectSummaries.stream()
                    .sorted(Comparator.comparing(OssListObjectsResponse.ObjectSummary::getObjectName))
                    .filter(summary -> StringUtils.isBlank(startAfter) || summary.getObjectName().compareTo(startAfter) > 0)
                    .limit(maxKeys == null || maxKeys <= 0 ? Long.MAX_VALUE : maxKeys)
                    .collect(Collectors.toList());

            return OssListObjectsResponse.builder().objectSummaries(objectSummaries).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new OssException(e);
        }
    }

    @Override
    public Boolean objectExist(OssObjectExistRequest request) {
        String bucketName = bucketName(request.getBucketName());
        Path target = objectPath(bucketName, request.getObjectName());
        return Files.isRegularFile(target);
    }

    private List<OssListObjectsResponse.ObjectSummary> listAllObjects(Path bucketRoot, String bucketName, String prefix) throws IOException {
        List<OssListObjectsResponse.ObjectSummary> objectSummaries = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(bucketRoot)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                String objectName = toObjectName(bucketRoot, path);
                if (!objectName.startsWith(prefix)) {
                    return;
                }
                objectSummaries.add(toObjectSummary(bucketName, objectName, path));
            });
        }
        return objectSummaries;
    }

    private List<OssListObjectsResponse.ObjectSummary> listWithDelimiter(Path bucketRoot, String bucketName, String prefix, String delimiter) throws IOException {
        Path searchRoot = StringUtils.isBlank(prefix) ? bucketRoot : bucketRoot.resolve(prefix);
        if (!Files.isDirectory(searchRoot)) {
            return Collections.emptyList();
        }

        List<OssListObjectsResponse.ObjectSummary> objectSummaries = new ArrayList<>();
        try (Stream<Path> stream = Files.list(searchRoot)) {
            stream.forEach(path -> {
                String objectName = toObjectName(bucketRoot, path);
                if (!objectName.startsWith(prefix)) {
                    return;
                }
                if (Files.isDirectory(path)) {
                    String commonPrefix = StringUtils.appendIfMissing(objectName, delimiter);
                    objectSummaries.add(OssListObjectsResponse.ObjectSummary.builder()
                            .bucketName(bucketName)
                            .objectName(commonPrefix)
                            .key(commonPrefix)
                            .build());
                } else if (Files.isRegularFile(path)) {
                    objectSummaries.add(toObjectSummary(bucketName, objectName, path));
                }
            });
        }
        return objectSummaries;
    }

    private OssListObjectsResponse.ObjectSummary toObjectSummary(String bucketName, String objectName, Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            return OssListObjectsResponse.ObjectSummary.builder()
                    .bucketName(bucketName)
                    .objectName(objectName)
                    .key(objectName)
                    .size(attrs.size())
                    .lastModified(attrs.lastModifiedTime().toInstant())
                    .build();
        } catch (IOException e) {
            throw new OssException(e);
        }
    }

    private String toObjectName(Path bucketRoot, Path path) {
        return bucketRoot.relativize(path).toString().replace('\\', '/');
    }

    private Path objectPath(String bucketName, String objectName) {
        if (StringUtils.isBlank(bucketName)) {
            return Paths.get(config.getDir(), objectName);
        }
        return Paths.get(config.getDir(), bucketName, objectName);
    }

    private Path bucketPath(String bucketName) {
        if (StringUtils.isBlank(bucketName)) {
            return Paths.get(config.getDir());
        }
        return Paths.get(config.getDir(), bucketName);
    }

    private String bucketName(String bucketName) {
        return StringUtils.isNotBlank(bucketName) ? bucketName : config.getBucket();
    }

    private String urlPrefix(String urlPrefix) {
        return StringUtils.isNotBlank(urlPrefix) ? urlPrefix : config.getUrlPrefix();
    }
}
