package com.trionesdev.csi.tencentcloud.ses;

import com.google.gson.Gson;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ses.v20201002.SesClient;
import com.tencentcloudapi.ses.v20201002.models.SendEmailRequest;
import com.tencentcloudapi.ses.v20201002.models.SendEmailResponse;
import com.tencentcloudapi.ses.v20201002.models.Template;
import com.trionesdev.csi.api.ses.SesException;
import com.trionesdev.csi.api.ses.SesTemplate;
import com.trionesdev.csi.api.ses.SesVariable;
import com.trionesdev.csi.api.ses.request.SesSendEmailRequest;
import com.trionesdev.csi.api.ses.response.SesSendEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TencentCloudSes implements SesTemplate {
    private final TencentCloudSesConfig config;
    private final SesClient sesClient;

    public TencentCloudSes(TencentCloudSesConfig config, SesClient sesClient) {
        this.config = config;
        this.sesClient = sesClient;
    }

    @Override
    public String template(String code) {
        return config.getTemplateCodes().get(code);
    }

    public SesSendEmailResponse sendEmail(SesSendEmailRequest request) {
        try {
            SendEmailRequest sendEmailRequest = new SendEmailRequest();
            sendEmailRequest.setFromEmailAddress(request.getFromAddress());
            sendEmailRequest.setSubject(request.getSubject());
            if (CollectionUtils.isNotEmpty(request.getDestinations())) {
                sendEmailRequest.setDestination(ListUtils.emptyIfNull(request.getDestinations()).toArray(new String[0]));
            }
            Template template = new Template();
            if (StringUtils.isNotBlank(request.getTemplateCode())) {
                template.setTemplateID(Long.valueOf(request.getTemplateCode()));
            }
            if (CollectionUtils.isNotEmpty(request.getVariables())) {
                Map<String, Object> templateData = request.getVariables().stream().collect(Collectors.toMap(SesVariable::getKey, SesVariable::getValue));
                template.setTemplateData(new Gson().toJson(templateData));
            }
            sendEmailRequest.setTemplate(template);
            SendEmailResponse response = sesClient.SendEmail(sendEmailRequest);
            return SesSendEmailResponse.builder().messageId(response.getMessageId()).requestId(response.getRequestId()).build();
        } catch (Exception e) {
            throw new SesException(e.getMessage(), e);
        }
    }
}
