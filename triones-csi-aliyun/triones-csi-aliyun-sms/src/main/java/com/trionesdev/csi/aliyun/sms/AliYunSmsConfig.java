package com.trionesdev.csi.aliyun.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AliYunSmsConfig extends AliYunSmsCredentials implements Serializable {
    private static final long serialVersionUID = 8551713446967178352L;
    private Map<String,String> templateCodes;
}
