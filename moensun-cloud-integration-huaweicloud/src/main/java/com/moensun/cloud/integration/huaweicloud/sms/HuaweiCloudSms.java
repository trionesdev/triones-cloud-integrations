package com.moensun.cloud.integration.huaweicloud.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.moensun.cloud.integration.api.sms.SmsException;
import com.moensun.cloud.integration.api.sms.SmsParam;
import com.moensun.cloud.integration.api.sms.SmsTemplate;
import com.moensun.cloud.integration.api.sms.request.SmsSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class HuaweiCloudSms implements SmsTemplate {

    private final HuaweiCloudSmsClient smsClient;
    private final HuaweiCloudSmsConfig smsConfig;

    public HuaweiCloudSms(HuaweiCloudSmsClient cloudSmsClient ,HuaweiCloudSmsConfig smsConfig) {
        this.smsClient = cloudSmsClient;
        this.smsConfig = smsConfig;
    }


    @Override
    public String template(String code) {
        return smsConfig.getTemplateCodes().get(code);
    }

    @Override
    public void send(SmsSendRequest sendRequest) {
        String signName = StringUtils.isNotBlank(sendRequest.getSignName()) ? sendRequest.getSignName() : smsConfig.getSignName();
        String regionId = StringUtils.isNotBlank(sendRequest.getRegionId())? sendRequest.getRegionId() : smsConfig.getRegionId();
        List<String> params = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(sendRequest.getParams())){
            params = sendRequest.getParams().stream().sorted(Comparator.comparing(SmsParam::getIndex)).map(SmsParam::getValue).collect(Collectors.toList());
        }
        try{
            HuaweiCloudSmsRequest request = HuaweiCloudSmsRequest.builder()
                    .sender(sendRequest.getSender())
                    .templateId(sendRequest.getTemplateCode())
                    .receiver(sendRequest.getPhoneNumbers())
                    .templateParams(new ObjectMapper().writeValueAsString(params))
                    .signature(signName)
                    .regionId(regionId)
                    .build();
            this.smsClient.request(request);
        }catch (Exception ex){
            throw new SmsException(ex);
        }
    }

}
