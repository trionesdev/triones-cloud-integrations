package com.trionesdev.csi.huaweicloud.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HuaweiCloudSmsCredentials implements Serializable {
    private String appKey;
    private String appSecret;
    private String regionId;
    private String signName;
}
