package com.trionesdev.csi.api.ses.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SesSendEmailResponse {
    private String messageId;
    private String requestId;
}
