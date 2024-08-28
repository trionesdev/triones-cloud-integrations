package com.trionesdev.csi.tencentcloud.ses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TencentCloudSesConfig implements Serializable {
    private String region;
}
