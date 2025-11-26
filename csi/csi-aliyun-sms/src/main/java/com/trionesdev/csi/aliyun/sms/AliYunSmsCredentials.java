package com.trionesdev.csi.aliyun.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AliYunSmsCredentials implements Serializable {
    private String accessKeyId;
    private String accessKeySecret;
    private String regionId;
    private String endpoint = "dysmsapi.aliyuncs.com";
    private String signName;
}
