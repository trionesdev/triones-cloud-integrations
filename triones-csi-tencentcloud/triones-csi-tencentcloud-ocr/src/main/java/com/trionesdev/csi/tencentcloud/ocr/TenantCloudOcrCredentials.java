package com.trionesdev.csi.tencentcloud.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TenantCloudOcrCredentials {
    private String secretId;
    private String secretKey;
    private String region;
}
