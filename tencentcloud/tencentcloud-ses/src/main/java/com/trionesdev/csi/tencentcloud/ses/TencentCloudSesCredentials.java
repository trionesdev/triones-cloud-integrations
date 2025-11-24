package com.trionesdev.csi.tencentcloud.ses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TencentCloudSesCredentials implements Serializable {
    private String secretId;
    private String secretKey;
    private String endpoint;
    private String region;
}
