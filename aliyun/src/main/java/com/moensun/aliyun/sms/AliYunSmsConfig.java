package com.moensun.aliyun.sms;

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
public class AliYunSmsConfig implements Serializable {
    private static final long serialVersionUID = 8551713446967178352L;
    private String regionId;
    private String endpoint = "dysmsapi.aliyuncs.com";
    private String signName;
    private Map<String,String> templateCodes;
}
