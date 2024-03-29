package com.trionesdev.csi.tencentcloud.sms;

import cn.hutool.core.util.ArrayUtil;
import com.trionesdev.csi.api.sms.SmsException;
import com.trionesdev.csi.api.sms.SmsParam;
import com.trionesdev.csi.api.sms.SmsTemplate;
import com.trionesdev.csi.api.sms.request.SmsSendRequest;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

@Slf4j
public class TencentCloudSms implements SmsTemplate {
    private final TencentCloudSmsConfig smsProperties;
    private final SmsClient smsClient;

    public TencentCloudSms(TencentCloudSmsConfig smsProperties, SmsClient smsClient) {
        this.smsProperties = smsProperties;
        this.smsClient = smsClient;
    }

    @Override
    public void addTemplates(Map<String, String> templates) {
        Map<String, String> templatesMap = new HashMap<>();
        templatesMap.putAll(templates);
        templatesMap.putAll(smsProperties.getTemplateCodes());
        smsProperties.setTemplateCodes(templatesMap);
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
            if (ArrayUtil.isNotEmpty(res.getSendStatusSet())) {
                Arrays.stream(res.getSendStatusSet()).forEach(sendStatus -> {
                    if (!Objects.equals("Ok", sendStatus.getCode())) {
                        log.error("短信发送失败，错误码：{}，错误信息：{}", sendStatus.getCode(), sendStatus.getMessage());
                        throw new SmsException(String.format("短信发送失败，错误码：%s，错误信息：%s", sendStatus.getCode(), sendStatus.getMessage()));
                    }
                });
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new SmsException(ex);
        }
    }
}
