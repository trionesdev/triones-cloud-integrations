package com.moensun.csi.api.sms.request;

import com.moensun.csi.api.sms.SmsParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SmsSendRequest {
    private String sender;
    private String signName;
    private String templateCode;
    private String phoneNumbers;
    private List<SmsParam> params;
    private String regionId;
}
