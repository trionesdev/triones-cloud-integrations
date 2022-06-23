package com.moensun.cloud.integration.api.oss.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OssPutObjectRequest extends BucketInfo {
    private String objectName;
    private InputStream inputStream;
    private String contentType;
}
