package com.trionesdev.csi.huaweicloud.obs;

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
public class HuaweiCloudObsConfig extends HuaweiCloudObsCredentials {
    private Boolean multi;
}
