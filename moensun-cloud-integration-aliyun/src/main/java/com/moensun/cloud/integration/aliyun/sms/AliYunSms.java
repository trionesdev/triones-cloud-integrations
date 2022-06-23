package com.moensun.cloud.integration.aliyun.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.google.gson.Gson;
import com.moensun.cloud.integration.api.sms.SmsException;
import com.moensun.cloud.integration.api.sms.SmsTemplate;
import com.moensun.cloud.integration.api.sms.request.SmsSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AliYunSms implements SmsTemplate {

    private final AliYunSmsConfig aliYunSmsProperties;

    private final IAcsClient client;

    public AliYunSms(IAcsClient client, AliYunSmsConfig aliYunSmsProperties) {
        this.client = client;
        this.aliYunSmsProperties = aliYunSmsProperties;
    }

    @Override
    public String template(String code) {
        return aliYunSmsProperties.getTemplateCodes().get(code);
    }

    @Override
    public void send(SmsSendRequest sendRequest) {
        String signName = StringUtils.isNotBlank(sendRequest.getSignName()) ? sendRequest.getSignName() : aliYunSmsProperties.getSignName();
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", aliYunSmsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", sendRequest.getPhoneNumbers());
        request.putQueryParameter("SignName", signName);
        if (StringUtils.isNoneBlank(sendRequest.getTemplateCode())) {
            request.putQueryParameter("TemplateCode", sendRequest.getTemplateCode());
        }
        if (CollectionUtils.isNotEmpty(sendRequest.getParams())) {
            Map<String, String> paramsMap = new HashMap<>();
            sendRequest.getParams().forEach(smsParam -> paramsMap.put(smsParam.getKey(), smsParam.getValue()));
            request.putQueryParameter("TemplateParam", new Gson().toJson(paramsMap));
        }
        try {
            CommonResponse response = client.getCommonResponse(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SmsException(e);
        }
    }


}
