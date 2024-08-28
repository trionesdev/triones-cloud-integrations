package com.trionesdev.csi.api.ses;

import com.trionesdev.csi.api.ses.request.SesSendEmailRequest;
import com.trionesdev.csi.api.ses.response.SesSendEmailResponse;

public interface SesTemplate {
    String template(String code);

    SesSendEmailResponse sendEmail(SesSendEmailRequest request);
}
