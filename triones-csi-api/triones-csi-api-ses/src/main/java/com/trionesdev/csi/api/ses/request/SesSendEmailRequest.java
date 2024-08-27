package com.trionesdev.csi.api.ses.request;

import com.trionesdev.csi.api.ses.SesParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SesSendEmailRequest {
    private String fromEmailAddress;
    private String replyToEmailAddress;
    private String subject;
    private List<String> destinations;
    private String templateCode;
    private List<SesParam> params;
}
