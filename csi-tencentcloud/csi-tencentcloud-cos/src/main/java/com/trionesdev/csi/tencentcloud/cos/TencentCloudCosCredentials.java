package com.trionesdev.csi.tencentcloud.cos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TencentCloudCosCredentials implements Serializable {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private String urlPrefix;
}
