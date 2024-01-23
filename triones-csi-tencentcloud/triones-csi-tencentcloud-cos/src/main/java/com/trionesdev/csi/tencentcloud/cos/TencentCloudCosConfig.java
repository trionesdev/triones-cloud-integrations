package com.trionesdev.csi.tencentcloud.cos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TencentCloudCosConfig implements Serializable {
    private static final long serialVersionUID = 2904775516552050070L;
    private String bucket;
    private String urlPrefix;
}
