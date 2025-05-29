package com.trionesdev.csi.aliyun.oss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AliYunOssCredentials implements Serializable {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucket;
    private String urlPrefix;
}
