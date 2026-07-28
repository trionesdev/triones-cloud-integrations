package com.trionesdev.csi.rustfs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RustFsCredentials {
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucket;
    private String urlPrefix;
}
