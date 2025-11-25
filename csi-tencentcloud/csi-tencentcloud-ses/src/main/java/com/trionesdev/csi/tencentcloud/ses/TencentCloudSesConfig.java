package com.trionesdev.csi.tencentcloud.ses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TencentCloudSesConfig extends TencentCloudSesCredentials {
    private String fromAddress;
    private String replyAddress;
    private Map<String, String> templateCodes;
}
