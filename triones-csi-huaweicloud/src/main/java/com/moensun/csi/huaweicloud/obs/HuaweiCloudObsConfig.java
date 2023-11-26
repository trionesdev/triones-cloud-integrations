package com.moensun.csi.huaweicloud.obs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HuaweiCloudObsConfig implements Serializable {
    private String bucket;
    private String urlPrefix;
}
