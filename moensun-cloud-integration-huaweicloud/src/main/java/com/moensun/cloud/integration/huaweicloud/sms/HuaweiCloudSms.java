package com.moensun.cloud.integration.huaweicloud.sms;

import com.moensun.cloud.integration.api.sms.SmsTemplate;
import com.moensun.cloud.integration.api.sms.request.SmsSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HuaweiCloudSms implements SmsTemplate {


    private final HuaweiCloudSmsConfig smsConfig;

    public HuaweiCloudSms(HuaweiCloudSmsConfig smsConfig) {
        this.smsConfig = smsConfig;
    }


    @Override
    public String template(String code) {
        return smsConfig.getTemplateCodes().get(code);
    }

    @Override
    public void send(SmsSendRequest request) {
        String signName = StringUtils.isNotBlank(request.getSignName()) ? request.getSignName() : smsConfig.getSignName();
    }

}
