package com.trionesdev.csi.tencentcloud.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TenantCloudOcrConfig extends TenantCloudOcrCredentials{
    private Boolean multi;
}
