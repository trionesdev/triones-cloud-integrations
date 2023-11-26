package com.moensun.csi.tencentcloud.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TencentCloudSmsConfig implements Serializable {
    private static final long serialVersionUID = 9107196948953793172L;
    private String sdkAppId;
    private String signName;
    private Map<String,String> templateCodes;
}
