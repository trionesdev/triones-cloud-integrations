package com.trionesdev.csi.tencentcloud.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TenantCloudSmsCredentials {
    private String secretId;
    private String secretKey;
    private String sdkAppId;
}
