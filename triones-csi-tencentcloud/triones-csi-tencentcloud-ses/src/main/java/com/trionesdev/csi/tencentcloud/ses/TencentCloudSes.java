package com.trionesdev.csi.tencentcloud.ses;

import com.google.gson.Gson;
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
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
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
        return Optional.ofNullable(config.getTemplateCodes()).map(m -> m.get(code)).orElse(null);
    }

    public SesSendEmailResponse sendEmail(SesSendEmailRequest request) {
        try {
            SendEmailRequest sendEmailRequest = new SendEmailRequest();
            if (StringUtils.isNotBlank(request.getFromAddress())){
                sendEmailRequest.setFromEmailAddress(request.getFromAddress());
            }else {
                sendEmailRequest.setFromEmailAddress(config.getFromAddress());
            }
            if (StringUtils.isNotBlank(request.getReplyAddress())){
                sendEmailRequest.setReplyToAddresses(request.getReplyAddress());
            }else {
                sendEmailRequest.setReplyToAddresses(config.getReplyAddress());
            }
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
