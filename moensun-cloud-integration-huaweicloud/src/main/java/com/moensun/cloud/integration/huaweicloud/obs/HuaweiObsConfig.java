package com.moensun.cloud.integration.huaweicloud.obs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HuaweiObsConfig implements Serializable {
    private String bucket;
    private String urlPrefix;
}
