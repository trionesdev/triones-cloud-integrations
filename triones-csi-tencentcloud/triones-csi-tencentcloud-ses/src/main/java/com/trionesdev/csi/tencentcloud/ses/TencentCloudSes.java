package com.trionesdev.csi.tencentcloud.ses;

import com.tencentcloudapi.ses.v20201002.SesClient;
import com.trionesdev.csi.api.ses.SesTemplate;
import com.trionesdev.csi.api.ses.request.SesSendEmailRequest;
import com.trionesdev.csi.api.ses.response.SesSendEmailResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TencentCloudSes implements SesTemplate {
    private final TencentCloudSesConfig config;
    private final SesClient sesClient;

    public TencentCloudSes(TencentCloudSesConfig config, SesClient sesClient) {
        this.config = config;
        this.sesClient = sesClient;
    }

    public SesSendEmailResponse sendEmail(SesSendEmailRequest request) {

    }
}
