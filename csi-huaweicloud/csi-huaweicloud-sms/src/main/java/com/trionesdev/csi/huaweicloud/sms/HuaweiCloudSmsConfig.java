package com.trionesdev.csi.huaweicloud.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HuaweiCloudSmsConfig extends HuaweiCloudSmsCredentials {
    private String regionId;
    private String sender;
    private Map<String,String> templateCodes;
}
