package com.moensun.cloud.integration.api.oss.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BucketInfo {
    private String bucketName;
    private String urlPrefix;
}
