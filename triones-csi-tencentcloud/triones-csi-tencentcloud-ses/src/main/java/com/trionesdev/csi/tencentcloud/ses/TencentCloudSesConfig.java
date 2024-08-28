package com.trionesdev.csi.tencentcloud.ses;

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
public class TencentCloudSesConfig implements Serializable {
    private String region;
    private String fromAddress;
    private String replyAddress;
    private Map<String,String> templateCodes;
}
