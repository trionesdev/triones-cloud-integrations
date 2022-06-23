package com.moensun.cloud.integration.huaweicloud.sms;

import lombok.Data;

@Data
public class HuaweiCloudSmsRequest {
    private String sender;
    private String receiver;
    private String templateId;
    private String templateParams;
    private String statusCallBack;
    private String signature;
}
