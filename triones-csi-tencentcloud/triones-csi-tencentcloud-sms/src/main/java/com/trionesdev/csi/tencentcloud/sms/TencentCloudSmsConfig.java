package com.trionesdev.csi.tencentcloud.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TencentCloudSmsConfig extends TenantCloudSmsCredentials implements Serializable {
    private static final long serialVersionUID = 9107196948953793172L;
    private String signName;
    private Map<String,String> templateCodes;
}
