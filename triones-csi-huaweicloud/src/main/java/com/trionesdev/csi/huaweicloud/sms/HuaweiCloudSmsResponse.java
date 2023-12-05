package com.trionesdev.csi.huaweicloud.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HuaweiCloudSmsResponse {
    private Integer status;
}
