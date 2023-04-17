package com.moensun.cloud.integration.qiniu.kodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QiNiuKoDoConfig implements Serializable {
    private static final long serialVersionUID = 2200810031665639938L;
    private String bucket;
    private String urlPrefix;
}
