package com.moensun.cloud.integration.api.sms;

import com.moensun.cloud.integration.api.sms.request.SmsSendRequest;

/**
 * 短信平台
 */
public interface SmsTemplate {

    String template(String code);

    void send(SmsSendRequest request);

}
