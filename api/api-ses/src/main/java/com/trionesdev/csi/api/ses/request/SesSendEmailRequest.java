package com.trionesdev.csi.api.ses.request;

import com.trionesdev.csi.api.ses.SesVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SesSendEmailRequest {
    private String fromAddress;
    private String replyAddress;
    private String subject;
    private List<String> destinations;
    private String templateCode;
    private List<SesVariable> variables;
    private Map<String,String> extra;
}
