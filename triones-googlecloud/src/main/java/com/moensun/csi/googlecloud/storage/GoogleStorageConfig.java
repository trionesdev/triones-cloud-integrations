package com.moensun.csi.googlecloud.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleStorageConfig implements Serializable {
    private static final long serialVersionUID = -5055632326502646993L;
    private String bucket;
    private String urlPrefix;
}
