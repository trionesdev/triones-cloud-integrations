package com.trionesdev.csi.huaweicloud.obs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HuaweiCloudObsCredentials {
    private String accessKeyId;
    private String secretAccessKey;
    private String endpoint;
    private String bucket;
    private String urlPrefix;
}
