package com.moensun.huaweicloud.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HuaweiCloudSmsRequest {
    private String sender;
    private String receiver;
    private String templateId;
    private String templateParams;
    private String statusCallBack;
    private String signature;
    private String regionId;
}
