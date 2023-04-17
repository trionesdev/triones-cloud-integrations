package com.moensun.cloud.integration.aliyun.sms;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import com.moensun.cloud.integration.api.sms.SmsException;
import com.moensun.cloud.integration.api.sms.SmsTemplate;
import com.moensun.cloud.integration.api.sms.request.SmsSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class AliYunSms implements SmsTemplate {

    private final AliYunSmsConfig aliYunSmsProperties;

    private final Client client;

    public AliYunSms(Client client, AliYunSmsConfig aliYunSmsProperties) {
        this.client = client;
        this.aliYunSmsProperties = aliYunSmsProperties;
    }

    @Override
    public void addTemplates(Map<String, String> templates) {
        Map<String, String> templatesMap = new HashMap<>();
        templatesMap.putAll(templates);
        templatesMap.putAll(aliYunSmsProperties.getTemplateCodes());
        aliYunSmsProperties.setTemplateCodes(templatesMap);
    }

    @Override
    public String template(String code) {
        return aliYunSmsProperties.getTemplateCodes().get(code);
    }

    @Override
    public void send(SmsSendRequest sendRequest) {
        String signName = StringUtils.isNotBlank(sendRequest.getSignName()) ? sendRequest.getSignName() : aliYunSmsProperties.getSignName();

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(sendRequest.getPhoneNumbers())
                .setSignName(signName);
        if (StringUtils.isNoneBlank(sendRequest.getTemplateCode())) {
            sendSmsRequest.setTemplateCode(sendRequest.getTemplateCode());
        }
        if (CollectionUtils.isNotEmpty(sendRequest.getParams())) {
            Map<String, String> paramsMap = new HashMap<>();
            sendRequest.getParams().forEach(smsParam -> paramsMap.put(smsParam.getKey(), smsParam.getValue()));
            sendSmsRequest.setTemplateParam(new Gson().toJson(paramsMap));
        }
        try {
            SendSmsResponse sendResponse = client.sendSms(sendSmsRequest);
            if (!Objects.equals("OK", sendResponse.getBody().getCode())) {
                throw new SmsException(sendResponse.getBody().getMessage());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new SmsException(ex);
        }

    }


}
