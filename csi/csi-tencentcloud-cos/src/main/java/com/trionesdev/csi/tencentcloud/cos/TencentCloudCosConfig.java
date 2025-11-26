package com.trionesdev.csi.tencentcloud.cos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TencentCloudCosConfig extends TencentCloudCosCredentials {
    private static final long serialVersionUID = 2904775516552050070L;
    private Boolean multi;
}
