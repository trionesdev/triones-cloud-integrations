package com.trionesdev.csi.huaweicloud.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.trionesdev.csi.api.sms.SmsException;
import com.trionesdev.csi.api.sms.SmsVariable;
import com.trionesdev.csi.api.sms.SmsTemplate;
import com.trionesdev.csi.api.sms.request.SmsSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class HuaweiCloudSms implements SmsTemplate {

    private final HuaweiCloudSmsClient smsClient;
    private final HuaweiCloudSmsConfig smsConfig;

    public HuaweiCloudSms( HuaweiCloudSmsConfig smsConfig) {
        this.smsClient = new HuaweiCloudSmsClient(smsConfig.getAppKey(), smsConfig.getAppSecret());
        this.smsConfig = smsConfig;
    }

    @Override
    public void addTemplates(Map<String, String> templates) {
        Map<String, String> templatesMap = new HashMap<>();
        templatesMap.putAll(templates);
        templatesMap.putAll(smsConfig.getTemplateCodes());
        smsConfig.setTemplateCodes(templatesMap);
    }

    @Override
    public String template(String code) {
        return smsConfig.getTemplateCodes().get(code);
    }

    @Override
    public void send(SmsSendRequest sendRequest) {
        String signName = StringUtils.isNotBlank(sendRequest.getSignName()) ? sendRequest.getSignName() : smsConfig.getSignName();
        String regionId = StringUtils.isNotBlank(sendRequest.getRegionId()) ? sendRequest.getRegionId() : smsConfig.getRegionId();
        String sender = Optional.ofNullable(sendRequest.getExtra()).map(extra -> extra.get("sender")).orElse(smsConfig.getSender());
        List<String> params = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(sendRequest.getVariables())) {
            params = sendRequest.getVariables().stream().sorted(Comparator.comparing(SmsVariable::getIndex)).map(SmsVariable::getValue).collect(Collectors.toList());
        }
        try {
            HuaweiCloudSmsRequest request = HuaweiCloudSmsRequest.builder()
                    .sender(sender)
                    .templateId(sendRequest.getTemplateCode())
                    .receiver(StringUtils.join(sendRequest.getPhoneNumbers(), ","))
                    .templateParams(new ObjectMapper().writeValueAsString(params))
                    .signature(signName)
                    .regionId(regionId)
                    .build();
            this.smsClient.request(request);
        } catch (Exception ex) {
            throw new SmsException(ex);
        }
    }

}
