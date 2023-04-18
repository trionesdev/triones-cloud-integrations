package com.moensun.huaweicloud.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HuaweiCloudSmsConfig implements Serializable {
    private String regionId;
    private String signName;
    private Map<String,String> templateCodes;
}
