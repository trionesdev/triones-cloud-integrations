package com.trionesdev.csi.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MinioConfig implements Serializable {
    private static final long serialVersionUID = 5129214195191527384L;
    private String bucket;
    private String urlPrefix;
}
