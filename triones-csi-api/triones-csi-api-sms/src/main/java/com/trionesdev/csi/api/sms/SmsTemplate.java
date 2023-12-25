package com.trionesdev.csi.api.sms;

import com.trionesdev.csi.api.sms.request.SmsSendRequest;

import java.util.Map;

/**
 * 短信平台
 */
public interface SmsTemplate {

    void addTemplates(Map<String, String> templates);

    String template(String code);

    void send(SmsSendRequest request);

}
