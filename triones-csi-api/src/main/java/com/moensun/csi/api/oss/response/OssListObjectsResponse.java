package com.moensun.csi.api.oss.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OssListObjectsResponse {

    private List<ObjectSummary> objectSummaries;

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ObjectSummary {
        private String bucketName;
        private String objectName;
        private String key;
        private String eTag;
        private long size;
        private Instant lastModified;
        private String storageClass;
        private String type;
    }
}
