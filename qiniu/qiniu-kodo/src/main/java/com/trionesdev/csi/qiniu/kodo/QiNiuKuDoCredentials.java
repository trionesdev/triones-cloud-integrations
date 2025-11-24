package com.trionesdev.csi.qiniu.kodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QiNiuKuDoCredentials implements Serializable {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String urlPrefix;
}
