package com.trionesdev.csi.aliyun.oss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AliYunOssConfig implements Serializable {
    private static final long serialVersionUID = 5179840999312034726L;
    private String bucket;
    private String urlPrefix;
}
