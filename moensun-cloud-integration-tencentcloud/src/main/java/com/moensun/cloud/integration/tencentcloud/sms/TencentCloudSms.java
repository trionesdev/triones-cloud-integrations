package com.moensun.cloud.integration.tencentcloud.sms;

import com.moensun.cloud.integration.product.sms.SmsException;
import com.moensun.cloud.integration.product.sms.SmsParam;
import com.moensun.cloud.integration.product.sms.SmsTemplate;
import com.moensun.cloud.integration.product.sms.request.SmsSendRequest;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class TencentCloudSms implements SmsTemplate {
    private final TencentCloudSmsConfig smsProperties;
    private final SmsClient smsClient;

    public TencentCloudSms(TencentCloudSmsConfig smsProperties, SmsClient smsClient) {
        this.smsProperties = smsProperties;
        this.smsClient = smsClient;
    }

    @Override
    public String template(String code) {
        return smsProperties.getTemplateCodes().get(code);
    }

    @Override
    public void send(SmsSendRequest sendRequest) {
        try {
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppId(smsProperties.getSdkAppId());
            req.setSignName(smsProperties.getSignName());
            String senderid = "";
            req.setSenderId(senderid);
            String sessionContext = "xxx";
            req.setSessionContext(sessionContext);
            String extendCode = "";
            req.setExtendCode(extendCode);
            req.setTemplateId(sendRequest.getTemplateCode());
            String[] phoneNumberSet = sendRequest.getPhoneNumbers().split(",");
            req.setPhoneNumberSet(phoneNumberSet);
            if (CollectionUtils.isNotEmpty(sendRequest.getParams())) {
                List<String> strings = new ArrayList<>();
                sendRequest.getParams().stream().sorted(Comparator.comparing(SmsParam::getIndex)).forEach(smsParam -> strings.add(smsParam.getValue()));
                req.setTemplateParamSet(strings.toArray(new String[strings.size()]));
            }
            SendSmsResponse res = smsClient.SendSms(req);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new SmsException(ex);
        }
    }
}
