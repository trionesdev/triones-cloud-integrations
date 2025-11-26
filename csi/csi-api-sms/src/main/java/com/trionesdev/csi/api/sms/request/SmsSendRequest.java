package com.trionesdev.csi.api.sms.request;

import com.trionesdev.csi.api.sms.SmsVariable;
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
public class SmsSendRequest {
    private String signName;
    private String templateCode;
    private List<String> phoneNumbers;
    private List<SmsVariable> variables;
    private String regionId;
    private String content;
    private Map<String,String> extra;
}
